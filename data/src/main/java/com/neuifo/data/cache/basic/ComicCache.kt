package com.neuifo.data.cache.basic

import com.neuifo.domain.model.dmzj.ComicDetail
import com.neuifo.domain.model.dmzj.ComicUpdate

interface ComicCache {


    fun queryComicByIDs(ids: List<Long>): MutableList<ComicUpdate>

    fun queryLastReadChapterId(comicId: Long): Long

    fun queryLastReadChapterName(comicId: Long): String

    fun hasSubscribed(comicId: Long): Int

    fun queryLastReadPage(comicId: Long,chapterId: Long): Int

    @Deprecated(" 影响shared element。弃用")
    fun saveComicDetail(detail: ComicDetail)

    fun saveReadDetail(comicId: Long, chapterId: Long, chapterName: String, chapterIndex: Int)

    fun saveSubscribe(code: Int, comicId: Long)

    fun saveListSubscribe(data: MutableList<ComicUpdate>)

    fun saveComicUpdate(comicUpdate: MutableList<ComicUpdate>)
}