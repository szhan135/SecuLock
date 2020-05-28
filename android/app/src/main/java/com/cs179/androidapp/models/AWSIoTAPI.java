package com.cs179.androidapp.models;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AWSIoTAPI {
    // Can use full URL to replace "partial url" and it will overwrite the base url

    // This is the one
    @POST("Test/publish-to-pi-3/")
    Call<IoTAPICommand> createCommand(@Header ("Content-Type") String header1,
                                      @Header ("Authorization") String header2,
                                      @Body Map<String, String> fields);

    // Not used
    @Headers({"Content-Type:application/json"})
    @POST("Test/publish-to-pi-3/")
    Call<IoTAPICommand> createCommand(@Body IoTAPICommand iotCommand);
}
