package com.neuifo.mangareptile.ui.detail

import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.ui.base.delegate.ItemListDelegate
import com.neuifo.mangareptile.ui.detail.cell.ComicChapterCell
import com.neuifo.widgetlibs.statelayout.StatePage


class ChapterListDelegate :
    ItemListDelegate<ChapterListDelegate.ChapterListDelegateCallBack, ComicChapterCell>() {

    override val pageStateFlags: Int
        get() = StatePage.NOTHING_STATE

    override val swipeLayoutId: Int
        get() = -1

    override val dataViewId: Int
        get() = R.id.list_view

    interface ChapterListDelegateCallBack :
        ItemListDelegateCallback<ComicChapterCell>

    override val rootLayoutId: Int
        get() = R.layout.common_list_without_fresh
}