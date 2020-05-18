package com.cs179.awsiotconnection;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Headers;
import retrofit2.http.POST;


// Interface to handle API requests
public interface AWSIoTAPI {
    // Can use full URL to replace "partial url" and it will overwrite the base url

    // Not used
    @Headers({"Content-Type:application/json"})
    @POST("Test/publish-to-pi-3/")
    Call<IoTCommand> createCommand(@Body IoTCommand iotCommand);

    // This is the one
    @Headers({"Content-Type:application/json"})
    @POST("Test/publish-to-pi-3/")
    Call<IoTCommand> createCommand(@Body Map<String, String> fields);
}
