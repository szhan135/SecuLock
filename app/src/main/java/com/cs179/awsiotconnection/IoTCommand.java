package com.cs179.awsiotconnection;

import com.google.gson.annotations.SerializedName;

public class IoTCommand {

    @SerializedName("body")
    private String text;

    //public IoTCommand(String title, String text) {
    public IoTCommand(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
