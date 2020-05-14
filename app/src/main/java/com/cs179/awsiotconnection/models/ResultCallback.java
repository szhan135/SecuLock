package com.cs179.awsiotconnection.models;

public interface ResultCallback<T> {
    void onResult(T result);
}
