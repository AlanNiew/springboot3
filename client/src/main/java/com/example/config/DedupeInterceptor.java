package com.example.config;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DedupeInterceptor implements Interceptor {
    private final Set<String> inFlight = Collections.synchronizedSet(new HashSet<>());
    
    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        String key = chain.request().url().toString();
        if (inFlight.contains(key)) {
            throw new IOException("Duplicate request");
        }
        inFlight.add(key);
        try {
            return chain.proceed(chain.request());
        } finally {
            inFlight.remove(key);
        }
    }
}