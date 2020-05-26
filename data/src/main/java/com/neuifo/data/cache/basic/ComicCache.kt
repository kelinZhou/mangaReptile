package com.neuifo.data.cache.basic

import com.neuifo.domain.model.dmzj.ComicDetail
import com.neuifo.domain.model.dmzj.ComicUpdate
import io.reactivex.Observable

interface ComicCache {


    fun queryComicByIDs(ids: List<Long>): Observable<MutableList<ComicUpdate>>

    fun saveComicDetail(detail: ComicDetail)

    fun saveReadDetail(comicId: Long, chapterId: Long, chapterName: String, chapterIndex: Int)

    fun queryLastReadChapterId(comicId: Long): Long

    fun saveSubscribe(code: Int, comicId: Long)

    fun hasSubscribed(comicId: Long):Int

    fun saveListSubscribe(data: MutableList<ComicUpdate>)

    fun saveComicUpdate(comicUpdate: MutableList<ComicUpdate>)
}