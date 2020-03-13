package com.neuifo.mangareptile.data.core

import android.util.SparseArray
import com.neuifo.data.domain.repo.LogicRepoImpl
import com.neuifo.data.domain.repo.dmzj.AuthRepoImpl
import com.neuifo.domain.executor.ThreadExecutor
import com.neuifo.domain.repo.LogicRepo
import com.neuifo.domain.repo.Repo
import com.neuifo.domain.repo.dmzj.AuthRepo
import com.neuifo.mangareptile.data.proxy.IdActionDataProxy
import com.neuifo.mangareptile.domain.util.JobExecutor
import com.neuifo.mangareptile.domain.util.UIThread

object API {
    private val repoCache = SparseArray<Repo>()

    val DMZJ_AUTH: AuthRepo
        get() = getRepo(Type.AUTH)

    val LOGIC: LogicRepo
        get() = getRepo(Type.LOGIC)

    val provideThreadExecutor: ThreadExecutor = JobExecutor()
    val postExecutionThread: UIThread = UIThread()

    fun clear() {
        clearRepo()

        if (provideThreadExecutor is JobExecutor) {
            provideThreadExecutor.shutdown()
        }
        IdActionDataProxy.clearUseCase()
    }

    private fun clearRepo() {
        repoCache.clear()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <API : Repo> getRepo(apiType: Type): API {
        var repo = repoCache[apiType.code]
        if (repo == null) {
            val context = AppModule.getContext()
            repo = when (apiType) {
                Type.AUTH -> AuthRepoImpl(
                    context
                )
                Type.LOGIC -> LogicRepoImpl(
                    context
                )
            }
            repoCache.put(apiType.code, repo)
        }
        return repo as API
    }

    enum class Type(val code: Int) {
        AUTH(0x0f0),
        LOGIC(0x0f1),
        ;
    }
}