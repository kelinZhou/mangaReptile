package com.neuifo.mangareptile.ui.base.home.cell

import android.view.View
import com.neuifo.domain.model.dmzj.ComicUpdate
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.domain.util.ImageLoaderUtils
import com.neuifo.mangareptile.ui.base.listcell.SimpleCell
import kotlinx.android.synthetic.main.item_comic.view.*

class ComicUpdateCell(var comicUpdate: ComicUpdate) : SimpleCell() {


    override fun onBindData(iv: View) {
        ImageLoaderUtils.display(iv.item_comic_cover, comicUpdate.cover)
        iv.iv_item_comic_title.text = comicUpdate.title
    }

    override val itemLayoutRes: Int
        get() = R.layout.item_comic

}