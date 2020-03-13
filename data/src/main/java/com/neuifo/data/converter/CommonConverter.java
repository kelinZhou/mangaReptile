package com.neuifo.data.converter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by neuifo on 2017/5/23.
 */

public class CommonConverter<T> implements Converter<ResponseBody, T> {

    private static final String TAG = "ResponseConverter";

    private final Gson gson;
    private final Type type;
    private TypeAdapter<T> adapter;

    CommonConverter(Gson gson, TypeAdapter<T> adapter, Type type) {
        this.gson = gson;
        this.adapter = adapter;
        this.type = type;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        T data = gson.fromJson(response, type);
        /*
         * 此处暂时只抛出异常，toast提示放到具体请求回调中
         */
        if (data == null) {
            throw new JsonSyntaxException("数据解析错误");
        }


        return data;
    }

}
