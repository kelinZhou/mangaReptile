package com.neuifo.domain.repo.dmzj

import com.neuifo.domain.repo.Repo
import com.neuifo.domain.request.AuthRequest
import io.reactivex.Observable

interface AuthRepo : Repo {

    fun login(authRequest: AuthRequest): Observable<Boolean>
}