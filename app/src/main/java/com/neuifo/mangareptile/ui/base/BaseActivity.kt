package com.neuifo.mangareptile.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.TaskStackBuilder
import androidx.fragment.app.Fragment

import com.lieluobo.candidate.annotation.SoftInputModeFlags
import com.neuifo.data.domain.utils.LogHelper
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.annotation.NetworkType
import com.neuifo.mangareptile.data.core.AppModule
import com.neuifo.mangareptile.event.EventBus
import com.neuifo.mangareptile.event.EventHolder
import com.neuifo.mangareptile.utils.MeasureUtil
import com.neuifo.mangareptile.utils.statusbar.StatusBarHelper


/**
 * **描述:** Activity的基类，所有Activity都应该由该类派生。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/3/27  4:08 PM
 *
 * **版本:** v 1.0.0
 */
abstract class BaseActivity : AppCompatActivity(), EventBus.OnEventListener<EventBus.BusEvent> {

    /**
     * 用来记录当前页面的Toolbar控件。
     */
    var toolbar: Toolbar? = null
    /**
     * 用来记录当前页面的标题控件。
     */
    private var tvTitle: TextView? = null
    /**
     * 用来记录当前页面的子标题控件。
     */
    private var tvSubtitle: TextView? = null

    var isActivityResumed = false
        private set

    protected var isDarkMode: Boolean = false

    private var mTitleCenter = true
    private val statusBarHelper: StatusBarHelper by lazy { StatusBarHelper(this) }

    protected val toolbarPosition: IntArray
        get() {
            val location = IntArray(2)
            if (toolbar != null) {
                toolbar!!.getLocationInWindow(location)
                location[1] = location[1] + toolbar!!.height - StatusBarHelper.getStatusBarHeight(this)
            }
            return location
        }

    val isActivityDestroyed: Boolean
        get() = isDestroyed || isFinishing


    protected open val networkDisconnectedHintEnable: Boolean
        get() = true

