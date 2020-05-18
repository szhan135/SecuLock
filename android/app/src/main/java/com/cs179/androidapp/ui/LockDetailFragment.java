/*
Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy of this
software and associated documentation files (the "Software"), to deal in the Software
without restriction, including without limitation the rights to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cs179.androidapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs179.androidapp.LocksApp;
import com.cs179.androidapp.R;
import com.cs179.androidapp.models.AWSIoTAPI;
import com.cs179.androidapp.models.IoTAPICommand;
import com.cs179.androidapp.models.Lock;
import com.cs179.androidapp.viewmodels.LockDetailViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LockDetailFragment extends Fragment {
    LockDetailViewModel viewModel;
    String lockId;
    private TextView textViewResult;
    private AWSIoTAPI awsIotApi;
    private Button button_lock;
    private Button button_unlock;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String lockId = arguments.getString(LocksApp.ITEM_ID);
            this.lockId = (lockId.equals("new")) ? null : lockId;
        }

        // Set up Retrofit to handle HTTP requests
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lock_detail, container, false);

        final EditText titleField = view.findViewById(R.id.edit_title);
        titleField.setEnabled(false);   // Disable the field by default

        final EditText contentField = view.findViewById(R.id.edit_content);
        contentField.setEnabled(false);

        viewModel = ViewModelProviders.of(this).get(LockDetailViewModel.class);
        // Observe the view model values.  Once we receive the value, enable the field.
        viewModel.getTitle().observe(this, (String title) -> {
            titleField.setText(title);
            titleField.setEnabled(true);
        });
        viewModel.getContent().observe(this, (String content) -> {
            contentField.setText(content);
            contentField.setEnabled(true);
        });

        // If this is a new lock, create the lock, then enable the fields.  Otherwise just load the fields
        // - the fields are received via observables
        if (lockId == null) {
            viewModel.create("", "", (Lock result) -> {
                titleField.setEnabled(true);
                contentField.setEnabled(true);
            });
        } else {
            viewModel.setLockId(lockId);
        }

        TextWatcher saveHandler = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String title = titleField.getText().toString();
                String content = contentField.getText().toString();
                viewModel.update(title, content);
            }
        };

        titleField.addTextChangedListener(saveHandler);
        contentField.addTextChangedListener(saveHandler);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textViewResult = getView().findViewById(R.id.text_view_result);
        button_lock = getView().findViewById(R.id.button_lock);
        button_lock.setOnClickListener((View v) -> createIotCommand("Lock"));
        button_unlock = getView().findViewById(R.id.button_unlock);
        button_unlock.setOnClickListener((View v) -> createIotCommand("Unlock"));
    }

    /* Apparently android:onClick="startService" can't be called in fragment. Use
       OnClickListener instead to handle this type of events (See above).
    // Lock button onClick function
    public void lock(View view) {
        createIotCommand("Lock");
    }

    // Unlock button onClick function
    public void unlock(View view) {
        createIotCommand("Unlock");
    }
    */
    // Send POST request to AWS IoT
    public void createIotCommand(String command) {
        Map<String, String> fields = new HashMap<>();
        fields.put("text", command);
        fields.put("user", "testUser"); // Mock user username, mock data in AWS Lambda index.js
        fields.put("password", "testPassword"); // Mock user password
        Call<IoTAPICommand> call = awsIotApi.createCommand(fields);
        // Asynchronously send the request and notify callback of its response
        // to avoid NetworkOnMainThreadException
        call.enqueue(new Callback<IoTAPICommand>() {
            @Override
            public void onResponse(Call<IoTAPICommand> call, Response<IoTAPICommand> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                IoTAPICommand commandResponse = response.body();
                String content = "";
                //content += "Status Code: " + response.code() + "\n";
                //content += "Text: " + commandResponse.getText() + "\n";
                content += getResponseText(commandResponse.getText());
                textViewResult.setText(content);
            }

            // Handle failure
            @Override
            public void onFailure(Call<IoTAPICommand> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    // Get the payload content from POST request response
    public String getResponseText(String s) {
        Map<String, String> myMap = new HashMap<String, String>();
        //String s = "{\"topic\":\"topic_1\",\"payload\":\"text\",\"qos\":0}";
        String[] pairs = s.split(",");
        String[] payload = pairs[1].split(":");
        String text = payload[1].replace("\"", "");
        return text;
    }
}
