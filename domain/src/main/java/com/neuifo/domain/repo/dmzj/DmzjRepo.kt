package com.neuifo.domain.repo.dmzj

import com.neuifo.domain.model.dmzj.ComicUpdate
import com.neuifo.domain.repo.Repo
import com.neuifo.domain.request.AuthRequest
import io.reactivex.Observable

interface DmzjRepo : Repo {

    fun login(authRequest: AuthRequest): Observable<Boolean>

    fun getLastestList(page:Int):Observable<MutableList<ComicUpdate>>
}