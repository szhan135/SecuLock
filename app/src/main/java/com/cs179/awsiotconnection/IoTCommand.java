package com.cs179.awsiotconnection;

import com.google.gson.annotations.SerializedName;

public class IoTCommand {
    private String title;
    private String payload;
    @SerializedName("body")
    private String text;

    //public IoTCommand(String title, String text) {
    public IoTCommand(String text) {
        this.title = title;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getPayload() {
        return payload;
    }
}
