package com.neuifo.mangareptile.ui.base.home

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neuifo.mangareptile.ui.base.delegate.ItemListDelegate
import com.neuifo.mangareptile.ui.base.home.cell.ComicUpdateCell

class UpdateComicListDelegate :
    ItemListDelegate<UpdateComicListDelegate.UpdateComicListDelegateCallBack, ComicUpdateCell>() {


    override fun onCreateLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 3).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position >= adapter.itemCount - 1) 3 else 1
                }
            }
        }
    }


    interface UpdateComicListDelegateCallBack :
        ItemListDelegateCallback<ComicUpdateCell> {

    }
}