    fun isForegroundFragment(fragmentClass: Class<out BaseFragment>): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(fragmentClass.simpleName)
        return fragment != null && fragment.isAdded && !fragment.isHidden && fragment.isResumed && fragment.isVisible
    }

    protected fun setNavigationIcon(@DrawableRes iconId: Int) {
        if (iconId != 0) {
            toolbar?.setNavigationIcon(iconId)
        } else {
            toolbar?.navigationIcon = null
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val fm = supportFragmentManager
        @SuppressLint("RestrictedApi") val frags = fm.fragments
        if (frags != null) {
            for (f in frags) {
                if (f is BaseFragment && event != null) {
                    val isConsum = f.dispatchKeyEvent(event)
                    return if (isConsum) isConsum else super.dispatchKeyEvent(event)
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title ?: "")
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (networkDisconnectedHintEnable) {
            //AppModule.eventBus.register<EventHolder.NetworkEvent>(EventHolder.NetworkEvent::class.java, this)
        }

        try {
            //Android8.0如果Activity是透明的则会崩溃，但是小米手机上调用这行代码虽然会崩溃但仍然可以实现屏幕锁定。所以这里用try catch。
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (ignore: Exception) {
            LogHelper.system.e("=========锁定屏幕方向失败：", this.javaClass.simpleName)
        }
        titleColor = resources.getColor(android.R.color.white)
    }

    override fun onStart() {
        super.onStart()
        if (isDarkMode) {
            StatusBarHelper.setStatusBarDarkMode(this)
        } else {
            StatusBarHelper.setStatusBarLightMode(this)
        }
    }

    public override fun onDestroy() {
        //AppModule.eventBus.unregister(this)
        super.onDestroy()
    }

    protected fun disableHomeAsUp() {
        setNavigationIcon(0)
    }

    override fun onTitleChanged(title: CharSequence, color: Int) {
        super.onTitleChanged(title, color)
        if (mTitleCenter) {
            tvTitle?.text = title
//            tvTitle?.setTextColor(color)
        } else {
            toolbar?.title = title
//            toolbar?.setTitleTextColor(color)
        }
    }

    fun setTitle(title: CharSequence?, center: Boolean) {
        mTitleCenter = center
        super.setTitle(title)
    }

    fun setSubTitle(title: CharSequence, center: Boolean) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            if (center) {
                if (tvSubtitle != null) {
                    tvSubtitle!!.visibility = View.VISIBLE
                    tvSubtitle!!.text = title
                }
            } else {
                actionBar.setDisplayShowTitleEnabled(true)
                actionBar.subtitle = title
            }
            mTitleCenter = center
        }
    }

    /**
     * 覆盖软键盘模式。调用改方法前必须确保[.overrideWindowSoftInputModeEnable]返回true，否则该方法不会生效。
     *
     * @param mode 要设置的模式。
     * @see .overrideWindowSoftInputModeEnable
     */
    fun overrideWindowSoftInputMode(@SoftInputModeFlags mode: Int) {
        if (overrideWindowSoftInputModeEnable()) {
            window.setSoftInputMode(mode)
        }
    }

    /**
     * 是否可以覆盖软键盘的弹出模式。
     *
     * 如果当前的Activity中包含了[BaseFragment]实例，
     * 并且覆盖了[BaseFragment.windowSoftInputModeFlags]方法或者调用了 [ ][BaseFragment.overrideWindowSoftInputMode]方法的话会导致[BaseActivity]本身所配置的软键盘启动模式失效，
     * 无论是通过清单文件配置还是代码配置，在[BaseFragment]启动后都会失效，如果你希望在这种情况下以[BaseActivity]为准，
     * 则需要覆盖该方法并返回false。如果返回了false，则[.overrideWindowSoftInputMode]方法就会失效。
     *
     * @return 如果允许覆盖则返回true，否则返回false。默认实现为可以覆盖。
     * @see .overrideWindowSoftInputMode
     * @see BaseFragment.overrideWindowSoftInputMode
     * @see BaseFragment.windowSoftInputModeFlags
     */
    open fun overrideWindowSoftInputModeEnable(): Boolean {
        return true
    }


    fun initTitleBar(
        toolbar: Toolbar?,
        titleView: TextView?,
        subtitleView: TextView?,
        needNavigationIcon: Boolean = true
    ) {
        if (toolbar != null) {
            this.toolbar = toolbar
            this.tvTitle = titleView
            this.tvSubtitle = subtitleView
            setSupportActionBar(toolbar)

            if (titleView != null) {
                titleView.compoundDrawablePadding = MeasureUtil.dp2px(applicationContext, 5f)
            }
            val actionBar = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false)
                actionBar.setDisplayHomeAsUpEnabled(false)
            }
            if (needNavigationIcon) {
                setNavigationIcon(if (isDarkMode) R.drawable.ic_toolbar_navigation_back_white else R.drawable.ic_toolbar_navigation_back_black)
            }
            val parent = toolbar.parent as? View
            if (parent != null) {
                statusBarHelper.setActionBarView(parent)
            }
//            toolbar.setTitleTextAppearance(this, R.style.common_title_text_style)
        }
    }

    protected fun onToolBarLeftClick() {
        finish()
    }

    fun processStatusBar(@ColorInt color: Int) {
        statusBarHelper.process(color)
        if (isDarkMode) {  //这里加这语句是因为如果onStart中的代码执行之后又执行了这里会导致设置失效。
            StatusBarHelper.setStatusBarDarkMode(this)
        } else {
            StatusBarHelper.setStatusBarLightMode(this)
        }
    }


    @JvmOverloads
    protected fun addFragmentV4(containerViewId: Int, fragment: Fragment, tag: String = fragment.javaClass.simpleName) {
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()
        fragmentTransaction.add(containerViewId, fragment, tag)
        fragmentTransaction.commitAllowingStateLoss()
    }


    protected fun replaceFragmentV4(containerViewId: Int, fragment: Fragment) {
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(containerViewId, fragment, fragment.javaClass.simpleName)
        fragmentTransaction.setCustomAnimations(
            R.anim.anim_translate_x100_x0_300,
            R.anim.anim_translate_x0_x100minus_300,
            R.anim.anim_translate_x100minus_x0_300,
            R.anim.anim_translate_x0_x100_300
        )
        fragmentTransaction.commitAllowingStateLoss()
    }


    @JvmOverloads
    protected fun pushFragmentV4(
        containerViewId: Int,
        fragment: Fragment,
        tag: String = fragment.javaClass.simpleName
    ) {
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(containerViewId, fragment, tag).addToBackStack(null)
        fragmentTransaction.commitAllowingStateLoss()
    }

    protected fun pushFragment(containerViewId: Int, fragment: android.app.Fragment) {
        this.fragmentManager.beginTransaction().replace(containerViewId, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.name).commitAllowingStateLoss()
    }

    protected fun popFragmentV4() {
        this.supportFragmentManager.popBackStack()
    }

    public override fun onResume() {
        super.onResume()
        isActivityResumed = true
    }

    public override fun onPause() {
        isActivityResumed = false
        super.onPause()
    }

    override fun onSupportNavigateUp(): Boolean {
        LogHelper.system.d(javaClass.simpleName, "onSupportNavigateUp")
        return super.onSupportNavigateUp()
    }

    override fun onCreateSupportNavigateUpTaskStack(builder: TaskStackBuilder) {
        super.onCreateSupportNavigateUpTaskStack(builder)

        LogHelper.system.d(javaClass.simpleName, "onCreateSupportNavigateUpTaskStack")
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        LogHelper.system.d(javaClass.simpleName, "onPrepareOptionsMenu")
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onOptionsMenuClosed(menu: Menu) {
        LogHelper.system.d(javaClass.simpleName, "onOptionsMenuClosed")
        super.onOptionsMenuClosed(menu)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        LogHelper.system.d(javaClass.simpleName, "onMenuOpened")
        return super.onMenuOpened(featureId, menu)
    }

    override fun onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed()
        }
    }

    /**
     * 当网络状态被改变之后执行。 该方法不一定会被执行，如果你覆盖了[.networkDisconnectedHintEnable]方法并返回false的话，该方法是不会被执行的。
     *
     * @param networkType 当前事件的网络类型。
     * @param isConnect 当前网络是否可用。
     */
    protected open fun onNetworkStateChanged(@NetworkType networkType: Int, isConnect: Boolean) {}

    final override fun onEvent(e: EventBus.BusEvent) {
        if (e is EventHolder.NetworkEvent) {
            onNetworkStateChanged(e.networkType, e.isConnect)
        } else {
            onReceiveEvent(e)
        }
    }

    protected open fun onReceiveEvent(e: EventBus.BusEvent) {}

    protected fun registerEventListener(tClass: Class<out EventBus.BusEvent>) {
        //AppModule.eventBus.register(tClass, this)
    }


    protected fun <T : EventBus.BusEvent> registerStickyEventListener(tClass: Class<T>) {
        //AppModule.eventBus.registerSticky(tClass, this)
    }


    protected fun <T : EventBus.BusEvent> postStickyEvent(e: T) {
        //AppModule.eventBus.postStickyEvent(e)
    }

    protected fun <T : EventBus.BusEvent> postEvent(e: T) {
        //AppModule.eventBus.postEvent(e)
    }

    protected fun createFragmentByClass(clazz: Class<out Fragment>, intent: Intent) =
        BaseFragment.newInstance(clazz, intent.extras)

    companion object {
        fun isForegroundActivity(context: Context, activityClassName: String): Boolean {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            if (!TextUtils.isEmpty(activityClassName) && am != null) {
                val list = am.getRunningTasks(1)
                if (list != null && list.size > 0) {
                    return TextUtils.equals(activityClassName, list[0].topActivity.className)
                }
            }
            return false
        }

        fun generateJumpIntent(context: Context, activityClass: Class<out BaseActivity>): Intent {
            val intent = Intent(context, activityClass)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            return intent
        }
    }
}
