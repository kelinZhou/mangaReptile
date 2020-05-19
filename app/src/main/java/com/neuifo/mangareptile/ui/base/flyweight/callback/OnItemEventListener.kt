package com.neuifo.mangareptile.ui.base.flyweight.callback

import android.view.View

/**
 * **描述:** 条目的事件监听。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/4/8  10:05 AM
 *
 * **版本:** v 1.0.0
 */
interface OnItemEventListener<T> {

    fun onItemClick(position: Int, item: T)

    fun onItemLongClick(position: Int, item: T)

    fun onItemChildClick(v: View, position: Int, item: T)
}