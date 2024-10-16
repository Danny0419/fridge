package com.example.fragment_test.ScannerList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.ServerAPI.*;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class OcrActivity extends AppCompatActivity {

    private static final String TAG = "OcrActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button buttonRecognizeImage;
    private TextView textViewDate;
    private TextView textViewItems;
    private RecyclerView recyclerViewItems;
    private ItemAdapter itemAdapter;

    private TextRecognizer recognizer;
    private ExecutorService apiExecutor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        buttonRecognizeImage = findViewById(R.id.buttonRecognizeImage);
        textViewDate = findViewById(R.id.textViewDate);
        textViewItems = findViewById(R.id.textViewItems);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);

        recognizer = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());
        apiExecutor = Executors.newSingleThreadExecutor();

        buttonRecognizeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

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

        String invoiceDate = extractDate(allLines);
        List<Item> items = extractItems(allLines);

        Log.d(TAG, "Invoice Date: " + invoiceDate);
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            Log.d(TAG, "Item: " + item.getName() + ", Quantity: " + item.getQuantity() + ", Amount: " + item.getAmount());
            callApiWithProductName(item.getName(), items, i);  // 傳入 items 列表和當前索引
        }

        textViewDate.setText("發票日期: " + invoiceDate);
        itemAdapter.updateItems(items);
    }



    private String extractDate(List<String> lines) {
        Pattern patternDate = Pattern.compile("\\b\\d{4}\\s*/\\s*\\d{2}\\s*/\\s*\\d{2}\\b");
        for (String line : lines) {
            Matcher matcher = patternDate.matcher(line);
            if (matcher.find()) {
                return matcher.group().replaceAll("\\s*", "");
            }
        }
        return "未找到日期";
    }

    private List<Item> extractItems(List<String> lines) {
        List<Item> items = new ArrayList<>();
        Pattern patternItem = Pattern.compile("^[\\*\\+#](\\d+)\\s*(.+)");
        Pattern patternPrice = Pattern.compile("^(\\d+)(?:TX)?$");
        Pattern patternQuantity = Pattern.compile("^[\\*\\+](\\d+)$");
        Pattern patternUnitPrice = Pattern.compile("^\\$(\\d+)");
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

    private void callApiWithProductName(String productName, List<Item> items, int itemIndex) {
        // 清除不必要字符
        String cleanedProductName = productName.replaceAll("[^a-zA-Z0-9\u4e00-\u9fa5]", "").trim();
        apiExecutor.execute(() -> {
            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<List<CombinedIngredient>> call = apiService.getCombinedIngredients(cleanedProductName);
            Response<List<CombinedIngredient>> response = null;
            try {
                response = call.execute();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<CombinedIngredient> ingredients = response.body();
                    // 更新 UI，只傳有回應資料的商品
                    runOnUiThread(() -> updateUIWithIngredients(ingredients, items, itemIndex));
                } else {
                    Log.d(TAG, "無法獲取商品信息: " + productName);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "API 呼叫失敗: " + e.getMessage());
            }
        });
    }


    private void updateUIWithIngredients(List<CombinedIngredient> ingredients, List<Item> items, int itemIndex) {
        // 確保只有該商品對應有資料才會顯示
        if (itemIndex < items.size()) {
            Item item = items.get(itemIndex);
            StringBuilder result = new StringBuilder(textViewItems.getText());
            for (CombinedIngredient ingredient : ingredients) {
                result.append("商品原始名稱: ").append(item.getName()).append("\n")
                        .append("數量: ").append(item.getQuantity()).append("\n")
                        .append("商品種類: ").append(ingredient.getIngredients_category()).append("\n")
                        .append("商品轉換名稱: ").append(ingredient.getIngredient_Name()).append("\n")
                        .append("保存期限: ").append(ingredient.getExpiration()).append(" 天\n")
                        .append("-------------------------\n");
            }
            textViewItems.setText(result.toString());
        }
    }
}


