package com.neuifo.data.api.dmzj

import com.neuifo.domain.model.dmzj.Comment
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentApi {


    //v1/4/latest/49566?page_index=1&limit=10&channel=Android&_debug=0&version=2.7.024&timestamp=1590734974

    @GET("/v1/4/latest/{comicId}")
    fun getComment(
        @Path("comicId") comicId: Long,
        @Query("page_index") page: Int,
        @Query("limit") size: Int = 10
    ): Observable<MutableList<Comment>>
}