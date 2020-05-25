package com.neuifo.domain.repo.dmzj

import com.neuifo.domain.model.DownloadProgressListener
import com.neuifo.domain.model.dmzj.Chapter
import com.neuifo.domain.model.dmzj.ComicDetailWarpper
import com.neuifo.domain.model.dmzj.ComicUpdate
import com.neuifo.domain.repo.Repo
import com.neuifo.domain.request.AuthRequest
import io.reactivex.Observable
import java.io.InputStream

interface DmzjRepo : Repo {

    fun login(authRequest: AuthRequest): Observable<Boolean>

    fun getLastestList(page: Int): Observable<MutableList<ComicUpdate>>

    fun getSubscribe(page: Int): Observable<MutableList<ComicUpdate>>

    fun getComicDetail(comicId: Long, page: Int, size: Int): Observable<ComicDetailWarpper>

    fun getChapter(comicId: Long, chapterId: Long): Observable<Chapter>

    fun getImage(url: String, progressListener: DownloadProgressListener?): Observable<InputStream>
}