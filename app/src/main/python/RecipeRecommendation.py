import json
import pandas as pd
import re
from datetime import datetime, timedelta

class RecipeRecommender:
    def __init__(self, recipes_file):
        self.recipes_df = self.load_recipes(recipes_file)

    def load_recipes(self, file_path):
        with open(file_path, 'r', encoding='utf-8') as file:
            recipes = json.load(file)
        return pd.DataFrame(recipes)

    def preprocess_recipes(self):
        self.recipes_df['難易度'] = self.recipes_df['難易度'].apply(lambda x: x.split()[-1])
        self.recipes_df['準備時間'] = self.recipes_df['準備時間'].apply(lambda x: int(re.search(r'\d+', x).group()))
        self.recipes_df['總共時間'] = self.recipes_df['總共時間'].apply(lambda x: int(re.search(r'\d+', x).group()))
        self.recipes_df['份量'] = self.recipes_df['份量'].apply(lambda x: int(re.search(r'\d+', x).group()))
        self.recipes_df['食材'] = self.recipes_df['食材'].apply(self.process_ingredients)
        self.recipes_df['卡路里'] = self.recipes_df['營養價值'].apply(lambda x: float(re.search(r'\d+', x['卡路里']).group()))
        self.recipes_df['評分'] = self.recipes_df['評分'].apply(self.process_rating)
        print(f"預處理後的食譜數量：{len(self.recipes_df)}")

    def process_ingredients(self, ingredients):
        # 需跳過的調味料
        seasonings = ['鹽', '油', '醬油', '蠔油', '米酒', '香油', '烏醋', '糖', '胡椒粉']
        processed = []
        for item in ingredients:
            parts = item.strip().split()
            if len(parts) > 1:
                try:
                    quantity = float(parts[0])
                    ingredient_name = parts[-1]
                    if ingredient_name not in seasonings:
                        processed.append((ingredient_name, quantity))
                except ValueError:
                    continue
        return processed

    def process_rating(self, rating):
        try:
            return float(re.search(r'\d+(\.\d+)?', str(rating)).group())
        except (ValueError, AttributeError):
            return 0.0

    def get_fridge_ingredients(self):
        today = datetime.now()
        # 冰箱現有食材量
        ingredients = {
            '雞蛋': {'quantity': 10, 'expiry_date': today + timedelta(days=10)},
            '牛奶': {'quantity': 2000, 'expiry_date': today + timedelta(days=5)},
            '洋蔥': {'quantity': 500, 'expiry_date': today + timedelta(days=20)},
            '奶油': {'quantity': 100, 'expiry_date': today + timedelta(days=3)},
            '牛肉': {'quantity': 300, 'expiry_date': today + timedelta(days=8)},
            '雞肉': {'quantity': 400, 'expiry_date': today + timedelta(days=12)},
            '豆腐': {'quantity': 200, 'expiry_date': today + timedelta(days=7)},
            '胡蘿蔔': {'quantity': 150, 'expiry_date': today + timedelta(days=15)},
            '西紅柿': {'quantity': 250, 'expiry_date': today + timedelta(days=6)},
            '蘑菇': {'quantity': 180, 'expiry_date': today + timedelta(days=4)},
            '黃瓜': {'quantity': 200, 'expiry_date': today + timedelta(days=14)},
            '青椒': {'quantity': 120, 'expiry_date': today + timedelta(days=9)},
            '大蒜': {'quantity': 50, 'expiry_date': today + timedelta(days=30)},
            '紅辣椒': {'quantity': 30, 'expiry_date': today + timedelta(days=25)},
            '蘋果': {'quantity': 6, 'expiry_date': today + timedelta(days=10)},
            '香蕉': {'quantity': 8, 'expiry_date': today + timedelta(days=7)},
            '土豆': {'quantity': 500, 'expiry_date': today + timedelta(days=18)},
            '芝士': {'quantity': 100, 'expiry_date': today + timedelta(days=12)},
            '酸奶': {'quantity': 600, 'expiry_date': today + timedelta(days=9)},
            '面包': {'quantity': 1, 'expiry_date': today + timedelta(days=5)},
            '義大利面': {'quantity': 500, 'expiry_date': today + timedelta(days=365)},
            '橄欖油': {'quantity': 250, 'expiry_date': today + timedelta(days=730)},
        }
        for item, info in ingredients.items():
            expiry_date = info['expiry_date']
            remaining_days = (expiry_date - today).days
            ingredients[item]['expiry_date'] = max(0, remaining_days)
        return ingredients

    def get_recommendations(self, fridge_ingredients, user_preferences):
        recommendations = []

        for i, recipe in self.recipes_df.iterrows():
            total_match_score = 0
            total_expiry_score = 0
            matched_ingredients_count = 0

            # 匹配得分
            # 冰箱食材與食譜食材match且足夠則匹配，（匹配食材/（食譜食材-調味料））*2
            recipe_ingredients_count = len(recipe['食材'])
            for ingredient, quantity in recipe['食材']:
                if ingredient in fridge_ingredients:
                    fridge_info = fridge_ingredients[ingredient]
                    if quantity <= fridge_info['quantity']:
                        match_score = min(quantity / fridge_info['quantity'], 1.0)
                        total_match_score += 1
                        matched_ingredients_count += 1

            if recipe_ingredients_count > 0:
                match_rate = matched_ingredients_count / recipe_ingredients_count
                match_score = match_rate * 2
            else:
                match_score = 0

            # 新鮮度得分
            # 匹配食材的新鮮度進行加總平均，（sum（if有匹配到&足夠's新鮮度配分）/匹配食材/（食譜食材-調味料））
            for ingredient, quantity in recipe['食材']:
                if ingredient in fridge_ingredients:
                    fridge_info = fridge_ingredients[ingredient]
                    expiry_score = self.calculate_expiry_score(fridge_info['expiry_date'])
                    total_expiry_score += expiry_score

            if matched_ingredients_count > 0:
                average_expiry_score = total_expiry_score / matched_ingredients_count
            else:
                average_expiry_score = 0

            combined_score = match_score + average_expiry_score
            recommendations.append((recipe, match_score, average_expiry_score, combined_score))

        recommendations.sort(key=lambda x: x[3], reverse=True)

        # 控制輸出食譜量
        return recommendations[:10]

    def calculate_expiry_score(self, days_remaining):
        # 新鮮度配分
        if days_remaining <= 3:
            return 5
        elif days_remaining <= 7:
            return 3
        else:
            return 1

