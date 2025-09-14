package com.example.config;

import okhttp3.*;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Objects;

public class EncryptionInterceptor implements Interceptor {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private final Key secretKey;

    public EncryptionInterceptor(String encryptionKey) {
        // 使用Base64解码密钥
        byte[] decodedKey = Base64.getDecoder().decode(encryptionKey);
        this.secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        // 1. 加密请求
        Request encryptedRequest = encryptRequest(chain.request());
        
        // 2. 执行请求
        Response response = chain.proceed(encryptedRequest);
        
        // 3. 解密响应
        return decryptResponse(response);
    }

    private Request encryptRequest(Request originalRequest) throws IOException {
        try {
            // 只处理有body的请求
            if (originalRequest.body() == null) {
                return originalRequest;
            }

            // 读取原始请求体
            Buffer buffer = new Buffer();
            Objects.requireNonNull(originalRequest.body()).writeTo(buffer);
            String requestBody = buffer.readUtf8();

            // 加密请求体
            String encryptedBody = encrypt(requestBody);

            // 创建新的请求体
            RequestBody newBody = RequestBody.create(
                encryptedBody, 
                MediaType.parse("text/plain")
            );

            // 返回新请求
            return originalRequest.newBuilder()
                .header("Content-Encoding", "aes") // 添加加密标记头
                .method(originalRequest.method(), newBody)
                .build();
        } catch (Exception e) {
            throw new IOException("Failed to encrypt request", e);
        }
    }

    private Response decryptResponse(Response originalResponse) throws IOException {
        try {
            // 检查响应是否加密
            String encoding = originalResponse.header("Content-Encoding");
            if (!"aes".equalsIgnoreCase(encoding) || originalResponse.body() == null) {
                return originalResponse;
            }

            // 读取加密响应体
            ResponseBody responseBody = originalResponse.body();
            String encryptedResponse = responseBody.string();

            // 解密响应体
            String decryptedBody = decrypt(encryptedResponse);

            // 重建响应体
            MediaType contentType = responseBody.contentType();
            ResponseBody newResponseBody = ResponseBody.create(
                decryptedBody, 
                contentType
            );

            // 返回新响应
            return originalResponse.newBuilder()
                .removeHeader("Content-Encoding")
                .body(newResponseBody)
                .build();
        } catch (Exception e) {
            throw new IOException("Failed to decrypt response", e);
        }
    }

    private String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private String decrypt(String encryptedData) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}