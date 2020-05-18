package com.neuifo.data.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neuifo.data.converter.ResponseConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitServiceFactory {

    public static <T> T createRetorfitService(Class<T> clazz, String url, Class converter) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);

        builder.retryOnConnectionFailure(true);

        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(ResponseConverterFactory.create(createDefaultGson(), converter))//暂时是为了专门处理date，以后可以单独添加各种指定的model
                .baseUrl(url)
                .build();
        return retrofit.create(clazz);
    }

    private static Gson createDefaultGson() {
        return new GsonBuilder()
                //.registerTypeAdapter(Date.class, new DateTypeDeserializer())
                //.registerTypeAdapter(EducationDegreeEnum.class,new EnumDeserializers.EducationEnumDeserializers())
                //.registerTypeAdapter(JobSearchStatusEnum.class,new EnumDeserializers.JobSearchStatusDeserializers())
                //.registerTypeAdapter()
                .create();
    }
}
