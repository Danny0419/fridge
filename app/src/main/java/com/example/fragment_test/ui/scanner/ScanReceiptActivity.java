package com.example.fragment_test.ui.scanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fragment_test.R;
import com.example.fragment_test.ScannerList.OcrActivity;
import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.entity.Invoice;
import com.example.fragment_test.entity.InvoiceItem;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.fragment_test.entity.RefrigeratorIngredient;

public class ScanReceiptActivity extends AppCompatActivity {
    //相機掃描
    //private DecoratedBarcodeView barcodeView;

    // 用于存储已识别的QR码信息
    private Set<String> recognizedQrCodes = new HashSet<>();
    // 需要识别的QR码数量
    private static final int REQUIRED_QRCODES = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);

        //開啟相機
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setPrompt("Scan a QR code");
        intentIntegrator.setCameraId(0);  // 使用特定的摄像头
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();

        //barcodeView = findViewById(R.id.camara);
        //barcodeView.resume();  // 启动相机预览

        /*
        個按鈕之點擊
        */
        Button goBackBtn = findViewById(R.id.goBackBnt);  //返回按鈕
        Button scanReceiptButton = findViewById(R.id.scan_receipt_button);    //掃描發票orOCR按鈕
        Button addManuallyButton = findViewById(R.id.add_manually_button);    //手動輸入按鈕
        LinearLayout scanLayout = findViewById(R.id.scan_layout);   //掃描發票orOCR按鈕區塊
        LinearLayout addManuallyLayout = findViewById(R.id.add_manually_layout);    //手動輸入區塊

        //返回
        goBackBtn.setOnClickListener(v -> finish());

        //掃描發票orOCR
        scanReceiptButton.setOnClickListener(v -> {
            addManuallyLayout.setVisibility(View.GONE);
            scanLayout.setVisibility(View.VISIBLE);
        });

        //手動輸入
        addManuallyButton.setOnClickListener(v -> {
            addManuallyLayout.setVisibility(View.VISIBLE);
            scanLayout.setVisibility(View.GONE);
        });
        Button albumBtn = findViewById(R.id.albumBnt);
        albumBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, OcrActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 处理QR码扫描结果
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String contents = scanResult.getContents();
            if (contents != null) {
                // 将扫描到的内容添加到集合中
                recognizedQrCodes.add(contents);
                // 检查是否已经扫描到所需数量的QR码
                if (recognizedQrCodes.size() >= REQUIRED_QRCODES) {
                    // 处理识别到的QR码
                    handleRecognizedQrCodes(recognizedQrCodes);
                } else {
                    // 提示用户已扫描的QR码，并继续扫描
                    Toast.makeText(this, "Scanned: " + contents, Toast.LENGTH_SHORT).show();
                    IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                    intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                    intentIntegrator.initiateScan();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleRecognizedQrCodes(Set<String> qrCodes) {
        // 合并QR码信息并解析发票和品项
        StringBuilder combinedInfo = new StringBuilder();
        List<ParsedItem> items = new ArrayList<>();
        String invoiceDate = null;

        List<String> beforeStarStar = new ArrayList<>();
        List<String> afterStarStar = new ArrayList<>();

        for (String qrCode : qrCodes) {
            if (qrCode.startsWith("**")) {
                afterStarStar.add(qrCode.replaceFirst("\\*\\*", ":"));
            } else {
                beforeStarStar.add(qrCode);
            }
        }

        for (String qrCode : beforeStarStar) {
            combinedInfo.append(qrCode);
        }
        for (String qrCode : afterStarStar) {
            combinedInfo.append(qrCode);
        }

        String combinedText = combinedInfo.toString().replaceAll("\\s+", "");
        Log.i("QR CODE SCANNER", "合并后的完整信息: " + combinedText);

        // 解析发票信息
        ParsedInvoice parsedInvoice = parseInvoiceData(combinedText);
        invoiceDate = parsedInvoice.getDate();
        items.addAll(parsedInvoice.getItems());

        // 建立要保存的數據
        if (invoiceDate != null) {
            // 创建后台线程池
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            // 获取数据库实例
            FridgeDatabase db = FridgeDatabase.getInstance(getApplicationContext());

            // 在后台线程中执行插入操作
            String finalInvoiceDate = invoiceDate;
            executorService.execute(() -> {
                // 插入发票数据
                Invoice invoice = new Invoice(finalInvoiceDate);
                long invoiceId = db.invoiceDAO().insertInvoice(invoice);

                // 插入发票品项数据
                List<InvoiceItem> invoiceItems = new ArrayList<>();
                for (ParsedItem item : items) {
                    InvoiceItem invoiceItem = new InvoiceItem((int) invoiceId, item.getName(), item.getQuantity(), item.getPrice());
                    invoiceItems.add(invoiceItem);
                }
                db.invoiceItemDAO().insertInvoiceItems(invoiceItems);

                // 將 ParsedItem 數據插入到 RefrigeratorIngredient
                for (ParsedItem item : items) {
                    RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(
                            0, // 假設 ID 是自動生成的
                            item.getName(),
                            Integer.parseInt(item.getQuantity()), // 確保數量是整數
                            null, // 圖片可以根據需要設置
                            null, // 類別可以根據需要設置
                            null, // 購買日期可以根據需要設置
                            null  // 到期日期可以根據需要設置
                    );
                    db.refrigeratorIngredientDAO().insertRefrigeratorIngredient(refrigeratorIngredient);
                }

                // 提示用戶成功
                runOnUiThread(() -> {
                    Toast.makeText(this, "發票和品項已成功保存", Toast.LENGTH_LONG).show();
                    finish();
                });
            });

            executorService.shutdown(); // 在適當的時候關閉線程池
        }
    }

    private ParsedInvoice parseInvoiceData(String qrText) {
        // 提取发票日期：QR码文本长度大于等于17时，从第11到第17个字符位置提取
        String date = qrText.length() >= 17 ? qrText.substring(10, 17) : "";
        // 按":"分割文本，得到多个部分
        String[] items = qrText.split(":");
        // 用于存储解析出的品项
        List<ParsedItem> resultItems = new ArrayList<>();

        int i = 0;
        while (i < items.length) {
            // 如果当前部分不是数字且不等于"**********"，且后面两个部分都是数字，说明是品项名称
            if (!items[i].matches("\\d+") && !items[i].equals("**********")) {
                // 确保后面有两个部分，分别是数量和价格
                if (i + 2 < items.length && items[i + 1].matches("\\d+") && items[i + 2].matches("\\d+")) {
                    String itemName = items[i];        // 品项名称
                    String itemQuantity = items[i + 1]; // 数量
                    String itemPrice = items[i + 2];    // 价格
                    // 创建品项对象并添加到列表中
                    resultItems.add(new ParsedItem(itemName, itemQuantity, itemPrice));
                    // 跳过已处理的三个部分
                    i += 3;
                } else {
                    // 如果不满足条件，则跳过当前部分
                    i++;
                }
            } else {
                // 如果当前部分是数字或"**********"，跳过
                i++;
            }
        }
        // 返回包含发票日期和品项列表的ParsedInvoice对象
        return new ParsedInvoice(date, resultItems);
    }

    // 自定义的解析发票和品项的类
    private static class ParsedInvoice {
        private String date;
        private List<ParsedItem> items;

        public ParsedInvoice(String date, List<ParsedItem> items) {
            this.date = date;
            this.items = items;
        }

        public String getDate() {
            return date;
        }

        public List<ParsedItem> getItems() {
            return items;
        }
    }

    // 自定义的品项类
    private static class ParsedItem {
        private String name;
        private String quantity;
        private String price;

        public ParsedItem(String name, String quantity, String price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getPrice() {
            return price;
        }
    }
}
