package com.neuifo.data.api.dmzj

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ImageApi {


    @Headers("Referer: http://images.dmzj.com/")
    @Streaming
    @GET
    fun getImageData(@Url url: String): Observable<Response<ResponseBody>>
}