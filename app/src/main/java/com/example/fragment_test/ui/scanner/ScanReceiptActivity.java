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
import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.entity.Invoice;
import com.example.fragment_test.entity.InvoiceItem;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ScanReceiptActivity extends AppCompatActivity {

    // 用于存储已识别的QR码信息
    private Set<String> recognizedQrCodes = new HashSet<>();
    // 需要识别的QR码数量
    private static final int REQUIRED_QRCODES = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setPrompt("Scan a QR code");
        intentIntegrator.setCameraId(0);  // 使用特定的摄像头
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();

        //點擊
        Button goBackBtn=findViewById(R.id.goBackBnt);  //返回按鈕
        Button scanReceiptButton=findViewById(R.id.scan_receipt_button);    //掃描發票orOCR按鈕
        Button addManuallyButton=findViewById(R.id.add_manually_button);    //手動輸入按鈕
        LinearLayout scanLayout = findViewById(R.id.scan_layout);   //掃描發票orOCR按鈕區塊
        LinearLayout addManuallyLayout = findViewById(R.id.add_manually_layout);    //手動輸入區塊


        //返回
        goBackBtn.setOnClickListener(v -> {
            finish();
                }
        );

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
        // 用于存储所有QR码合并后的完整信息
        StringBuilder combinedInfo = new StringBuilder();
        // 用于存储解析出的品项列表
        List<ParsedItem> items = new ArrayList<>();
        // 用于存储发票日期
        String invoiceDate = null;

        // 分开存储不以"**"开头和以"**"开头的内容
        List<String> beforeStarStar = new ArrayList<>();
        List<String> afterStarStar = new ArrayList<>();

        // 分类存储
        for (String qrCode : qrCodes) {
            if (qrCode.startsWith("**")) {
                // 将开头的"**"替换为":"后存储
                afterStarStar.add(qrCode.replaceFirst("\\*\\*", ":"));
            } else {
                beforeStarStar.add(qrCode);
            }
        }

        // 先处理不以"**"开头的内容
        for (String qrCode : beforeStarStar) {
            combinedInfo.append(qrCode);
        }

        // 再处理以"**"开头的内容
        for (String qrCode : afterStarStar) {
            combinedInfo.append(qrCode);
        }

        // 获取合并后的完整字符串
        String combinedText = combinedInfo.toString();
        // 去除合并字符串中的所有空格
        combinedText = combinedText.replaceAll("\\s+", "");
        Log.i("QR CODE SCANNER", "合并后的完整信息: " + combinedText);


        // 解析发票信息
        ParsedInvoice parsedInvoice = parseInvoiceData(combinedText);
        invoiceDate = parsedInvoice.getDate();
        items.addAll(parsedInvoice.getItems());

        // 构建结果字符串
        if (invoiceDate != null) {
            StringBuilder parsedResults = new StringBuilder();
            parsedResults.append("Invoice Date: ").append(invoiceDate).append("\nItems:\n");
            for (ParsedItem item : items) {
                parsedResults.append("Name: ").append(item.getName())
                        .append(", Quantity: ").append(item.getQuantity())
                        .append(", Price: ").append(item.getPrice())
                        .append("\n");
            }
            // 显示结果
            Toast.makeText(this, parsedResults.toString(), Toast.LENGTH_LONG).show();
            Log.i("QR CODE SCANNER", parsedResults.toString());
        }

        // 获取数据库实例
        FridgeDatabase db = FridgeDatabase.getInstance(getApplicationContext());

        // 创建后台线程池
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 在后台线程中执行数据库操作
        String finalInvoiceDate = invoiceDate;
        executorService.execute(() -> {
            // 创建发票对象并保存
            Invoice invoice = new Invoice(finalInvoiceDate);
            long invoiceId = db.invoiceDAO().insertInvoice(invoice);

            // 保存发票项目
            for (ParsedItem item : items) {
                InvoiceItem invoiceItem = new InvoiceItem((int) invoiceId, item.getName(), item.getQuantity(), item.getPrice());
                db.invoiceItemDAO().insertInvoiceItems(Collections.singletonList(invoiceItem));
            }
        });

        executorService.shutdown(); // 记得在合适的时候关闭ExecutorService

        // 关闭活动或转到其他活动
        finish();
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
        // 返回包含发票日期和品项的对象
        return new ParsedInvoice(date, resultItems);
    }

    // 内部类：表示一个解析出的品项
    private static class ParsedItem {
        private final String name;     // 品项名称
        private final String quantity; // 数量
        private final String price;    // 价格

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

    // 内部类：表示一个解析出的发票
    private static class ParsedInvoice {
        private final String date;           // 发票日期
        private final List<ParsedItem> items; // 品项列表

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
}
