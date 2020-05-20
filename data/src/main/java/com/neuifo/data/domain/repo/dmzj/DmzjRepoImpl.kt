package com.neuifo.data.domain.repo.dmzj

import android.content.Context
import com.neuifo.domain.model.base.HttpResult
import com.neuifo.data.api.dmzj.DmzjApi
import com.neuifo.data.api.dmzj.Host
import com.neuifo.data.cache.ComicDetailCacheImpl
import com.neuifo.data.converter.CommonConverter
import com.neuifo.data.domain.utils.LogHelper
import com.neuifo.data.interceptor.HeadTokenInterceptor
import com.neuifo.data.util.RetrofitServiceFactory
import com.neuifo.domain.model.dmzj.*
import com.neuifo.domain.repo.dmzj.DmzjRepo
import com.neuifo.domain.request.AuthRequest
import com.neuifo.domain.utils.DateHelper
import com.neuifo.domain.utils.DateType
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class DmzjRepoImpl(context: Context) : DmzjRepo {

    private val dmzjApi: DmzjApi by lazy {
        RetrofitServiceFactory.createRetorfitService(
            DmzjApi::class.java,
            Host.LATESTAPI,
            CommonConverter::class.java,
            HeadTokenInterceptor("418dab9cd91f3d261a5f3f2ad57462be", 107228800)
        )
    }

    private val comicCache: ComicDetailCacheImpl by lazy {
        ComicDetailCacheImpl(context)
    }


    override fun login(authRequest: AuthRequest): Observable<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLastestList(page: Int): Observable<MutableList<ComicUpdate>> {
        val orignSource = if (page == 1) {
            dmzjApi.getRecommendList().onErrorReturn { HttpResult<RecommendItem>() }
                .zipWith(
                    dmzjApi.getLatest(page - 1),
                    BiFunction { t1: HttpResult<RecommendItem>, t2: MutableList<ComicUpdate> ->
                        t2.sort()
                        if (!t1.isEmpty && t1.data.items.isNotEmpty())
                            t2.addAll(0, t1.data.items)
                        t2
                    })
        } else {
            dmzjApi.getLatest(page - 1)
        }
        return orignSource.flatMap { data ->
            comicCache.queryComicByIDs(data.map { it.id }).map { queryResult ->
                queryResult.map { queryData ->
                    val returnData = data.find { it.id == queryData.id }
                    if (returnData != null) {
                        returnData.last_read_name = queryData.last_read_name
                        returnData.latest_update_chapter_name =
                            queryData.latest_update_chapter_name
                    }
                }
                data.toMutableList().apply {
                    forEach {
                        LogHelper.system.d(
                            "时间：" + DateHelper.formatData(
                                DateType.DATE_FORMAT_MM_DD_HH_MM,
                                it.latest_update_time * 1000
                            )
                        )
                    }
                }
            }
        }
    }


    override fun getComicDetail(id: Long, page: Int, size: Int): Observable<ComicDetailWarpper> {
        return dmzjApi.getComicDetail(id).map {
            ComicDetailWarpper(it, mutableListOf())
        }
    }

}