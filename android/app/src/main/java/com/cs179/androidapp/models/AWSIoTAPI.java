package com.cs179.androidapp.models;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AWSIoTAPI {
    // Can use full URL to replace "partial url" and it will overwrite the base url

    // This is the one
    @Headers({"Content-Type:application/json"})
    @POST("Test/publish-to-pi-3/")
    Call<IoTAPICommand> createCommand(@Body Map<String, String> fields);

    // Not used
    @Headers({"Content-Type:application/json"})
    @POST("Test/publish-to-pi-3/")
    Call<IoTAPICommand> createCommand(@Body IoTAPICommand iotCommand);
}
