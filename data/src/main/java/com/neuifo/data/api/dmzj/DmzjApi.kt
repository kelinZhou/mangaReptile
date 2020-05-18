package com.neuifo.data.api.dmzj

import com.neuifo.domain.model.dmzj.ComicUpdate
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DmzjApi {

    //channel=Android&_debug=0&version=2.7.024&timestamp={time}
    @GET("/latest/100/{page}.json")
    fun getLastest(
        @Path("page") page: Int,
        @Query("channel") channel: String = "Android",
        @Query("version") version: String = "2.7.024",
        @Query("timestamp") timestamp: Long
    ): Observable<MutableList<ComicUpdate>>


    @GET("/UCenter/subscribe")
    fun getSubscribe(
        @Query("uid") id: Long,
        @Query("sub_type") sub_type: Int = 1,
        @Query("letter") letter: String = "all",
        @Query("channel") channel: String = "Android",
        @Query("version") version: String = "2.7.024",
        @Query("_debug") _debug: String = "0",
        @Query("dmzj_token") dmzj_token: String,
        @Query("page") page: Int,
        @Query("type") type: Int = 0,
        @Query("timestamp") timestamp: Long
    ): Observable<MutableList<List<ComicUpdate>>>
}