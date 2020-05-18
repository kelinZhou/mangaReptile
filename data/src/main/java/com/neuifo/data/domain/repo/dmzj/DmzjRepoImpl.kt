package com.neuifo.data.domain.repo.dmzj

import android.content.Context
import com.neuifo.data.api.dmzj.DmzjApi
import com.neuifo.data.api.dmzj.Host
import com.neuifo.data.converter.CommonConverter
import com.neuifo.data.util.RetrofitServiceFactory
import com.neuifo.domain.model.dmzj.ComicUpdate
import com.neuifo.domain.repo.dmzj.DmzjRepo
import com.neuifo.domain.request.AuthRequest
import io.reactivex.Observable

class DmzjRepoImpl(context: Context) : DmzjRepo {

    private val dmzjApi: DmzjApi by lazy {
        RetrofitServiceFactory.createRetorfitService(
            DmzjApi::class.java,
            Host.LATESTAPI,
            CommonConverter::class.java
        )
    }

    init {

    }


    override fun login(authRequest: AuthRequest): Observable<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLastestList(page: Int): Observable<MutableList<ComicUpdate>> {
        return dmzjApi.getLastest(page, timestamp = System.currentTimeMillis())
    }

}