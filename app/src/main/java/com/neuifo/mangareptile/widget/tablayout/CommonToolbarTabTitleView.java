package com.neuifo.mangareptile.widget.tablayout;

import android.content.Context;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * <strong>描述: </strong> TabLayout的Tab。
 * <p><strong>创建人: </strong> kelin
 * <p><strong>创建时间: </strong> 2018/6/19  下午5:57
 * <p><strong>版本: </strong> v 1.0.0
 */
public class CommonToolbarTabTitleView extends SimplePagerTitleView {
    private OnSelectedListener listener;

    public CommonToolbarTabTitleView(Context context) {
        this(context, null);
    }

    public CommonToolbarTabTitleView(Context context, OnSelectedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public void onSelected(int index, int totalCount) {
        super.onSelected(index, totalCount);
        getPaint().setFakeBoldText(true);
        setTextColor(mSelectedColor);
        if (mSelectedColor == mNormalColor) {
            invalidate();
        }
        if (listener != null) {
            listener.onSelected(index);
        }
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        super.onDeselected(index, totalCount);
        getPaint().setFakeBoldText(false);
        setTextColor(mNormalColor);
        if (mSelectedColor == mNormalColor) {
            invalidate();
        }
    }
}
