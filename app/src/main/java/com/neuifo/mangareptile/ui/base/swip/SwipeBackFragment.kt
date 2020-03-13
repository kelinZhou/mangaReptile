package com.neuifo.mangareptile.ui.base.swip

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.neuifo.mangareptile.ui.base.BaseFragment
import com.neuifo.mangareptile.widget.SwipeBackLayout

abstract class SwipeBackFragment : BaseFragment() {

    private val swipeBackLayout by lazy { SwipeBackLayout(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSwipeBackLayout()
        if (savedInstanceState != null) {
            val isSupportHidden = savedInstanceState.getBoolean(
                SWIPE_BACK_FRAGMENT_STATE_SAVE_IS_HIDDEN
            )

            val fm = fragmentManager
            if (fm != null) {
                val ft = fm.beginTransaction()
                if (isSupportHidden) {
                    ft.hide(this)
                } else {
                    ft.show(this)
                }
                ft.commit()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val view = view
        if (view != null) {
            view.isClickable = true
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SWIPE_BACK_FRAGMENT_STATE_SAVE_IS_HIDDEN, isHidden)
    }

    private fun initSwipeBackLayout() {
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        swipeBackLayout.layoutParams = params
        swipeBackLayout.setBackgroundColor(Color.TRANSPARENT)
    }

    protected fun attachToSwipeBack(view: View): View {
        swipeBackLayout.attachToFragment(this, view)
        return swipeBackLayout
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            swipeBackLayout.hiddenFragment()
        }
    }

    protected fun setSwipeBackEnable(enable: Boolean) {
        swipeBackLayout.setEnableGesture(enable)
    }

    companion object {
        private const val SWIPE_BACK_FRAGMENT_STATE_SAVE_IS_HIDDEN = "swipe_back_fragment_state_save_is_hidden"
    }
}
