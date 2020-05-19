package com.neuifo.mangareptile.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.neuifo.mangareptile.AppLayerErrorCatcher
import com.neuifo.mangareptile.BuildConfig
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.SystemError
import com.neuifo.mangareptile.ui.CommonErrorFragment
import com.neuifo.mangareptile.utils.ToastUtil

/**
 * **描述:** 用来承载Fragment的基类。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019-09-26  16:23
 *
 * **版本:** v 1.0.0
 */
abstract class BaseFragmentActivity : BaseActivity() {

    @get:LayoutRes
    protected open val activityRootLayout: Int
        get() = R.layout.activity_common_layout

    @get:IdRes
    protected open val warpFragmentId: Int
        get() = R.id.fragment_container

    @get:IdRes
    protected open val toolbarId: Int
        get() = R.id.my_awesome_toolbar

    @get:IdRes
    protected open val toolbarTitleViewId: Int
        get() = R.id.toolbar_center_title

    @get:IdRes
    protected open val toolbarSubTitleViewId: Int
        get() = R.id.toolbar_sub_title

    protected open val addInitialFragmentEnable: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityRootLayout)
        if (addInitialFragmentEnable) {
            addInitialFragment()
        }
        initTitleBar(findViewById(toolbarId), findViewById(toolbarTitleViewId), findViewById(toolbarSubTitleViewId))
    }

    private fun addInitialFragment() {
        val intent = intent
        val curTarget = intent.getIntExtra(KEY_TARGET_PAGE, PAGE_UNKNOWN)
        if (curTarget == PAGE_UNKNOWN) {
            if (BuildConfig.DEBUG) {
                throw RuntimeException("The target page value is unknown!")
            } else {
                onJumpError(SystemError.NULL_ARGUMENT, RuntimeException("The target page value is unknown!"))
                return
            }
        }
        addFragmentV4(warpFragmentId, getCurrentFragment(curTarget, intent))
    }

    protected open fun onJumpError(systemError: SystemError, exception: Throwable = RuntimeException(systemError.text)): CommonErrorFragment {
        ToastUtil.showShortToast(systemError.text)
        AppLayerErrorCatcher.throwException(exception)
        return CommonErrorFragment.createInstance(systemError)
    }

    protected abstract fun getCurrentFragment(targetPage: Int, intent: Intent): Fragment

    companion object {

        /**
         * 用来获取目标页面的键。
         */
        private const val KEY_TARGET_PAGE = "key_target_page"
        /**
         * 表示当前目标页面为未知的。
         */
        private const val PAGE_UNKNOWN = 0

        fun getJumpIntent(context: Context, activityClass: Class<out BaseFragmentActivity>, targetPage: Int): Intent {
            val intent = generateJumpIntent(context, activityClass)
            intent.putExtra(KEY_TARGET_PAGE, targetPage)
            return intent
        }
    }
}