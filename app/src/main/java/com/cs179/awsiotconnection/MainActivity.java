package com.cs179.awsiotconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
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

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://b0fzda82dg.execute-api.us-west-2.amazonaws.com/") // Retrofit forces a / at the end
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        awsIotApi = retrofit.create(AWSIoTAPI.class);

        //getPosts();
        //getComments();
        //createPost();
        //getCases();
        //createIotCommand();
    }

    public void lock(View view){
        createIotCommand("Lock");
    }

    public void unlock(View view){
        createIotCommand("Unlock");
    }

    public void GG(View view){
        createIotCommand("GG");
    }

    private void getPosts(){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId","1");
        parameters.put("_sort","id");
        parameters.put("_order","desc");

        Call<List<Post>> call = awsIotApi.getPosts(parameters);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<Post> posts = response.body();
                for (Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";
                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getComments(){
        Call<List<Comment>> call = awsIotApi.getComments("posts/3/comments");

        call.enqueue((new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()){
                    textViewResult.setText("Code" + response.code());
                    return;
                }
                List<Comment> comments = response.body();
                for (Comment comment : comments) {
                    String content = "";
                    content += "ID: " + comment.getId() + "\n";
                    content += "Post ID: " + comment.getPostId() + "\n";
                    content += "Name: " + comment.getName() + "\n";
                    content += "Email: " + comment.getEmail() + "\n";
                    content += "Text: " + comment.getText() + "\n\n";
                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        }));
    }

    private void createPost(){
        Post post = new Post(23, "New Title", "New Text");
        Map<String, String> fields = new HashMap<>();
        fields.put("text", "Test");
        Call<Post> call = awsIotApi.createPost(fields);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "Post ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";
                textViewResult.setText(content);
            }


            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getCases(){
        Call<List<Case>> call = awsIotApi.getCases("countries");

        call.enqueue((new Callback<List<Case>>() {
            @Override
            public void onResponse(Call<List<Case>> call, Response<List<Case>> response) {
                if (!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Case> cases = response.body();
                for (Case _case: cases){
                    String content = "";
                    content += "Country: " + _case.getCountry() + "\n";
                    content += "Country Code: " + _case.getCountryCode() + "\n";
                    content += "Date: " + _case.getDate() + "\n";
                    content += "Slug: " + _case.getSlug() + "\n";
                    content += "New Deaths: " + _case.getNewDeaths() + "\n";
                    content += "New Confirmed: " + _case.getNewConfirmed() + "\n";
                    content += "New Recovered: " + _case.getNewRecovered() + "\n";
                    content += "Total Confirmed: " + _case.getTotalConfirmed() + "\n";
                    content += "Total Deaths: " + _case.getTotalDeaths() + "\n";
                    content += "Total Recovered: " + _case.getTotalRecovered() + "\n\n";
                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Case>> call, Throwable t) {

            }
        }));
    }

    private void createIotCommand(String command){
        Map<String, String> fields = new HashMap<>();
        fields.put("text", command);
        Call<IoTCommand> call = awsIotApi.createCommand(fields);
        call.enqueue(new Callback<IoTCommand>() {
            @Override
            public void onResponse(Call<IoTCommand> call, Response<IoTCommand> response) {
                if (!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                IoTCommand commandResponse = response.body();
                //String content = "";
                //content += "Status Code: " + response.code() + "\n";
                //content += "Text: " + commandResponse.getText() + "\n";
                String content = getResponseText(commandResponse.getText());
                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<IoTCommand> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private String getResponseText(String s){
        Map<String, String> myMap = new HashMap<String, String>();
        //String s = "{\"topic\":\"topic_1\",\"payload\":\"GG\",\"qos\":0}";
        String[] pairs = s.split(",");

        String[] payload = pairs[1].split(":");
        String text = payload[1].replace("\"","");
        return text;
    }
}
