package com.example;

import com.example.api.JsonPlaceholderApi;
import com.example.config.RetrofitClient;
import com.example.data.Post;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        JsonPlaceholderApi apiService = RetrofitClient.getApiService();
        try {
            Call<List<Post>> posts = apiService.getPosts();
            Response<List<Post>> response = posts.execute();
            if (response.isSuccessful()) {
                System.out.println();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}