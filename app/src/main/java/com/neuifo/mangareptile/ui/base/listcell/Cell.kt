package com.neuifo.mangareptile.ui.base.listcell

import android.content.Context
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.extensions.LayoutContainer

/**
 * **描述: ** RecyclerView Cell的基类。
 *
 * **创建人: ** kelin
 *
 * **创建时间: ** 2018/4/26  上午11:41
 *
 * **版本: ** v 1.0.0
 */

interface Cell : LayoutContainer {

    /**
     * 获取条目类型。
     */
    @get:LayoutRes
    val itemLayoutRes: Int

    @get:IdRes
    val clickableIds: IntArray?

    @get:IdRes
    val itemClickableViewId: Int

    @get:DrawableRes
    val itemBackgroundResource: Int

    fun onCreate(parent: RecyclerView.ViewHolder)

    fun bindData(position: Int, parent: RecyclerView.ViewHolder)

    fun onItemClick(context: Context, position: Int)

    fun onItemLongClick(context: Context, position: Int)

    fun onItemChildClick(context: Context, position: Int, v: View)

    fun needFilterDoubleClick(v: View): Boolean

    fun onViewAttachedToWindow(parent: RecyclerView.ViewHolder, position: Int)

    fun onViewDetachedFromWindow(parent: RecyclerView.ViewHolder, position: Int)

    fun setIsRecyclable(recyclable: Boolean)

    fun getItemSpanSize(totalSpanCount: Int): Int

    val itemClickable: Boolean

    val itemLongClickable: Boolean

    val haveItemClickBg: Boolean


    companion object {

        fun equals(a: Cell?, b: Cell): Boolean {
            return a == b || (a != null && a == b)
        }
    }
}
