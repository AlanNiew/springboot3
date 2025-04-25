package com.example.config;

import okhttp3.*;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class StandardLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = StandardCharsets.UTF_8;
    
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        
        // 1. 打印请求信息
        logRequest(request);
        
        // 2. 记录请求开始时间
        long startNs = System.nanoTime();
        
        // 3. 执行请求
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logError(request, e);
            throw e;
        }
        
        // 4. 计算请求耗时
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        
        // 5. 打印响应信息
        return logResponse(response, tookMs);
    }

    private void logRequest(Request request) throws IOException {
        try {
            String requestStartMessage = "--> " + request.method() + " " + request.url();
            System.out.println(requestStartMessage);

            // 打印请求头
            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    System.out.println(name + ": " + headers.value(i));
                }
            }

            RequestBody requestBody = request.body();
            if (requestBody != null) {
                System.out.println("Content-Type: " + requestBody.contentType());
                System.out.println("Content-Length: " + requestBody.contentLength());
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                // 对于小请求体可以直接读取
                if (buffer.size() < 1024 * 1024) { // 小于1MB
                    Charset charset = UTF8;
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }

                    if (isPlaintext(buffer.readByteArray())) {
                        System.out.println("\n" + buffer.readString(charset));
                        System.out.println("--> END " + request.method());
                    } else {
                        System.out.println("\n--> END " + request.method() + " (binary " + buffer.size() + "-byte body omitted)");
                    }
                } else {
                    System.out.println("\n--> END " + request.method() + " (large " + buffer.size() + "-byte body omitted)");
                }
            } else {
                System.out.println("--> END " + request.method());
            }
        } catch (Exception e) {
            System.err.println("Failed to log request: " + e.getMessage());
        }
    }

    private Response logResponse(Response originalResponse, long tookMs) throws IOException {
        try {
            // 克隆响应以便多次读取body
            Response response = originalResponse.newBuilder()
                .body(ResponseBody.create(
                    Objects.requireNonNull(originalResponse.body()).contentType(),
                    Objects.requireNonNull(originalResponse.body()).bytes()
                ))
                .build();

            System.out.println("<-- " + response.code() + " " + response.message() + " " +
                             response.request().url() + " (" + tookMs + "ms)");

            // 打印响应头
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                System.out.println(headers.name(i) + ": " + headers.value(i));
            }

            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                byte[] bytes = responseBody.bytes();

                if (isPlaintext(bytes)) {
                    System.out.println("\n" + new String(bytes, UTF8));
                    System.out.println("<-- END HTTP (" + bytes.length + "-byte body)");
                } else {
                    System.out.println("<-- END HTTP (binary " + bytes.length + "-byte body omitted)");
                }

                // 重建响应体
                return response.newBuilder()
                    .body(ResponseBody.create(bytes, responseBody.contentType()))
                    .build();
            } else {
                System.out.println("<-- END HTTP (no body)");
                return response;
            }
        } catch (Exception e) {
            System.err.println("Failed to log response: " + e.getMessage());
            return originalResponse;
        }
    }

    private void logError(Request request, Exception e) {
        System.err.println("<-- HTTP FAILED: " + e);
        System.err.println("Request: " + request.method() + " " + request.url());
    }

    /**
     * 判断是否是文本内容（可打印）
     */
    private static boolean isPlaintext(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return false;
        
        try {
            int length = Math.min(bytes.length, 64);
            for (int i = 0; i < length; i++) {
                byte b = bytes[i];
                if (b < 0x20 && b != 0x09 && b != 0x0A && b != 0x0D) {
                    return false;
                }
                if (b > 0x7E) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}