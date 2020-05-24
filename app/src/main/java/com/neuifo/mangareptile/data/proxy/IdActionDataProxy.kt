package com.neuifo.mangareptile.data.proxy

import android.util.LruCache
import android.util.SparseArray
import com.kelin.apkUpdater.util.NetWorkStateUtil
import com.neuifo.domain.exception.ApiException
import com.neuifo.domain.exception.ErrorWrapper
import com.neuifo.mangareptile.data.core.AppModule
import com.neuifo.mangareptile.data.subscriber.NetErrorHandler
import com.neuifo.mangareptile.data.usecase.UseCase
import com.neuifo.mangareptile.utils.ToastUtil
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import java.lang.Exception

/**
 * **描述:** 有请求参数的网络请求代理。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/4/1  1:42 PM
 *
 * **版本:** v 1.0.0
 */
abstract class IdActionDataProxy<ID, D> : UnBounder {

    companion object {
        private const val CONSTANT_CACHE_KEY = 1
        private val mUseCaseMap = LruCache<Int, UseCase<*>>(5)

        fun clearUseCase() {
            mUseCaseMap.evictAll()
        }
    }

    var uncheckNetWork = false
    var noAnyToast = false

    private val defaultRequestId = Any()
    var isWorking: Boolean = false
        private set

    protected var mGlobalCallback: IdActionDataCallback<ID, ActionParameter, D>? = null
    private val mSubscriptions = ExtCompositeSubscription()
    private val mErrorCount = SparseArray<ErrorWrapper>()

    val isBound: Boolean
        get() = mGlobalCallback != null

    protected abstract fun createUseCase(id: ID, action: ActionParameter): UseCase<D>

    protected open fun checkNetworkEnable(id: ID, action: ActionParameter): Boolean = true

    /**
     * 执行请求，调用该方法使代理去做他该做的事情。
     * @param action 动作，用来表示当前请求是如何触发的。
     * @param id 请求参数。
     */
    fun request(action: ActionParameter, id: ID): Disposable? {
        isWorking = true
        var e = checkPreCondition(id, action)
        val observer = createCallback(id, action)
        if (e != null) {
            observer.onError(e)
            observer.onComplete()
            observer.dispose()
            return null
        }

        if (isExceedMaxErrorCount(id, action)) {
            e = ApiException(ApiException.Error.FAIL_TOO_MUCH.code, "失败次数过多")
            observer.onError(e)
            observer.onComplete()
            observer.dispose()
            return null
        }

        //这里泄漏了。。
        @SuppressWarnings("unchecked")
        //var useCase = mUseCaseMap.get(getCacheKey(id, action)) as? UseCase<D>
        //if (useCase == null) {
        //useCase = createUseCase(id, action)
        //mUseCaseMap.put(getCacheKey(id, action), useCase)
        //}
        var useCase = createUseCase(id, action)
        useCase.execute(observer)

        //mSubscriptions.clearAllUnSubscribed()
        mSubscriptions.add(observer)
        return observer
    }

    private fun isExceedMaxErrorCount(id: ID, action: ActionParameter): Boolean {
        val key = getCacheKey(id, action)
        val errorWrapper = mErrorCount.get(key) ?: return false

        if (errorWrapper.count.toInt() > ErrorWrapper.MAX_ERROR_COUNT) {
            val current = System.currentTimeMillis()

            if (current - errorWrapper.lastTime < ErrorWrapper.MAX_ERROR_PERIOD) {
                return true
            } else {
                errorWrapper.count.set(0)
            }
        }
        return false
    }

    private fun getCacheKey(id: ID, action: ActionParameter): Int {
        var result = id?.hashCode() ?: CONSTANT_CACHE_KEY
        result = 31 * result + action.hashCode()
        return result
    }

    open fun createCallback(id: ID, action: ActionParameter): DisposableObserver<D> {
        return IdActionCaseSubscriber(id, action)
    }

    private fun checkPreCondition(id: ID, action: ActionParameter): ApiException? {
        return if (uncheckNetWork || !checkNetworkEnable(id, action)) {
            null
        } else {
            if (!NetWorkStateUtil.isConnected(AppModule.getContext())) {
                ApiException(ApiException.Error.NETWORK_UNAVAILABLE)
            } else {
                null
            }
        }
    }

    fun bind(
        owner: ProxyOwner,
        callBack: IdActionDataCallback<ID, ActionParameter, D>
    ): IdActionDataProxy<ID, D> {
        if (mGlobalCallback != null) {
            if (mGlobalCallback != callBack) {
                unbind()
                mGlobalCallback = callBack
            }
        } else {
            mGlobalCallback = callBack
        }
        owner.attachToOwner(this)
        return this
    }

    override fun unbind(thorough: Boolean) {
        this.mGlobalCallback = null

        if (thorough) {
            mSubscriptions.disposed()
        } else {
            mSubscriptions.clear()
        }
    }

    /**
     * 用户行为观察者
     */
    internal inner class IdActionCaseSubscriber(
        private val id: ID,
        private val action: ActionParameter
    ) : NetErrorHandler<D>() {
        /**
         * 处理http权限错误
         * @param ex 异常
         */
        override fun handlePermissionError(ex: ApiException) {
            super.handlePermissionError(ex)
            if (!noAnyToast) {
                ToastUtil.showErrorToast("您的登录已过期，请重新登录。")
            }
        }

        override fun onFinished(successful: Boolean) {
            isWorking = false
        }

        override fun onError(ex: ApiException) {
            mGlobalCallback?.onFailed(id, action, ex)
        }

        override fun onSuccess(t: D) {
            try {
                isWorking = false
                mGlobalCallback?.onSuccess(id, action, t)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    interface IdActionDataCallback<ID, ACTION : ActionParameter, D> {

        fun onSuccess(id: ID, action: ACTION, data: D)

        fun onFailed(id: ID, action: ACTION, e: ApiException)
    }

    interface DefaultIdDataCallback<ID, ACTION : ActionParameter, D> :
        IdActionDataCallback<ID, ACTION, D> {

        fun onLoadSuccess(id: ID, data: D)

        fun onLoadFailed(id: ID, e: ApiException)

        fun onRetrySuccess(id: ID, data: D)

        fun onRetryFailed(id: ID, e: ApiException)

        fun onRefreshSuccess(id: ID, data: D)

        fun onRefreshFailed(id: ID, e: ApiException)
    }

    interface PageIdDataCallback<ID, ACTION : ActionParameter, D> :
        DefaultIdDataCallback<ID, ACTION, D> {

        fun onLoadMoreFailed(id: ID, e: ApiException)

        fun onLoadMoreSuccess(id: ID, data: D)
    }
}