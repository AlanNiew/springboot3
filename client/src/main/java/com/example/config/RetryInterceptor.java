package com.example.config;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class RetryInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        int tryCount = 0;
        while (response == null) {
            try {
                response = chain.proceed(request);
            } catch (IOException e) {
                tryCount++;
                if (tryCount >= 3) throw e;
            }
        }
        return response;
    }
}