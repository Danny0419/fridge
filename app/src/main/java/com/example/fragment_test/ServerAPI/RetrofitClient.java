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
            // 配置 OkHttpClient
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS) // 增加连接超时
                    .readTimeout(60, TimeUnit.SECONDS) // 增加读取超时
                    .writeTimeout(60, TimeUnit.SECONDS) // 增加写入超时
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // 日志拦截器
                    .addInterceptor(new RetryInterceptor(3)) // 添加重试机制，最多重试3次
                    .build();

            // 创建 Gson 对象并设置为宽松模式
            Gson gson = new GsonBuilder()
                    .setLenient() // 宽松模式，允许解析宽松的 JSON
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson)) // 使用宽松的 Gson 解析器
                    .build();

        }
        return retrofit;
    }

    // 定义 RetryInterceptor，处理重试逻辑
    private static class RetryInterceptor implements Interceptor {
        private int maxRetries;
        private int retryCount = 0;

        public RetryInterceptor(int maxRetries) {
            this.maxRetries = maxRetries;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = null;
            boolean responseOK = false;

            // 重试逻辑
            while (retryCount < maxRetries && !responseOK) {
                try {
                    response = chain.proceed(request);
                    responseOK = response.isSuccessful(); // 如果成功，退出循环
                } catch (Exception e) {
                    retryCount++;
                    if (retryCount >= maxRetries) {
                        throw e; // 达到最大重试次数，抛出异常
                    }
                }
            }
            return response;
        }
    }
}
