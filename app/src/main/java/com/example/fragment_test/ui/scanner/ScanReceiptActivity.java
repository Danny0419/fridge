package com.example.fragment_test.ui.scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


//import com.example.fragment_test.Manifest;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fragment_test.R;
import com.example.fragment_test.ScannerList.OcrActivity;
import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.entity.Invoice;
import com.example.fragment_test.entity.InvoiceItem;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanReceiptActivity extends AppCompatActivity {
    //相機掃描
    private DecoratedBarcodeView barcodeView;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    //QRcode集合
    private Set<String> scannedDataSet = new HashSet<>();


    // 用于存储已识别的QR码信息
    private Set<String> recognizedQrCodes = new HashSet<>();
    // 需要识别的QR码数量
    private static final int REQUIRED_QRCODES = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);

        //開啟相機
//        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
//        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
//        intentIntegrator.setPrompt("Scan a QR code");
//        intentIntegrator.setCameraId(0);  // 使用特定的摄像头
//        intentIntegrator.setBeepEnabled(true);
//        intentIntegrator.setBarcodeImageEnabled(true);
//        intentIntegrator.initiateScan();

        barcodeView = findViewById(R.id.camara);
        // 相機
        if (checkCameraPermission()) {
            //如果相機已授權，則啟動相機
            startCamera();
            initializeBarcodeScanner();
        }
        else {
            //如果相機未授權，則請求
            requestCameraPermission();
        }

        /*
        個按鈕之點擊
        */
        Button goBackBtn = findViewById(R.id.goBackBnt);  //返回按鈕
        Button scanReceiptButton = findViewById(R.id.scan_receipt_button);    //掃描發票orOCR按鈕
        Button addManuallyButton = findViewById(R.id.add_manually_button);    //手動輸入按鈕
        LinearLayout scanLayout = findViewById(R.id.scan_layout);   //掃描發票orOCR按鈕區塊
        LinearLayout addManuallyLayout = findViewById(R.id.add_manually_layout);    //手動輸入區塊
        int unclickTextColor = ContextCompat.getColor(this, R.color.hintBoxGray);
        int clickTextColor=ContextCompat.getColor(this,R.color.black);
        Drawable unclickBackground=ContextCompat.getDrawable(this,R.drawable.non_under_line);
        Drawable clickBackground=ContextCompat.getDrawable(this,R.drawable.text_under_line);

        //返回
        goBackBtn.setOnClickListener(v -> {
            finish();
                }
        );

        //掃描發票orOCR
        scanReceiptButton.setOnClickListener(v -> {
            addManuallyLayout.setVisibility(View.GONE);
            scanLayout.setVisibility(View.VISIBLE);
            scanReceiptButton.setTextColor(clickTextColor);
            scanReceiptButton.setBackground(clickBackground);
            addManuallyButton.setTextColor(unclickTextColor);
            addManuallyButton.setBackground(unclickBackground);
        });

        //手動輸入
        addManuallyButton.setOnClickListener(v -> {
            addManuallyLayout.setVisibility(View.VISIBLE);
            scanLayout.setVisibility(View.GONE);
            scanReceiptButton.setTextColor(unclickTextColor);
            scanReceiptButton.setBackground(unclickBackground);
            addManuallyButton.setTextColor(clickTextColor);
            addManuallyButton.setBackground(clickBackground);
        });

        Button albumBtn = findViewById(R.id.albumBnt);
        albumBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, OcrActivity.class);
            startActivity(intent);
        });
//        String productName ;  //"有機青江菜"
//        fetchCombinedIngredients(productName);
    }

    //開啟相機
    private void startCamera() {
        barcodeView.decodeContinuous(result -> {
            String scanContent = result.getText();
            Toast.makeText(ScanReceiptActivity.this, "Scan result: " + scanContent, Toast.LENGTH_LONG).show();
        });
    }

    //要求相機權限
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    //    確認相機權限
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    //暫停相機
    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    //    使用者拒絕開啟相機
