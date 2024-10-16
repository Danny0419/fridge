package com.example.fragment_test.ServerAPI;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://120.125.83.32:5000/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // 配置日誌攔截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 配置重試攔截器
            RetryInterceptor retryInterceptor = new RetryInterceptor(3);

            // 配置 OkHttpClient
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS) // 增加連接超時
                    .readTimeout(60, TimeUnit.SECONDS)    // 增加讀取超時
                    .writeTimeout(60, TimeUnit.SECONDS)   // 增加寫入超時
                    .addInterceptor(loggingInterceptor)    // 添加日誌攔截器
                    .addInterceptor(retryInterceptor)      // 添加重試攔截器
                    .build();

            // 配置 Gson
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // 建立 Retrofit 實例
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson)) // 使用寬鬆的 Gson 解析器
                    .build();
        }
        return retrofit;
    }

    // 重試攔截器
    private static class RetryInterceptor implements Interceptor {
        private final int maxRetries;

        public RetryInterceptor(int maxRetries) {
            this.maxRetries = maxRetries;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            int retryCount = 0;
            Response response = null;
            IOException exception = null;

            while (retryCount < maxRetries) {
                try {
                    response = chain.proceed(request);
                    if (response.isSuccessful()) {
                        return response;
                    } else {
                        // 根據需要添加特定的 HTTP 狀態碼重試邏輯
                        // 例如，只在 5xx 錯誤時重試
                        if (response.code() < 500) {
                            // 非服務器錯誤，避免重試
                            return response;
                        }
                    }
                } catch (IOException e) {
                    exception = e;
                }

                // 如果回應不成功且還有重試機會，關閉回應並重試
                if (response != null) {
                    response.close();
                }

                retryCount++;
            }

            // 如果所有重試均失敗，則拋出最後一個異常或返回最後一個響應
            if (response != null) {
                return response;
            } else {
                throw exception != null ? exception : new IOException("Unknown network error");
            }
        }
    }
}
