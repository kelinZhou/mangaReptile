package com.neuifo.data.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.neuifo.data.converter.ResponseConverterFactory.Companion.create
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitServiceFactory {
    fun <T> createRetorfitService(
        clazz: Class<T>?,
        url: String?,
        converter: Class<out Converter<*, *>>? = null,
        vararg ints: Interceptor?
    ): T {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.readTimeout(15, TimeUnit.SECONDS)
        builder.retryOnConnectionFailure(true)

        //builder.sslSocketFactory()
        /*builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return false;
            }
        });*/

        //HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        //logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //builder.addNetworkInterceptor(logInterceptor);

        //builder.addInterceptor(new HeadTokenInterceptor());
        //builder.addNetworkInterceptor(new StethoInterceptor()); // 添加抓包工具(stetho)
        ints.mapNotNull {
            builder.addInterceptor(it)
        }

        val client = builder.build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .apply {
                //暂时是为了专门处理date，以后可以单独添加各种指定的model
                if (converter != null) {
                    addConverterFactory(ScalarsConverterFactory.create())
                    addConverterFactory(
                        create(
                            createDefaultGson(),
                            converter
                        )
                    )
                }
            }
            .baseUrl(url)
            .build()
        return retrofit.create(clazz)
    }


    public fun <T> createDefault(clazz: Class<T>?, baseUrl: String): T {
        return Retrofit.Builder().client(
            OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build()
        )
            .baseUrl(baseUrl).build().create(clazz)
    }


    private fun createDefaultGson(): Gson {
        return GsonBuilder() //.registerTypeAdapter(Date.class, new DateTypeDeserializer())
            //.registerTypeAdapter(EducationDegreeEnum.class,new EnumDeserializers.EducationEnumDeserializers())
            //.registerTypeAdapter(JobSearchStatusEnum.class,new EnumDeserializers.JobSearchStatusDeserializers())
            //.registerTypeAdapter()
            .create()
    }
}