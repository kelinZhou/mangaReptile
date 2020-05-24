package com.neuifo.data.api.dmzj

import com.neuifo.domain.model.base.HttpResult
import com.neuifo.domain.model.dmzj.Chapter
import com.neuifo.domain.model.dmzj.ComicDetail
import com.neuifo.domain.model.dmzj.ComicUpdate
import com.neuifo.domain.model.dmzj.RecommendItem
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface DmzjApi {

    @GET("/latest/100/{page}.json")
    fun getLatest(
        @Path("page") page: Int
    ): Observable<MutableList<ComicUpdate>>


    @GET("/UCenter/subscribe")
    fun getSubscribe(
        //@Query("uid") id: Long,
        @Query("sub_type") sub_type: Int = 1,
        @Query("letter") letter: String = "all",
        @Query("page") page: Int,
        @Query("type") type: Int = 0
    ): Observable<MutableList<List<ComicUpdate>>>


    @GET("/recommend/batchUpdate")
    fun getRecommendList(
        @Query("category_id") sub_type: Int = 49
    ): Observable<HttpResult<RecommendItem>>

    //http://v3api.dmzj.com/comic/comic_49689.json
    @GET("/comic/comic_{comicId}.json")
    fun getComicDetail(
        @Path("comicId") comicId: Long
    ): Observable<ComicDetail>


    @GET("/chapter/{comicId}/{chapterId}.json")
    fun getChapterDetail(
        @Path("comicId") comicId: Long,
        @Path("chapterId") chapterId: Long
    ): Observable<Chapter>

}