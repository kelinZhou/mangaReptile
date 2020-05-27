package com.neuifo.mangareptile.ui.home

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neuifo.mangareptile.ui.base.delegate.ItemListDelegate
import com.neuifo.mangareptile.ui.home.cell.ComicUpdateCell

class UpdateComicListDelegate :
    ItemListDelegate<UpdateComicListDelegate.UpdateComicListDelegateCallBack, ComicUpdateCell>() {


    override fun onCreateLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 3).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.getItemViewType(position) == adapter.getLoadMoreViewType()) 3 else 1
                    //return if (position == adapter.itemCount) 3 else 1
                    //return 1
                }
            }
        }
    }


    interface UpdateComicListDelegateCallBack :
        ItemListDelegateCallback<ComicUpdateCell>
}