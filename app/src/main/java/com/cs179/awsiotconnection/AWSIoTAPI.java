package com.cs179.awsiotconnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface AWSIoTAPI {
    @GET("posts")
    Call<List<Post>> getPosts();
}
