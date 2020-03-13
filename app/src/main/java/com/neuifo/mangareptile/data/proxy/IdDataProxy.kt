package com.neuifo.mangareptile.data.proxy

import com.neuifo.domain.exception.ApiException
import com.neuifo.mangareptile.data.usecase.UseCase
import com.neuifo.mangareptile.utils.ToastUtil
import io.reactivex.disposables.Disposable

/**
 * **描述:** 有请求参数的网络请求代理。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/4/1  1:42 PM
 *
 * **版本:** v 1.0.0
 */
abstract class IdDataProxy<ID, D> : IdActionDataProxy<ID, D>() {

    private val defaultAction = ActionParameter.createInstance()

    final override fun createUseCase(id: ID, action: ActionParameter): UseCase<D> {
        return createUseCase(id)
    }

    abstract fun createUseCase(id: ID): UseCase<D>

    final override fun checkNetworkEnable(id: ID, action: ActionParameter): Boolean {
        return checkNetworkEnable(action)
    }

    protected open fun checkNetworkEnable(action: ActionParameter): Boolean = true

    fun request(id: ID): Disposable? {
        return super.request(defaultAction, id)
    }

    fun bind(owner: ProxyOwner, callBack: IdDataCallback<ID, D>): IdDataProxy<ID, D> {
        super.bind(owner, callBack)
        return this
    }

    fun bind(owner: ProxyOwner): IdDataProxy<ID, D> {
        bind(owner, InnerCallback())
        return this
    }

    fun onSuccess(onSuccess: (id: ID, data: D) -> Unit): IdDataProxy<ID, D> {
        if (mGlobalCallback != null && mGlobalCallback is InnerCallback) {
            (mGlobalCallback as InnerCallback).success = onSuccess
        } else {
            mGlobalCallback = SingleCallback()
            (mGlobalCallback as SingleCallback).success = onSuccess
        }
        return this
    }

    fun onFailed(onFailed: (id: ID, e: ApiException) -> Unit): IdDataProxy<ID, D> {
        if (mGlobalCallback != null && mGlobalCallback is InnerCallback) {
            (mGlobalCallback as InnerCallback).failed = onFailed
        } else {
            mGlobalCallback = SingleCallback()
            (mGlobalCallback as SingleCallback).failed = onFailed
        }
        return this
    }

    private open inner class InnerCallback : IdActionDataCallback<ID, ActionParameter, D> {

        var success: ((id: ID, data: D) -> Unit)? = null

        var failed: ((id: ID, e: ApiException) -> Unit)? = null

        override fun onSuccess(id: ID, action: ActionParameter, data: D) {
            success?.invoke(id, data)
        }

        override fun onFailed(id: ID, action: ActionParameter, e: ApiException) {
            if (failed != null) {
                failed?.invoke(id, e)
            } else {
                ToastUtil.showShortToast(e.displayMessage)
            }
        }
    }

    private inner class SingleCallback : InnerCallback() {
        override fun onSuccess(id: ID, action: ActionParameter, data: D) {
            super.onSuccess(id, action, data)
            unbind()
        }

        override fun onFailed(id: ID, action: ActionParameter, e: ApiException) {
            super.onFailed(id, action, e)
            unbind()
        }
    }

    abstract class IdDataCallback<ID, D> : IdActionDataCallback<ID, ActionParameter, D> {
        final override fun onSuccess(id: ID, action: ActionParameter, data: D) {
            onSuccess(id, data)
        }

        abstract fun onSuccess(id: ID, data: D)

        final override fun onFailed(id: ID, action: ActionParameter, e: ApiException) {
            onFailed(id, e)
        }

        abstract fun onFailed(id: ID, e: ApiException)
    }
}