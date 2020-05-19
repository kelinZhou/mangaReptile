package com.neuifo.mangareptile.ui.base.listcell

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.annotation.*
import androidx.recyclerview.widget.RecyclerView
import com.neuifo.mangareptile.R

import java.lang.Exception

/**
 * **描述: ** Cell的基类。
 *
 * **创建人: ** kelin
 *
 * **创建时间: ** 2018/4/30  下午12:49
 *
 * **版本: ** v 1.0.0
 */
abstract class SimpleCell : Cell {

    private var parent: RecyclerView.ViewHolder? = null

    var isInWindow = false
        private set

    protected val layoutPosition: Int
        get() = parent?.layoutPosition ?: 0

    override val containerView: View?
        get() = parent!!.itemView

    protected val resources: Resources
        get() = containerView!!.resources

    protected val context: Context by lazy { containerView!!.context }

    @CallSuper
    final override fun onCreate(parent: RecyclerView.ViewHolder) {
        this.parent = parent
        onCreate(parent.itemView)
    }

    override fun setIsRecyclable(recyclable: Boolean) {
        parent?.setIsRecyclable(recyclable)
    }

    open fun onCreate(itemView: View) {
    }

    final override fun bindData(position: Int, parent: RecyclerView.ViewHolder) {
        this.parent = parent
        onBindData(parent.itemView)
    }

    protected abstract fun onBindData(iv: View)

    final override fun onItemClick(context: Context, position: Int) =
        onItemClick(getItemView(), context, position)

    protected open fun onItemClick(iv: View, context: Context, position: Int) {}

    final override fun onItemLongClick(context: Context, position: Int) =
        onItemLongClick(getItemView(), context, position)

    protected open fun onItemLongClick(iv: View, context: Context, position: Int) {}

    final override fun onItemChildClick(context: Context, position: Int, v: View) {
        onItemChildClick(getItemView(), context, position, v)
    }

    protected open fun onItemChildClick(iv: View, context: Context, position: Int, v: View) {}

    override val clickableIds: IntArray?
        get() = null

    override val itemClickable: Boolean
        get() = false

    override val itemLongClickable: Boolean
        get() = false

    override val haveItemClickBg: Boolean
        get() = itemClickable

    override val itemClickableViewId: Int
        get() = 0

    override val itemBackgroundResource: Int
        get() = R.drawable.selector_recycler_item_bg_white

    override fun getItemSpanSize(totalSpanCount: Int): Int = totalSpanCount

    override fun needFilterDoubleClick(v: View): Boolean = true

    @CallSuper
    final override fun onViewAttachedToWindow(parent: RecyclerView.ViewHolder, position: Int) {
        this.parent = parent
        isInWindow = true
        onViewAttachedToWindow(parent.itemView, position)
    }

    @CallSuper
    final override fun onViewDetachedFromWindow(parent: RecyclerView.ViewHolder, position: Int) {
        this.parent = parent
        isInWindow = false
        onViewDetachedFromWindow(parent.itemView, position)
    }

    protected open fun onViewAttachedToWindow(iv: View, position: Int) {
    }

    protected open fun onViewDetachedFromWindow(iv: View, position: Int) {
    }

    @Suppress("UNCHECKED_CAST")
    fun <V : View> getItemView(): V = (parent!!.itemView as? V)
        ?: throw ClassCastException("${parent!!.itemView.javaClass.simpleName} can't case be V!")

    @Suppress("UNCHECKED_CAST")
    fun <V : View> getItemViewOrNull(): V? = (parent?.itemView as? V)

    fun <V : View> getView(@IdRes viewId: Int): V = getItemView<View>().findViewById(viewId)

    fun getDimension(@DimenRes dimen: Int): Int {
        return resources.getDimension(dimen).toInt()
    }

    fun getString(@StringRes stringRes: Int): CharSequence {
        return resources.getString(stringRes)
    }

    @ColorInt
    fun getColor(@ColorRes colorRes: Int): Int {
        return resources.getColor(colorRes)
    }

    fun getDrawable(@DrawableRes drawableRes: Int): Drawable? {
        return try {
            resources.getDrawable(drawableRes)
        } catch (e: Exception) {
            null
        }
    }

    fun setDrawableStart(tv: TextView, @DrawableRes drawable: Int) {
        val drawable = getDrawable(drawable)
        drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        tv.setCompoundDrawables(drawable, null, null, null)
    }

    fun setDrawableEnd(tv: TextView, @DrawableRes drawable: Int) {
        val drawable = getDrawable(drawable)
        drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        tv.setCompoundDrawables(null, null, drawable, null)
    }

    fun setDrawableStartAndEnd(
        tv: TextView,
        @DrawableRes drawableStart: Int,
        @DrawableRes drawableEnd: Int
    ) {
        val start = getDrawable(drawableStart)
        start?.setBounds(0, 0, start.minimumWidth, start.minimumHeight)
        val end = getDrawable(drawableEnd)
        end?.setBounds(0, 0, end.minimumWidth, end.minimumHeight)
        tv.setCompoundDrawables(start, null, end, null)
    }
}
