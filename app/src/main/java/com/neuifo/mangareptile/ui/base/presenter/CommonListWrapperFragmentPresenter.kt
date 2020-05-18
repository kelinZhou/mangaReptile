package com.neuifo.mangareptile.ui.base.presenter

import android.view.View
import com.kelin.apkUpdater.exception.IllegalCallException
import com.neuifo.domain.exception.ApiException
import com.neuifo.mangareptile.data.proxy.*
import com.neuifo.mangareptile.ui.base.delegate.CommonListWrapperDelegate
import com.neuifo.mangareptile.utils.StyleHelper
import io.reactivex.Observable


abstract class CommonListWrapperFragmentPresenter<V : CommonListWrapperDelegate<VC, I, VD>, VC : CommonListWrapperDelegate.CommonListDelegateCallback<I>, ID, I, W, VD> :
    CommonFragmentPresenter<V, VC, ID, W, VD>() {

    override val initialDataProxy: IdActionDataProxy<ID, W> by lazy {
        if (isEnablePage) {
            ProxyFactory.createPageIdActionProxy { id, pages ->
                getApiObservable(id, pages.page, pages.size)
            }
        } else {
            ProxyFactory.createIdActionProxy<ID, W> {
                getApiObservable(it, 1, 1)
            }
        }.bind(this, onCreateDataCallback())
    }

    /**
     * 是否分页,是否支持分页有子类重写此方法决定。
     */
    protected open val isEnablePage: Boolean
        get() = true

    override val actionParameter: PageActionParameter by lazy { PageActionParameter.createInstance(isEnablePage) }

    final override fun getApiObservable(id: ID): Observable<W> {
        throw IllegalCallException("This method can't be called!")
    }

    protected abstract fun getApiObservable(id: ID, page: Int, size: Int): Observable<W>

    override fun loadInitialData(id: ID?) {
        if (setupMode != SETUP_NO_PROXY) {
            viewDelegate?.showLoadingView()
            initialDataProxy.request(
                actionParameter.updateAction(
                    LoadAction.LOAD,
                    PageActionParameter.FIRST_PAGE_NUMBER
                ), id ?: initialRequestId
            )
        } else {
            throw RuntimeException("The Setup Mode is SETUP_NO_PROXY")
        }
    }

    override fun reloadInitialData() {
        if (setupMode != SETUP_NO_PROXY) {
            viewDelegate?.showLoadingView()
            initialDataProxy.request(
                actionParameter.updateAction(
                    LoadAction.RETRY,
                    PageActionParameter.FIRST_PAGE_NUMBER
                ), initialRequestId
            )
        } else {
            throw RuntimeException("The Setup Mode is SETUP_NO_PROXY")
        }
    }

    override fun refreshData(showProgress: Boolean) {
        if (setupMode != SETUP_NO_PROXY) {
            if (showProgress) {
                StyleHelper.showProgress(context)
            }
            initialDataProxy.request(
                actionParameter.updateAction(
                    LoadAction.REFRESH,
                    PageActionParameter.FIRST_PAGE_NUMBER
                ), initialRequestId
            )
        } else {
            throw RuntimeException("The Setup Mode is SETUP_NO_PROXY")
        }
    }

    protected open fun loadMore(retry: Boolean) {
        if (isEnablePage && setupMode != SETUP_NO_PROXY) {
            val curPage = actionParameter.pages!!.page
            initialDataProxy.request(
                actionParameter.updateAction(
                    LoadAction.LOAD_MORE,
                    if (retry) curPage else (curPage + 1)
                ), initialRequestId
            )
        } else {
            throw RuntimeException("The Setup Mode is SETUP_NO_PROXY")
        }
    }

    protected open fun onListItemClick(position: Int, item: I) {}

    protected open fun onListItemLongClick(position: Int, item: I) {}

    protected open fun onListItemChildClick(position: Int, v: View, item: I) {}

    override fun <ACTION : ActionParameter> onCreateDataCallback(): IdActionDataProxy.IdActionDataCallback<ID, ACTION, W> {
        return CommonDataListCallbackImpl()
    }

    /**
     * 允许分页的情况下调用
     *
     * @param initialData 已经获取到的数据集合。
     * @param data        加载更多获取到的数据集合。
     */
    protected open fun addMoreData(initialData: W, data: W): W {
        if (isEnablePage) {
            throw RuntimeException("The method must implementation, in " + javaClass.simpleName + "。")
        }
        return initialData
    }

    /**
     * 允许分页的情况下调用，检查当前数据是否是最后一波数据。
     *
     * @param data 当前从网络获取到的数据。
     * @return 如果返回 `true` 说明当前已经加载到了最后一页，则不会继续加载更多。
     */
    protected open fun checkIfGotAllData(data: W): Boolean {
        return checkAnyIfGotAllData(data as Any)
    }

    /**
     * 允许分页的情况下调用，检查当前数据是否是最后一波数据。
     *
     * @param data 当前从网络获取到的数据。
     * @return 如果返回 `true` 说明当前已经加载到了最后一页，则不会继续加载更多。
     */
    protected fun checkAnyIfGotAllData(data: Any): Boolean {
        return !isEnablePage || data !is Collection<*> || data.isEmpty() || (data.size < actionParameter.pages?.size ?: PageActionParameter.DEFAULT_PAGE_SIZE)
    }

    /**
     * 数据请求回调
     */
    protected inner class CommonDataListCallbackImpl<ACTION : ActionParameter> : CommonDataLoadCallbackImpl<ACTION>(),
        IdActionDataProxy.PageIdDataCallback<ID, ACTION, W> {

        override fun onSuccess(id: ID, action: ACTION, data: W) {
            if (action.action == LoadAction.REFRESH) {
                StyleHelper.hideProgress(context)
            }
            if (!onInterceptLoadSuccess(id, action, data)) {
                when (action.action) {
                    LoadAction.LOAD -> onLoadSuccess(id, data)
                    LoadAction.RETRY -> onRetrySuccess(id, data)
                    LoadAction.REFRESH -> onRefreshSuccess(id, data)
                    LoadAction.LOAD_MORE -> onLoadMoreSuccess(id, data)
                }
            }
        }

        override fun onFailed(id: ID, action: ACTION, e: ApiException) {
            if (action.action == LoadAction.REFRESH) {
                StyleHelper.hideProgress(context)
            }
            if (!onInterceptLoadFailed(id, action, e)) {
                when (action.action) {
                    LoadAction.LOAD -> onLoadFailed(id, e)
                    LoadAction.RETRY -> onRetryFailed(id, e)
                    LoadAction.REFRESH -> onRefreshFailed(id, e)
                    LoadAction.LOAD_MORE -> onLoadMoreFailed(id, e)
                }
            }
        }

        override fun onLoadSuccess(id: ID, data: W) {
            val vd = viewDelegate
            if (isEnablePage && vd != null) {
                vd.setNoMoreData(checkIfGotAllData(data))
            }
            super.onLoadSuccess(id, data)

        }

        override fun onRefreshSuccess(id: ID, data: W) {
            val vd = viewDelegate
            if (isEnablePage && vd != null) {
                vd.setNoMoreData(checkIfGotAllData(data))
            }
            super.onRefreshSuccess(id, data)
            vd?.reSetRefresh()
        }

        override fun onRefreshFailed(id: ID, e: ApiException) {
            super.onRefreshFailed(id, e)
            viewDelegate?.reSetRefresh()
        }

        override fun onLoadMoreSuccess(id: ID, data: W) {
            val vd = viewDelegate
            if (vd != null) {
                initialData = addMoreData(initialData!!, data)
                vd.addMoreData(transformUIData(actionParameter.pages!!.page, data))
                vd.setLoadMoreFinish()
                if (checkIfGotAllData(data)) {
                    vd.setNoMoreData(true)
                }
            }
        }


        override fun onLoadMoreFailed(id: ID, e: ApiException) {
            viewDelegate?.setLoadMoreFailed()
        }
    }

    final override fun transformUIData(data: W): VD {
        return transformUIData(1, data)
    }

    protected abstract fun transformUIData(page: Int, data: W): VD

    /**
     * Delegate回调
     */
    open inner class CommonListDelegateCallbackImpl : CommonDelegateCallbackImpl(),
        CommonListWrapperDelegate.CommonListDelegateCallback<I> {

        override fun isLoadMoreEnable(): Boolean {
            return isEnablePage
        }

        override fun onListItemClick(position: Int, item: I) {
            this@CommonListWrapperFragmentPresenter.onListItemClick(position, item)
        }

        override fun onListItemLongClick(position: Int, item: I) {
            this@CommonListWrapperFragmentPresenter.onListItemLongClick(position, item)
        }

        override fun onListItemChildClick(position: Int, v: View, item: I) {
            this@CommonListWrapperFragmentPresenter.onListItemChildClick(position, v, item)
        }

        override fun onLoadMore(retry: Boolean) {
            if (setupMode == SETUP_DEFAULT || setupMode == SETUP_TIMELY) {
                if (isEnablePage && viewDelegate?.isNoMoreData() != true && !isProxyWorking) {
                    loadMore(retry)
                }
            } else {
                viewDelegate?.setNoMoreData(true)
            }
        }
    }
}
