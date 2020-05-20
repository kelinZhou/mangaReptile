package com.neuifo.mangareptile.ui.detail

import com.neuifo.mangareptile.ui.base.delegate.ItemListDelegate
import com.neuifo.mangareptile.ui.base.listcell.SimpleCell

class ComicDetailDelegate : ItemListDelegate<ComicDetailDelegate.ComicDetailDelegateCallback, SimpleCell>() {

    interface ComicDetailDelegateCallback : ItemListDelegateCallback<SimpleCell>
}