package com.neuifo.data.domain.repo.dmzj

import android.content.Context
import com.neuifo.data.api.dmzj.CommentApi
import com.neuifo.data.api.dmzj.ImageApi
import com.neuifo.domain.model.base.HttpResult
import com.neuifo.data.api.dmzj.DmzjApi
import com.neuifo.data.api.dmzj.Host
import com.neuifo.data.cache.CacheFactory
import com.neuifo.data.cache.ComicDetailCacheImpl
import com.neuifo.data.cache.basic.ComicCache
import com.neuifo.data.converter.CommentConverter
import com.neuifo.data.converter.CommonConverter
import com.neuifo.data.domain.utils.LogHelper
import com.neuifo.data.interceptor.DownloadProgressInterceptor
import com.neuifo.data.interceptor.HeadTokenInterceptor
import com.neuifo.domain.model.DownloadProgressListener
import com.neuifo.data.util.RetrofitServiceFactory
import com.neuifo.domain.model.dmzj.*
import com.neuifo.domain.repo.dmzj.DmzjRepo
import com.neuifo.domain.request.AuthRequest
import com.neuifo.domain.utils.DateHelper
import com.neuifo.domain.utils.DateType
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.io.InputStream

class DmzjRepoImpl(context: Context) : DmzjRepo {

    private val dmzjApi: DmzjApi by lazy {
        RetrofitServiceFactory.createRetorfitService(
            DmzjApi::class.java,
            Host.LATESTAPI,
            CommonConverter::class.java,
            HeadTokenInterceptor("418dab9cd91f3d261a5f3f2ad57462be", 107228800)
        )
    }

    private val commentApi: CommentApi by lazy {
        RetrofitServiceFactory.createRetorfitService(
            CommentApi::class.java,
            Host.COMMENT,
            CommentConverter::class.java,
            HeadTokenInterceptor(uid = 107228800)
        )
    }

    private val comicCache: ComicCache by lazy {
        CacheFactory.instance.comicCache!!
    }


    override fun login(authRequest: AuthRequest): Observable<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSubscribe(page: Int): Observable<MutableList<ComicUpdate>> {
        return dmzjApi.getSubscribe(page = page - 1).map { data ->
            comicCache.saveComicUpdate(data)
            comicCache.saveListSubscribe(data)
            mapReadHistory(data)
            data
        }
    }

    private fun mapReadHistory(data: MutableList<ComicUpdate>) {
        comicCache.queryComicByIDs(data.map { it.id }).map { queryData ->
            val returnData = data.find { it.id == queryData.id }
            if (returnData != null) {
                returnData.last_read_name = queryData.last_read_name
                returnData.last_read_chapter_id = queryData.last_read_chapter_id
            }
        }
    }

    override fun getLastestList(page: Int): Observable<MutableList<ComicUpdate>> {
        val latest = dmzjApi.getLatest(page - 1).map {
            comicCache.saveComicUpdate(it)
            it
        }
        val orignSource = if (page == 1) {
            dmzjApi.getRecommendList().onErrorReturn { HttpResult<RecommendItem>() }
                .zipWith(
                    latest,
                    BiFunction { t1: HttpResult<RecommendItem>, t2: MutableList<ComicUpdate> ->
                        t2.sort()
                        if (!t1.isEmpty && t1.data.items.isNotEmpty()) {
                            t2.addAll(0, t1.data.items)
                            comicCache.saveComicUpdate(t1.data.items)
                            comicCache.saveListSubscribe(
                                t1.data.items
                            )
                        }
                        t2
                    })
        } else {
            latest
        }
        return orignSource.map { data ->
            mapReadHistory(data)
            data
        }
    }

    override fun subscribeComic(comicId: Long): Observable<Boolean> {
        return dmzjApi.addSubscribe(comicId).map {
            comicCache.saveSubscribe(1, comicId)
            true
        }
    }

    override fun unSubscribeComic(comicId: Long): Observable<Boolean> {
        return dmzjApi.cancelSubscribe(comicId).map {
            comicCache.saveSubscribe(0, comicId)
            true
        }
    }


    override fun getComicDetail(
        comicId: Long,
        page: Int,
        size: Int
    ): Observable<ComicDetailWarpper> {
        if (page == 1) {
            return dmzjApi.getComicDetail(comicId)
                .zipWith(getCommentList(comicId, page), BiFunction { detail, comment ->
                    val lastReadChapterId = comicCache.queryLastReadChapterId(detail.id)
                    detail.chapters.map { chapter ->
                        chapter.data.map { item ->
                            item.comicId = comicId
                            item.showMarker = item.chapterId > lastReadChapterId
                        }
                    }
                    ComicDetailWarpper(detail, comment)
                })
        } else {
            return getCommentList(comicId, page).map {
                ComicDetailWarpper(ComicDetail.createShareData(comicId, ""), it)
            }
        }
    }

    override fun getChapter(comicId: Long, chapterId: Long): Observable<Chapter> {
        return dmzjApi.getChapterDetail(comicId, chapterId)
    }


    override
    fun getImage(
        url: String, progressListener: DownloadProgressListener?
    ): Observable<InputStream> {
        var imageApi = RetrofitServiceFactory.createRetorfitService(
            ImageApi::class.java,
            Host.IMAGEAPI,
            ints = *arrayOf(DownloadProgressInterceptor(progressListener))
        )
        return imageApi.getImageData(url).map {
            val body = it.body()
            body?.byteStream()
        }
    }

    override fun getCommentList(comicId: Long, page: Int): Observable<MutableList<Comment>> {
        return commentApi.getComment(comicId, page)
    }

}