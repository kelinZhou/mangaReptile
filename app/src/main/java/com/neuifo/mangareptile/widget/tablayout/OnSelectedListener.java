package com.neuifo.mangareptile.widget.tablayout;

/**
 * <strong>描述: </strong> 选中监听。
 * <p><strong>创建人: </strong> kelin
 * <p><strong>创建时间: </strong> 2018/6/19  下午5:55
 * <p><strong>版本: </strong> v 1.0.0
 */
public interface OnSelectedListener {
    /**
     * 被选中时调用。
     * @param index 当前被选中的索引。
     */
    void onSelected(int index);
}
