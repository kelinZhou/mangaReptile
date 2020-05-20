package com.neuifo.domain.model.base;

import com.google.gson.annotations.SerializedName;

public class HttpResult<T> {

    @SerializedName("code")
    public String code;
    @SerializedName("msg")
    public String message;

    @SerializedName(value = "result", alternate = {"body", "data"})
    public T data;

    public boolean isEmpty() {
        return data == null;
    }
}
