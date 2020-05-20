package com.neuifo.data.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.neuifo.data.domain.utils.ClassConstructorLoader.getAppropriateConstructor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.lang.reflect.Constructor
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 * Created by neuifo on 16/7/25.
 * 处理响应结果
 */
class ResponseConverterFactory<T : Converter<*, *>> private constructor(
    private val gson: Gson,
    private val clazz: Class<T>
) : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, T>? {
        val adapter =
            gson.getAdapter(TypeToken.get(type))
        val objArr = arrayOf<Any?>(gson, adapter, type)
        try {
            val constructor: Constructor<T> =
                getAppropriateConstructor<T>(clazz, objArr)
            constructor.isAccessible = true
            return constructor.newInstance(*objArr) as Converter<ResponseBody, T>
        } catch (e: Exception) {
        }
        return null
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val adapter =
            gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyConverter(
            gson,
            adapter
        )
    }

    internal class GsonRequestBodyConverter<T>(
        private val gson: Gson,
        private val adapter: TypeAdapter<T>
    ) : Converter<T, RequestBody> {
        @Throws(IOException::class)
        override fun convert(value: T): RequestBody {
            val buffer = Buffer()
            val writer: Writer = OutputStreamWriter(
                buffer.outputStream(),
                UTF_8
            )
            val jsonWriter = gson.newJsonWriter(writer)
            adapter.write(jsonWriter, value)
            jsonWriter.close()
            return RequestBody.create(
                MEDIA_TYPE,
                buffer.readByteString()
            )
        }

        companion object {
            private val MEDIA_TYPE =
                MediaType.parse("application/json; charset=UTF-8")
            private val UTF_8 = Charset.forName("UTF-8")
        }

    }

    companion object {
        @JvmStatic
        fun create(
            gson: Gson,
            tClass: Class<out Converter<*,*>>
        ): ResponseConverterFactory<*> {
            return ResponseConverterFactory(gson, tClass)
        }
    }

}