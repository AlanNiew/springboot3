package com.example.config;

import com.example.api.JsonPlaceholderApi;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static final Retrofit retrofit;

    static {
        // 配置 OkHttp

        // 连接池
        ConnectionPool connectionPool = new ConnectionPool(5, 5, TimeUnit.MINUTES);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RetryInterceptor())
                .connectionPool(connectionPool)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        // 配置 Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public static JsonPlaceholderApi getApiService() {
        return retrofit.create(JsonPlaceholderApi.class);
    }

}
