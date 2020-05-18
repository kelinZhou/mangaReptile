package com.neuifo.mangareptile.ui.base.home

import com.neuifo.mangareptile.ui.base.presenter.BaseFragmentPresenter

class HomeFragment : BaseFragmentPresenter<HomeDelegate, HomeDelegate.HomeDelegateCallback>() {

    override val saveDelegate: Boolean
        get() = true


    override fun onViewDelegateBound(vd: HomeDelegate) {
        super.onViewDelegateBound(vd)
        if (vd.isEmpty()) {
            vd.init(childFragmentManager)
        }
    }

    override val viewCallback: HomeDelegate.HomeDelegateCallback
        get() = HomeFragmentCallback()


    private inner class HomeFragmentCallback : HomeDelegate.HomeDelegateCallback

}