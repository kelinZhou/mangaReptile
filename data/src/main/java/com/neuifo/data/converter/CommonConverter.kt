package com.neuifo.data.converter

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by neuifo on 2017/5/23.
 */
class CommonConverter<T> internal constructor(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>,
    private val type: Type
) : Converter<ResponseBody, T> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val response = value.string()
        /*
         * 此处暂时只抛出异常，toast提示放到具体请求回调中
         */
        return gson.fromJson(response, type) ?: throw JsonSyntaxException(response)
    }

}