//    @SuppressLint("MissingSuperCall")
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // 用户授予了相机权限，启动相机
//                startCamera();
//            } else {
//                // 用户拒绝了相机权限，显示提示
//                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private void initializeBarcodeScanner() {
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result != null) {
                    String scannedData = result.getText();
                    scannedDataSet.add(scannedData);

                    if (scannedDataSet.size() == 2) {
                        handleRecognizedQrCodes(scannedDataSet);
                        barcodeView.pause();
                    }
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                // 可選：處理可能的結果點
            }
        });
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
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("tag", "onActivityResult: "+"avtivity");
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
    }

    private void handleRecognizedQrCodes(Set<String> qrCodes) {

        Log.i("tag", "handleRecognizedQrCodes: "+"handleRecognizedQrCodes");
        // 合并QR码信息并解析发票和品项
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

        String combinedText = combinedInfo.toString().replaceAll("\\s+", "");
        Log.i("QR CODE SCANNER", "合并后的完整信息: " + combinedText);

        // 提取前10个字符作为发票ID
        String invoiceId = combinedText.substring(0, 10);

        // 解析发票信息
        ParsedInvoice parsedInvoice = parseInvoiceData(combinedText);
        invoiceDate = parsedInvoice.getDate();
        items.addAll(parsedInvoice.getItems());

        // 建立要保存的数据
        if (invoiceDate != null) {
            // 创建后台线程池
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            // 获取数据库实例
            FridgeDatabase db = FridgeDatabase.getInstance(getApplicationContext());

            // 在后台线程中执行插入操作
            String finalInvoiceDate = invoiceDate;
            executorService.execute(() -> {
                // 检查发票是否已存在
                Invoice existingInvoice = db.invoiceDAO().getInvoiceById(invoiceId);
                if (existingInvoice != null) {
                    // 提示用户发票已被扫描过
                    runOnUiThread(() -> {
                        Toast.makeText(this, "此發票已被掃描過，請檢查！", Toast.LENGTH_LONG).show();
                    });
                    //return; // 拦截，避免后续处理
                }

                // 插入发票数据，使用提取的前10个字符作为ID
                Invoice invoice = new Invoice(invoiceId, finalInvoiceDate);
                db.invoiceDAO().insertInvoice(invoice);

                // 插入发票品项数据
                List<InvoiceItem> invoiceItems = new ArrayList<>();
                for (ParsedItem item : items) {
                    InvoiceItem invoiceItem = new InvoiceItem(invoiceId, item.getName(), item.getQuantity(), item.getPrice());
                    invoiceItems.add(invoiceItem);
                }
                db.invoiceItemDAO().insertInvoiceItems(invoiceItems);

                // 调用 API 获取 ingredient 信息，传入数量和发票日期
                fetchCombinedIngredients(items, finalInvoiceDate);

                // 提示用户成功
                runOnUiThread(() -> {
                    Toast.makeText(this, "發票和品項已成功保存", Toast.LENGTH_LONG).show();
                    finish();
                });
            });

            executorService.shutdown(); // 在适当的时候关闭线程池
        }
    }


    private void fetchCombinedIngredients(List<ParsedItem> items, String invoiceDate) {
        // 获取数据库实例
        FridgeDatabase db = FridgeDatabase.getInstance(getApplicationContext());

        // 获取 RetrofitClient 实例
        ApiService apiService = RetrofitClient.getInstance().getApiService();

// 循环处理每个品项，调用 API 获取相关的食材信息
        for (ParsedItem item : items) {
            String productName = item.getName(); // 获取品项名称  //"有機青江菜"; item.getName();
            int quantity = item.getQuantity(); // 获取品项数量

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
                                    int date = 0;
                                    int expirationDays = 0;

                                    if (invoiceDateStr.length() == 7) {
                                        // 提取民国年份、月份和日期
                                        int rocYear = Integer.parseInt(invoiceDateStr.substring(0, 3)); // 民国年份
                                        int month = Integer.parseInt(invoiceDateStr.substring(3, 5));   // 月份
                                        int day = Integer.parseInt(invoiceDateStr.substring(5, 7));     // 日期

                                        // 将民国年份转换为西元年份（西元年份 = 民国年份 + 1911）
                                        int gregorianYear = rocYear + 1911;

                                        // 转换为 LocalDate 对象
                                        LocalDate invoiceLocalDate = LocalDate.of(gregorianYear, month, day);

                                        // 保存原始日期的西元格式
                                        String gregorianDateStr = invoiceLocalDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                                        date = Integer.parseInt(gregorianDateStr);
                                        System.out.println("转换后的西元日期：" + gregorianDateStr);

                                        // 加上保存天數來計算保存期限
                                        LocalDate expirationDate = invoiceLocalDate.plusDays(ingredient.getExpiration());
                                        expirationDays = Integer.parseInt(expirationDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

                                        System.out.println("保存期限日期：" + expirationDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                                    } else {
                                        System.out.println("无效的 invoiceDate 格式");
                                    }


// 创建格式化后的日期字符串

// 解析保存天数
                                    //int expirationDays = date + ( ingredient.getExpiration() ); // 从 ingredient 中获取保存天数


// 获取当前日期
                                    Calendar todayCalendar = Calendar.getInstance();
                                    SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy/MM/dd"); // 格式化当前日期
                                    String todayDateStr = todayFormat.format(todayCalendar.getTime());


// 创建 RefrigeratorIngredient 对象
                                    RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(
                                            0, // 假设 ID 是自动生成的
                                            ingredient.getIngredient_Name(), // 使用 ingredient 的名字
                                            quantity, // 使用 API 请求时的数量
                                            ingredient.getIngredient_pictures(), // 图片可以根据需要设置
                                            ingredient.getIngredients_category(), // 使用 ingredient 的类别
                                            date, // 使用格式化后的购买日期
                                            expirationDays, // 保存天数
                                            ingredient.getUnit()
                                    );

                                    // 存入数据库
                                    db.refrigeratorIngredientDAO().insertIngredient(refrigeratorIngredient);
                                } catch (NumberFormatException e) {
                                    Log.e("DATA ERROR", "Invalid number format: " + e.getMessage());
                                }
                            });
                        }
                        executorService.shutdown(); // 在适当的时机关闭线程池
                    } else {
//                        // 处理非预期的响应，存储缺失的食材信息
//                        Log.i("API RESPONSE", "NXo matching ingredients found for: " + productName);
//                        ExecutorService executorService = Executors.newSingleThreadExecutor();
//                        executorService.execute(() -> {
//                            // 处理 invoiceDate 从民国转换为西元日期
//                            String invoiceDateStr = invoiceDate; // 假设 invoiceDate 是字符串类型，例如 "1131225"
//                            int date = Integer.parseInt(invoiceDateStr);
//
//                            // 创建一个默认的 RefrigeratorIngredient 对象，未知字段设置为 null
//                            RefrigeratorIngredient missingIngredient = new RefrigeratorIngredient(
//                                    0, // 假设 ID 是自动生成的
//                                    productName, // 使用查询的产品名
//                                    quantity, // 数量设为 0
//                                    productName+"的圖片", // 图片设为 null
//                                    "其他", // 类别设为 null
//                                    date, // 购买日期设为 null
//                                    null, // 到期日期设为 null
//                                    "克"
//                            );
//
//                            // 存入数据库
//                            db.refrigeratorIngredientDAO().insertIngredient(missingIngredient);//不把其他食材存入，就註解掉這行
//                        });
//                        executorService.shutdown(); // 在适当的时机关闭线程池
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
                    resultItems.add(new ParsedItem(itemName, Integer.parseInt(itemQuantity), Double.parseDouble(itemPrice)));
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
        private int quantity;
        private double price;

        public ParsedItem(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }
}

