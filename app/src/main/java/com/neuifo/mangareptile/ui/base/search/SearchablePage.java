package com.neuifo.mangareptile.ui.base.search;

import android.text.InputFilter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 描述 描述有搜索功能的页面。
 * 创建人 kelin
 * 创建时间 2017/2/16  下午6:48
 * 版本 v 1.0.0
 */

public interface SearchablePage {

    /**
     * 初始化的对象。该方法的调用时机是对象被new出来以后，onCreate之前。
     */
    void onInitSearchPage(@NonNull Searcher searcher);

    /**
     * 获取搜索框内显示的提示文字。
     */
    @NonNull
    String getSearchHint();

    /**
     * 当需要搜索的时候调用。
     *
     * @param searchKey 要搜索的关键字。
     */
    void onSearch(@NonNull String searchKey);

    void onCancel();

    void onActionDown(@NonNull String key);

    @Nullable
    InputFilter[] getInputFilter();

    int IME_OPTIONS();


}