def main():
    recommender = RecipeRecommender('data/cookidoo.json')
    recommender.preprocess_recipes()

    fridge_ingredients = recommender.get_fridge_ingredients()
    user_preferences = {}  # 假設有一些用戶偏好
    recommendations = recommender.get_recommendations(fridge_ingredients, user_preferences)

    for recipe, match_score, expiry_score, combined_score in recommendations:
        print(f"標題: {recipe['標題']}")
        # print(f"難易度: {recipe['難易度']}")
        # print(f"準備時間: {recipe['準備時間']} 分鐘")
        # print(f"總共時間: {recipe['總共時間']} 分鐘")
        # print(f"份量: {recipe['份量']} 人份")
        # print(f"卡路里: {recipe['卡路里']} kcal")
        # print(f"評分: {recipe['評分']}")
        # print(f"匹配得分: {match_score}")
        # print(f"新鮮度得分: {expiry_score}")
        print(f"綜合得分: {combined_score}")
        # print("所需食材:")
        # for ingredient in recipe['食材']:
        #     print(f"  {ingredient[0]}: {ingredient[1]}")
        # print("冰箱中的匹配食材量:")
        # for ingredient in recipe['食材']:
        #     item = ingredient[0]
        #     if item in fridge_ingredients:
        #         print(f"  {item}: {fridge_ingredients[item]['quantity']} 克")
        # print("冰箱所缺少的食材:")
        # for ingredient in recipe['食材']:
        #     item = ingredient[0]
        #     if item in fridge_ingredients:
        #         if fridge_ingredients[item]['quantity'] < ingredient[1]:
        #             print(f"  {item}: {ingredient[1] - fridge_ingredients[item]['quantity']} 克")
        #     else:
        #         print(f"  {item}: {ingredient[1]} 克")
        print("")

if __name__ == "__main__":
    main()
# def main():
#     print("Hello Python")
