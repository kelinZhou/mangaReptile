package com.neuifo.mangareptile.widget.flowlayout

import android.view.View

/**
 * <strong>描述: </strong> 标签选择器帮助类。
 * <p><strong>创建人: </strong> kelin
 * <p><strong>创建时间: </strong> 2018/8/16  下午1:39
 * <p><strong>版本: </strong> v 1.0.0
 */
object TagSelectorHelper {
    fun selectSingle(parent: FlowLayout, tagView: View) {
        for (i: Int in 0 until parent.childCount) {
            val at = parent.getChildAt(i)
            at.isSelected = at == tagView
        }
    }
}