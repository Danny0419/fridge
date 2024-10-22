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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        String productName = "有機青江菜";
        fetchCombinedIngredients(productName);
    }

    private void fetchCombinedIngredients(String productName) {
        // 使用 RetrofitClient 發送請求
        RetrofitClient.getInstance().getApiService().getCombinedIngredients(productName).enqueue(new Callback<List<CombinedIngredient>>() {
            @Override
            public void onResponse(Call<List<CombinedIngredient>> call, Response<List<CombinedIngredient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 成功接收到 API 回應
                    List<CombinedIngredient> ingredients = response.body();

                    // 顯示詳細信息
                    StringBuilder message = new StringBuilder("Found: " + ingredients.size() + " ingredients\n");
                    for (CombinedIngredient ingredient : ingredients) {
                        message.append("ID: ").append(ingredient.getSupermarket_ingredient_ID()).append(", ")
                                .append("Name: ").append(ingredient.getIngredient_Name()).append(", ") //新名字
                                .append("Category: ").append(ingredient.getIngredients_category()).append(", ") //種類
                                .append("Unit: ").append(ingredient.getUnit()).append(", ")
                                .append("Grams: ").append(ingredient.getGrams()).append(", ")
                                .append("Expiration: ").append(ingredient.getExpiration()).append("\n"); //有效期限
                    }

                    Toast.makeText(ScanReceiptActivity.this, message.toString(), Toast.LENGTH_LONG).show();
                } else {
                    // 處理非預期的回應
                    Toast.makeText(ScanReceiptActivity.this, "No matching ingredients found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CombinedIngredient>> call, Throwable t) {
                // 請求失敗處理
                Toast.makeText(ScanReceiptActivity.this, "請求失敗: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
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

                // 調用 API 獲取 ingredient 資訊，傳入數量和發票日期
                fetchCombinedIngredients(items, finalInvoiceDate);

                // 提示用戶成功
                runOnUiThread(() -> {
                    Toast.makeText(this, "發票和品項已成功保存", Toast.LENGTH_LONG).show();
                    finish();
                });
            });

            executorService.shutdown(); // 在適當的時候關閉線程池
        }
    }

    private void fetchCombinedIngredients(List<ParsedItem> items, String invoiceDate) {
        // 获取数据库实例
        FridgeDatabase db = FridgeDatabase.getInstance(getApplicationContext());

        // 获取 RetrofitClient 实例
        ApiService apiService = RetrofitClient.getInstance().getApiService();

// 循环处理每个品项，调用 API 获取相关的食材信息
        for (ParsedItem item : items) {
            String productName = "有機青江菜";//item.getName(); // 获取品项名称
            int quantity = Integer.parseInt(item.getQuantity()); // 获取品项数量

            // 使用 RetrofitClient 发送请求
            apiService.getCombinedIngredients(productName).enqueue(new Callback<List<CombinedIngredient>>() {
                @Override
                public void onResponse(Call<List<CombinedIngredient>> call, Response<List<CombinedIngredient>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // 成功接收到 API 回应
                        List<CombinedIngredient> ingredients = response.body();

                        // 获取到期日期
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        for (CombinedIngredient ingredient : ingredients) {
                            executorService.execute(() -> {
                                try {
                                    // 将获取的资讯存入 RefrigeratorIngredient
                                    // 处理 invoiceDate 从民国转换为西元日期
                                    String invoiceDateStr = invoiceDate; // 假设 invoiceDate 是字符串类型，例如 "1131225"
                                    int year = Integer.parseInt(invoiceDateStr.substring(0, 3)) + 1911; // 提取并转换年份
                                    int month = Integer.parseInt(invoiceDateStr.substring(3, 5)); // 提取月份
                                    int day = Integer.parseInt(invoiceDateStr.substring(5, 7)); // 提取日期

                                    // 创建格式化后的日期字符串
                                    String formattedDate = String.format("%04d/%02d/%02d", year, month, day);

                                    RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(
                                            0, // 假设 ID 是自动生成的
                                            ingredient.getIngredient_Name(), // 使用 ingredient 的名字
                                            quantity, // 使用 API 请求时的数量
                                            "食材的图片", // 图片可以根据需要设置
                                            ingredient.getIngredients_category(), // 使用 ingredient 的类别
                                            formattedDate, // 使用格式化后的购买日期
                                            Integer.parseInt(ingredient.getExpiration()) // 到期日期
                                    );

                                    // 存入数据库
                                    db.refrigeratorIngredientDAO().insertRefrigeratorIngredient(refrigeratorIngredient);
                                } catch (NumberFormatException e) {
                                    Log.e("DATA ERROR", "Invalid number format: " + e.getMessage());
                                }
                            });
                        }
                        executorService.shutdown(); // 在适当的时机关闭线程池
                    } else {
                        // 处理非预期的响应
                        Log.i("API RESPONSE", "No matching ingredients found for: " + productName);
                    }
                }

                @Override
                public void onFailure(Call<List<CombinedIngredient>> call, Throwable t) {
                    // 请求失败处理
                    Log.e("API ERROR", "Request failed: " + t.getMessage());
                }
            });
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
