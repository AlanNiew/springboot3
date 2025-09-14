package com.example.api;

import com.example.data.Post;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * @Author: Niu
 * @Date: 2025/3/27 16:41
 * @Description:
 */
public interface JsonPlaceholderApi {
    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("posts/{id}")
    Call<Post> getPostById(@Path("id") Long id);

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    @PUT("posts/{id}")
    Call<Post> updatePost(@Path("id") Long id, @Body Post post);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") Long id);
}
