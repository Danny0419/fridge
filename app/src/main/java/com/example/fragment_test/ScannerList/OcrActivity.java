package com.example.fragment_test.ScannerList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.MainActivity2;
import com.example.fragment_test.R;
import com.example.fragment_test.ServerAPI.*;
import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RefrigeratorIngredientDAO;
import com.example.fragment_test.databinding.ActivityOcrBinding;
import com.example.fragment_test.databinding.ScanIngredientConfirmBinding;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Response;

import java.time.temporal.ChronoUnit;

public class OcrActivity extends AppCompatActivity {
    private static final String TAG = "OcrActivity";
    private static final int PICK_IMAGE_REQUEST = 1;
    private ScanIngredientConfirmBinding scanIngredientConfirmBinding;
    private ActivityOcrBinding activityOcrBinding;

    private Button buttonRecognizeImage;
    private TextView textViewDate;
    private TextView textViewItems;
    private RecyclerView recyclerViewItems;
    private ItemAdapter itemAdapter;
    private String invoiceDate;

    private TextRecognizer recognizer;
    private ExecutorService apiExecutor;

    private FridgeDatabase db;
    private RefrigeratorIngredientDAO refrigeratorIngredientDAO;

    // 新增用於追蹤API調用的變數
    private int apiCallsCompleted = 0;
    private List<CombinedIngredient> allMatchedIngredients = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        buttonRecognizeImage = findViewById(R.id.buttonRecognizeImage);
        textViewDate = findViewById(R.id.textViewDate);
        textViewItems = findViewById(R.id.textViewItems);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);

        scanIngredientConfirmBinding = ScanIngredientConfirmBinding.inflate(getLayoutInflater());

        recognizer = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());
        apiExecutor = Executors.newSingleThreadExecutor();

        db = FridgeDatabase.getInstance(this);
        refrigeratorIngredientDAO = db.refrigeratorIngredientDAO();

        openGallery();

        itemAdapter = new ItemAdapter(new ArrayList<>());
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems.setAdapter(itemAdapter);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                processImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "無法讀取所選圖片", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void processImage(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        recognizer.process(image)
                .addOnSuccessListener(this::processText)
                .addOnFailureListener(e -> Toast.makeText(OcrActivity.this, "OCR失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void processText(Text text) {
        List<String> allLines = new ArrayList<>();
        for (Text.TextBlock block : text.getTextBlocks()) {
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
                allLines.add(lineText);
                Log.d(TAG, "Recognized text: " + lineText);
            }
        }

        invoiceDate = extractDate(allLines);
        List<Item> items = extractItems(allLines);

        // 重置計數器和列表
        apiCallsCompleted = 0;
        allMatchedIngredients = new ArrayList<>(Collections.nCopies(items.size(), null));

        // 為每個項目調用API
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            Log.d(TAG, "Processing item: " + item.getName());
            callApiWithProductName(item.getName(), items, i, invoiceDate);
        }

        itemAdapter.updateItems(items);
    }

    private String extractDate(List<String> lines) {
        Pattern patternDate = Pattern.compile("\\b\\d{4}\\s*/\\s*\\d{2}\\s*/\\s*\\d{2}\\b");
        for (String line : lines) {
            Matcher matcher = patternDate.matcher(line);
            if (matcher.find()) {
                String dateStr = matcher.group().replaceAll("\\s*", "");
                try {
                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date date = originalFormat.parse(dateStr);
                    SimpleDateFormat targetFormatForDb = new SimpleDateFormat("yyyyMMdd");
                    return targetFormatForDb.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private List<Item> extractItems(List<String> lines) {
        List<Item> items = new ArrayList<>();
        Pattern patternItem = Pattern.compile("^([\\*\\+#]\\d+|66)\\s*(.+)"); //商品
        Pattern patternPrice = Pattern.compile("^(\\d+)(?:TX)?$");   //價錢
        Pattern patternQuantity = Pattern.compile("^[\\*\\+](\\d+)$"); //數量
        Pattern patternUnitPrice = Pattern.compile("^\\$(\\d+)"); //單一價錢
        Pattern patternFullItem = Pattern.compile("\\$(\\d+)\\s*[\\*\\+](\\d+)\\s*(\\d+)TX");

        Item currentItem = null;

        for (int i = 0; i < lines.size(); i++) {
            String text = lines.get(i);
            Matcher fullItemMatcher = patternFullItem.matcher(text);
            if (fullItemMatcher.find()) {
                String unitPrice = fullItemMatcher.group(1);
                String quantity = fullItemMatcher.group(2);
                String totalPrice = fullItemMatcher.group(3);
                if (i > 0) {
                    String itemName = lines.get(i - 1);
                    String changedName = "default_changed_name";
                    String expiration = "default_expiration";
                    items.add(new Item(itemName, quantity, totalPrice, changedName, expiration));
                }
                continue;
            }

            Matcher itemMatcher = patternItem.matcher(text);
            if (itemMatcher.find()) {
                if (currentItem != null) {
                    items.add(currentItem);
                }
                currentItem = new Item(itemMatcher.group(2), "1", "", "default_changed_name", "default_expiration");
                Matcher priceMatcher = patternPrice.matcher(text);
                if (priceMatcher.find()) {
                    currentItem.setAmount(priceMatcher.group(1));
                } else if (i + 1 < lines.size()) {
                    String nextLine = lines.get(i + 1);
                    priceMatcher = patternPrice.matcher(nextLine);
                    if (priceMatcher.find()) {
                        currentItem.setAmount(priceMatcher.group(1));
                    }
                }
                continue;
            }

            if (currentItem != null && currentItem.getAmount().isEmpty()) {
                Matcher unitPriceMatcher = patternUnitPrice.matcher(text);
                if (unitPriceMatcher.find()) {
                    currentItem.setAmount(unitPriceMatcher.group(1));
                    if (i + 1 < lines.size()) {
                        String nextLine = lines.get(i + 1);
                        Matcher quantityMatcher = patternQuantity.matcher(nextLine);
                        if (quantityMatcher.find()) {
                            currentItem.setQuantity(quantityMatcher.group(1));
                            int total = Integer.parseInt(currentItem.getAmount()) * Integer.parseInt(currentItem.getQuantity());
                            currentItem.setAmount(String.valueOf(total));
                        }
                    }
                } else if (text.matches("^\\d+$")) {
                    currentItem.setAmount(text);
                }
            }
        }

        if (currentItem != null) {
            items.add(currentItem);
        }

        return items;
    }

    private void callApiWithProductName(String productName, List<Item> items, int itemIndex, String invoiceDate) {
        String cleanedProductName = productName.replaceAll("[^a-zA-Z0-9\u4e00-\u9fa5]", "").trim();

        apiExecutor.execute(() -> {
            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<List<CombinedIngredient>> call = apiService.getCombinedIngredients(cleanedProductName);

            try {
                Response<List<CombinedIngredient>> response = call.execute();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<CombinedIngredient> responseIngredients = response.body();

                    synchronized (this) {
                        if (!responseIngredients.isEmpty()) {
                            allMatchedIngredients.set(itemIndex, responseIngredients.get(0));
                            Log.d(TAG, "API Success for item " + itemIndex + ": " + responseIngredients.get(0).getIngredient_Name());
                        }

                        updateDBWithIngredients(responseIngredients, items, itemIndex);
                        apiCallsCompleted++;

                        Log.d(TAG, "API calls completed: " + apiCallsCompleted + "/" + items.size());

                        if (apiCallsCompleted == items.size()) {
                            runOnUiThread(() -> showFinalDialog(items, allMatchedIngredients));
                        }
                    }
                } else {
                    handleApiFailure(items, itemIndex);
                }
            } catch (IOException e) {
                e.printStackTrace();
                handleApiFailure(items, itemIndex);
            }
        });
    }

    private void handleApiFailure(List<Item> items, int itemIndex) {
        synchronized (this) {
            apiCallsCompleted++;
            Log.d(TAG, "API call failed for item " + itemIndex + ". Total completed: " + apiCallsCompleted + "/" + items.size());
            if (apiCallsCompleted == items.size()) {
                runOnUiThread(() -> showFinalDialog(items, allMatchedIngredients));
            }
        }
    }

    private void showFinalDialog(List<Item> items, List<CombinedIngredient> matchedIngredients) {
        Dialog dialog = new Dialog(OcrActivity.this);
        ScanIngredientConfirmBinding dialogBinding = ScanIngredientConfirmBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        JumpAdapter jumpAdapter = new JumpAdapter(items, matchedIngredients);
        dialogBinding.shoppingListItemRecyclerview.setAdapter(jumpAdapter);
        dialogBinding.shoppingListItemRecyclerview.setLayoutManager(new LinearLayoutManager(OcrActivity.this));

        dialogBinding.continueButton.setOnClickListener(v -> {
            dialog.dismiss();
            openGallery();
        });

        dialogBinding.confirmButton.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(OcrActivity.this, MainActivity2.class);
            startActivity(intent);
            finish();
        });

        // 應急用調整彈跳視窗大小
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = 1000;
        layoutParams.height = 1020;
        dialog.getWindow().setAttributes(layoutParams);

        dialog.show();

    }
//    private void callApiWithProductName(String productName, List<Item> items, int itemIndex, String invoiceDate) {
//        // 清除不必要字符
//        String cleanedProductName = productName.replaceAll("[^a-zA-Z0-9\u4e00-\u9fa5]", "").trim();
//
//        apiExecutor.execute(() -> {
//            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
//            Call<List<CombinedIngredient>> call = apiService.getCombinedIngredients(cleanedProductName);
//
//            try {
//                Response<List<CombinedIngredient>> response = call.execute();
//                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
//                    List<CombinedIngredient> ingredients = response.body();
//
//                    // 更新 UI 和顯示對話框
//                    runOnUiThread(() -> {
//                        // 更新 UI
//                        updateUIWithIngredients(ingredients, items, itemIndex);
//                        // 初始化並顯示對話框
//                        showIngredientDialog(ingredients);
//                    });
//                    updateDBWithIngredients(ingredients, items, itemIndex);
//                } else {
//                    Log.d(TAG, "無法獲取商品信息: " + productName);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.d(TAG, "API 呼叫失敗: " + e.getMessage());
//            }
//        });
//    }
//
//    // 顯示對話框的獨立方法
//    private void showIngredientDialog(List<CombinedIngredient> ingredients) {
//        // 確認當前的 Activity 上下文
//        Dialog dialog = new Dialog(OcrActivity.this);
//
//        // 使用 ViewBinding 或 DataBinding 初始化佈局
//        ScanIngredientConfirmBinding binding = ScanIngredientConfirmBinding.inflate(getLayoutInflater());
//        dialog.setContentView(binding.getRoot());
//
//        // 將資料綁定到對話框的 UI 元件
//        binding.getRoot(); // 假設在佈局中有一個 setIngredients 方法來綁定資料
//
//        // 顯示對話框
//        dialog.show();
//    }
//
////     更新 UI 資料的邏輯
//    private void updateUIWithIngredients(List<CombinedIngredient> ingredients, List<Item> items, int itemIndex) {
//        // 這裡可以對 UI 中的其他元件進行更新，例如在清單中顯示成分或更新產品狀態
//        jumpAdapter = new JumpAdapter(items);
//        Log.i("TAG",items.toString());
////        items.get(itemIndex).getChangedName();//轉換後名稱
////        ingredients.get(itemIndex).getIngredients_category();//種類
////        items.get(itemIndex).getQuantity();//數量
////        items.get(itemIndex).getExpiration();//保存期限
//        // 假設你有一個 RecyclerView 或 ListView，可以在此處通知資料更新
//
//        jumpAdapter.notifyItemChanged(itemIndex);
//    }



    private void updateDBWithIngredients(List<CombinedIngredient> ingredients, List<Item> items, int itemIndex) {
        if (itemIndex < items.size()) {
            Item item = items.get(itemIndex);

            // 明確指定時區
            LocalDate todayDate = LocalDate.now(ZoneId.of("Asia/Taipei"));
            String formattedTodayDate = todayDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    LocalDate invoiceDateLocal = LocalDate.parse(invoiceDate, formatter);

                    for (CombinedIngredient ingredient : ingredients) {
                        try {
                            // 計算保存期限日期
                            LocalDate expirationDate = invoiceDateLocal.plusDays(ingredient.getExpiration());
                            int formattedExpirationDate = Integer.parseInt(expirationDate.format(formatter));
                            int invoiceDateInt = Integer.parseInt(invoiceDate);

                            // 檢查數據有效性
                            if (ingredient.getIngredient_Name() == null || ingredient.getIngredient_Name().isEmpty()) {
                                throw new IllegalArgumentException("Ingredient name cannot be null or empty");
                            }

                            if (ingredient.getGrams() == null || ingredient.getGrams().isEmpty()) {
                                throw new IllegalArgumentException("Grams cannot be null or empty");
                            }
//int id, @NonNull String name, int quantity, String img, String sort, Integer purchaseDate, Integer expiration, String unit
                            RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(

                                    0,
                                    ingredient.getIngredient_Name(),
                                    Integer.parseInt(ingredient.getGrams()),
                                    ingredient.getIngredient_pictures(),
                                    ingredient.getIngredients_category(),
                                    invoiceDateInt,
                                    formattedExpirationDate,
                                    ingredient.getUnit()// 根據保存期限計算狀態
                            );

                            // 在事務中執行插入操作
                            db.runInTransaction(() -> {
                                long result = refrigeratorIngredientDAO.insertIngredient(refrigeratorIngredient);
                                if (result > 0) {
                                    Log.d(TAG, "成功寫入資料庫: " + refrigeratorIngredient.getName());
                                } else {
                                    throw new RuntimeException("Insert operation failed");
                                }
                                return null;
                            });

                            // 更新UI
                          //  runOnUiThread(() -> updateUI(item, ingredient));

                        } catch (NumberFormatException e) {
                            Log.e(TAG, "數值轉換錯誤: " + e.getMessage(), e);
                            showError("數值格式錯誤");
                        } catch (IllegalArgumentException e) {
                            Log.e(TAG, "參數錯誤: " + e.getMessage(), e);
                            showError("資料格式錯誤");
                        } catch (Exception e) {
                            Log.e(TAG, "資料庫操作錯誤: " + e.getMessage(), e);
                            showError("資料庫寫入失敗");
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "整體處理錯誤: " + e.getMessage(), e);
                    showError("處理過程發生錯誤");
                }
            });
            executor.shutdown();
        }
    }

    // 計算食材狀態
    private String calculateStatus(int expirationDate) {
        LocalDate today = LocalDate.now();
        LocalDate expDate = LocalDate.parse(String.valueOf(expirationDate),
                DateTimeFormatter.ofPattern("yyyyMMdd"));
        long daysUntilExpiration = ChronoUnit.DAYS.between(today, expDate);

        if (daysUntilExpiration < 0) {
            return "已過期";
        } else if (daysUntilExpiration <= 3) {
            return "即將過期";
        } else {
            return "正常";
        }
    }

    // 更新UI的方法
//    private void updateUI(Item item, CombinedIngredient ingredient) {
//        StringBuilder result = new StringBuilder(textViewItems.getText());
//        result.append("商品原始名稱: ").append(item.getName()).append("\n")
//                .append("數量: ").append(item.getQuantity()).append("\n")
//                .append("商品種類: ").append(ingredient.getIngredients_category()).append("\n")
//                .append("商品轉換名稱: ").append(ingredient.getIngredient_Name()).append("\n")
//                .append("保存期限: ").append(ingredient.getExpiration()).append(" 天\n")
//                .append("-------------------------\n");
//        textViewItems.setText(result.toString());
//    }

    // 顯示錯誤訊息
    private void showError(String message) {
        runOnUiThread(() ->
                Toast.makeText(OcrActivity.this, message, Toast.LENGTH_SHORT).show()
        );
    }
}





