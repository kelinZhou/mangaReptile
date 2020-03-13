package com.neuifo.mangareptile.ui.base.swip

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.ui.base.BaseActivity
import com.neuifo.mangareptile.widget.SwipeBackLayout


/**
 * **描述:** 可以侧滑回退的Activity。
 *
 *
 * **创建人:** kelin
 *
 *
 * **创建时间:** 2019/4/17  5:31 PM
 *
 *
 * **版本:** v 1.0.0
 */
abstract class SwipeBackActivity : BaseActivity() {

    private val swipeBackLayout by lazy { SwipeBackLayout(this) }

    protected open val enableSwipe = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSwipeBackLayout()

        overridePendingTransition(R.anim.anim_translate_x100_x0_300, R.anim.anim_translate_xy0_xy0_300)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_translate_xy0_xy0_300, R.anim.anim_translate_x0_x100_300)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        swipeBackLayout.attachToActivity(this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : View?> findViewById(id: Int): T {
        val view = super.findViewById<View>(id)
        return if (view == null) {
            swipeBackLayout.findViewById<T>(id)
        } else view as T
    }

    private fun initSwipeBackLayout() {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.decorView.setBackgroundDrawable(null)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        swipeBackLayout.layoutParams = params
        swipeBackLayout.setEnableGesture(enableSwipe)
    }

    /**
     * 限制SwipeBack的条件,默认栈内Fragment数 <= 1时 , 优先滑动退出Activity , 而不是Fragment
     *
     * @return true: Activity可以滑动退出, 并且总是优先; false: Activity不允许滑动退出
     */
    fun swipeBackPriority(): Boolean {
        return supportFragmentManager.backStackEntryCount <= 1
    }
}
