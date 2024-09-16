package com.example.fragment_test.ScannerList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OcrActivity extends AppCompatActivity {

    private static final String TAG = "OcrActivity";

    private Button buttonRecognizeImage;
    private TextView textViewDate;
    private RecyclerView recyclerViewItems;
    private ItemAdapter itemAdapter;

    private TextRecognizer recognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        buttonRecognizeImage = findViewById(R.id.buttonRecognizeImage);
        textViewDate = findViewById(R.id.textViewDate);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);

        recognizer = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());

        buttonRecognizeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recognizeImageFromDrawable();
            }
        });

        itemAdapter = new ItemAdapter(new ArrayList<>());
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems.setAdapter(itemAdapter);
    }

    private void recognizeImageFromDrawable() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scannerlist01);
        processImage(bitmap);
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
        for (Item item : items) {
            Log.d(TAG, "Item: " + item.getName() + ", Quantity: " + item.getQuantity() + ", Amount: " + item.getAmount());
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
        Pattern patternItem = Pattern.compile("^[\\*\\+](\\d+)\\s*(.+)");
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
                    items.add(new Item(itemName, quantity, totalPrice));
                }
                continue;
            }

            Matcher itemMatcher = patternItem.matcher(text);
            if (itemMatcher.find()) {
                if (currentItem != null) {
                    items.add(currentItem);
                }
                currentItem = new Item(itemMatcher.group(2), "1", "");
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
}