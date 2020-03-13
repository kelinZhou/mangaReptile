package com.neuifo.data.domain.repo.dmzj

import android.content.Context
import com.neuifo.domain.repo.dmzj.AuthRepo
import com.neuifo.domain.request.AuthRequest
import io.reactivex.Observable

class AuthRepoImpl(context:Context):AuthRepo{



    override fun login(authRequest: AuthRequest): Observable<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}