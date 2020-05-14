package com.cs179.awsiotconnection;

//import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private AWSIoTAPI awsIotApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        // Handle timeout
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        // Retrofit forces a / at the end of the base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://b0fzda82dg.execute-api.us-west-2.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        awsIotApi = retrofit.create(AWSIoTAPI.class);
    }

    // Lock button onClick function
    public void lock(View view){
        createIotCommand("Lock");
    }

    // Unlock button onClick function
    public void unlock(View view){
        createIotCommand("Unlock");
    }

    // Practice button
    public void GG(View view){
        createIotCommand("GG");
    }

    // Send POST request to AWS IoT
    private void createIotCommand(String command){
        Map<String, String> fields = new HashMap<>();
        fields.put("text", command);
        fields.put("user", "testUser"); // Mock user username, mock data in AWS Lambda index.js
        fields.put("password", "testPassword"); // Mock user password
        Call<IoTCommand> call = awsIotApi.createCommand(fields);
        // Asynchronously send the request and notify callback of its response
        // to avoid NetworkOnMainThreadException
        call.enqueue(new Callback<IoTCommand>() {
            @Override
            public void onResponse(Call<IoTCommand> call, Response<IoTCommand> response) {
                if (!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                IoTCommand commandResponse = response.body();
                String content = "";
                //content += "Status Code: " + response.code() + "\n";
                //content += "Text: " + commandResponse.getText() + "\n";
                content += getResponseText(commandResponse.getText());
                textViewResult.setText(content);
            }

            // Handle failure
            @Override
            public void onFailure(Call<IoTCommand> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    // Get the payload content from POST request response
    private String getResponseText(String s){
        Map<String, String> myMap = new HashMap<String, String>();
        //String s = "{\"topic\":\"topic_1\",\"payload\":\"text\",\"qos\":0}";
        String[] pairs = s.split(",");
        String[] payload = pairs[1].split(":");
        String text = payload[1].replace("\"","");
        return text;
    }
}
