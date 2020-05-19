package com.neuifo.mangareptile.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.neuifo.mangareptile.annotation.SoftInputModeFlags
import com.neuifo.data.domain.utils.LogHelper
import com.neuifo.mangareptile.annotation.NetworkType
import com.neuifo.mangareptile.data.core.AppModule
import com.neuifo.mangareptile.event.EventBus
import com.neuifo.mangareptile.event.EventHolder


/**
 * **描述:** Fragment的基类。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/3/27  6:08 PM
 *
 * **版本:** v 1.0.0
 */
abstract class BaseFragment : Fragment(), EventBus.OnEventListener<EventBus.BusEvent> {

    protected val isRealResumed: Boolean
        @SuppressLint("RestrictedApi")
        get() {
            return isResumed && (userVisibleHint || isMenuVisible)

        }


    /**
     * 获取当前[BaseFragment]的软键盘启动模式。
     *
     * @return 返回当前 [BaseFragment] 的你所期望的软键盘启动模式。过个启动模式可以使用 "|" (或运算符)连接。
     * @see .overrideWindowSoftInputMode
     */
    @get:SoftInputModeFlags
    protected open val windowSoftInputModeFlags: Int = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED //默认实现，返回未指定任何模式的flag。

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        // FragmentStatePagerAdapter保存状态有问题，可能导致getUserVisibleHint为false，所以加上isMenuVisible一起判断
        if (userVisibleHint || isMenuVisible) {
            onRealResume()
        }
    }

    open fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return false
    }

    override fun onPause() {
        if (userVisibleHint) {
            onRealPause()
        }
        super.onPause()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isResumed) {
            if (isVisibleToUser) {
                onRealResume()
            } else {
                onRealPause()
            }
        }
        LogHelper.system.d(javaClass.simpleName, "setUserVisibleHint: isVisibleToUser? $isVisibleToUser")
    }

    @CallSuper
    protected open fun onRealResume() {
    }

    protected open fun onRealPause() {
    }

    protected fun hideToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    protected fun showToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    protected fun processStatusBar(@ColorInt color: Int) {
        (activity as? BaseActivity)?.processStatusBar(color)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            overrideWindowSoftInputMode(windowSoftInputModeFlags)
        }
        LogHelper.system.d(javaClass.simpleName, "onAttachView")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 覆盖软键盘弹出模式。该方法的实现方式为调用{@link BaseActivity#overrideWindowSoftInputMode(int)}，所以
     * 如果想要该方法生效必须保证当前{@link BaseFragment}是依附于{@link BaseActivity}的。
     * 另外还要确保{@link BaseActivity#overrideWindowSoftInputModeEnable()}方法的返回值为true。
     * <p>通常情况下该方法是不需要手动调用的，也不能被覆盖。该方法会在当前{@link BaseFragment}与{@link BaseActivity}
     * 发生关联并且是最初关联的时候调用一次，具体的参数值是通过{@link #windowSoftInputModeFlags()}方法返回的，
     * 也就是说一般情况下你只需要覆盖{@link #windowSoftInputModeFlags()}方法就可以了。
     * <p>调用该方法前你必须确保{@link #getActivity()}方法的返回值不为null，否则会抛出{@link NullPointerException}。
     *
     * @param flags 要设置的模式。
     * @see BaseActivity.overrideWindowSoftInputModeEnable()
     * @see BaseActivity.overrideWindowSoftInputMode(int)
     * @see windowSoftInputModeFlags()
     */
    fun overrideWindowSoftInputMode(@SoftInputModeFlags flags: Int) {
        if (activity != null) {
            if (activity is BaseActivity) {
                (activity as BaseActivity).overrideWindowSoftInputMode(flags)
            }
        } else {
            throw NullPointerException("The activity must not null!")
        }
    }

    protected fun finishActivity() {
        activity?.finish()
    }

    protected open fun onInterceptBackPressed(): Boolean {
        return false
    }

    protected fun setTitle(@StringRes title: Int, center: Boolean = true) {
        setTitle(context.getString(title), center)
    }

    protected fun setTitle(title: String?, center: Boolean = true) {
        (activity as? BaseActivity)?.setTitle(title, center)
    }

    override fun getContext(): Context {
        return super.getContext() ?: activity ?: AppModule.getContext()
    }

    @ColorInt
    protected fun getColor(@ColorRes color: Int): Int {
        return resources.getColor(color)
    }

    final override fun onEvent(e: EventBus.BusEvent) {
        if (e is EventHolder.NetworkEvent) {
            onNetWorkStateChanged(e.networkType, e.isConnect)
        }
        onReceiveEvent(e)
    }

    protected open fun onReceiveEvent(e: EventBus.BusEvent) {}

    protected open fun onNetWorkStateChanged(@NetworkType type: Int, isConnect: Boolean) {}

    companion object {
        fun newInstance(clazz: Class<out Fragment>, args: Bundle?): Fragment {
            try {
                val f = clazz.newInstance() as Fragment
                if (args != null) {
                    args.classLoader = f.javaClass.classLoader
                    f.arguments = args
                }
                return f
            } catch (e: Exception) {
                try {
                    val constructor = clazz.getDeclaredConstructor()
                    constructor.isAccessible = true
                    val f = constructor.newInstance() as Fragment
                    if (args != null) {
                        args.classLoader = f.javaClass.classLoader
                        f.arguments = args
                    }
                    return f
                } catch (e: Exception) {
                    throw InstantiationException("Unable to instantiate fragment $clazz: make sure class warehouseName exists, is public, and has an empty constructor that is public", e)
                }
            }

        }
    }
}