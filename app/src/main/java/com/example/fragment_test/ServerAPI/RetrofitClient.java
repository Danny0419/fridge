package com.example.fragment_test.ServerAPI;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://120.125.83.32:5000/";
    private static final int CACHE_SIZE = 10 * 1024 * 1024; // 10 MB

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    retrofit = buildRetrofit();
                }
            }
        }
        return retrofit;
    }

    private static Retrofit buildRetrofit() {
        // 配置 OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new RetryInterceptor(3))
                .addInterceptor(new ErrorLoggingInterceptor()) // 新增錯誤日誌攔截器
                .addNetworkInterceptor(new CacheInterceptor())
                .build();

        // 創建 Gson 對象並設置為寬鬆模式
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    // 改進的 RetryInterceptor
    private static class RetryInterceptor implements Interceptor {
        private final int maxRetries;

        public RetryInterceptor(int maxRetries) {
            this.maxRetries = maxRetries;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            IOException exception = null;

            for (int retryCount = 0; retryCount <= maxRetries; retryCount++) {
                try {
                    Response response = chain.proceed(request);
                    if (response.isSuccessful()) {
                        return response;
                    } else if (retryCount == maxRetries) {
                        return response; // 返回最後一次的錯誤響應
                    }

                    // 關閉之前的響應
                    response.close();

                } catch (IOException e) {
                    exception = e;
                    Log.w(TAG, "Retry attempt " + retryCount + " failed", e);

                    if (retryCount == maxRetries) {
                        throw exception;
                    }

                    // 在重試之前等待一段時間
                    try {
                        Thread.sleep(1000L * (retryCount + 1));
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Retry interrupted", ie);
                    }
                }
            }

            throw new IOException("Unexpected retry loop exit");
        }
    }

    // 新增的錯誤日誌攔截器
    private static class ErrorLoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.nanoTime();

            try {
                Response response = chain.proceed(request);
                long endTime = System.nanoTime();
                double duration = (endTime - startTime) / 1e6; // 轉換為毫秒

                if (!response.isSuccessful()) {
                    Log.e(TAG, String.format("HTTP %d Error for %s (%.1fms)%nBody: %s",
                            response.code(),
                            request.url(),
                            duration,
                            response.peekBody(4096).string()));
                }

                return response;
            } catch (IOException e) {
                Log.e(TAG, String.format("Network Error for %s: %s",
                        request.url(),
                        e.getMessage()), e);
                throw e;
            }
        }
    }

    // 改進的緩存攔截器
    private static class CacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            // 設置緩存控制
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=300")  // 在線緩存 5 分鐘
                    .header("Cache-Control", "public, only-if-cached, max-stale=604800")  // 離線緩存 7 天
                    .removeHeader("Pragma")  // 移除可能影響緩存的舊頭
                    .build();
        }
    }

    // 清除 Retrofit 實例的方法，用於需要重置連接時
    public static void clearInstance() {
        retrofit = null;
    }
}