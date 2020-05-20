package com.neuifo.data.cache.basic

import com.neuifo.domain.model.dmzj.ComicDetail
import com.neuifo.domain.model.dmzj.ComicUpdate
import io.reactivex.Observable

interface ComicCache {


    fun queryComicByIDs(ids:List<Long>): Observable<MutableList<ComicUpdate>>

    fun saveComicDetail(detail:ComicDetail)
}