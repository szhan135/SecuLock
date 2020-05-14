package com.cs179.androidapp.models;

import com.google.gson.annotations.SerializedName;

public class IoTAPICommand {

    @SerializedName("body")
    private String text;

    public IoTAPICommand(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
