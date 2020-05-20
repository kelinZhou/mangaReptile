package com.neuifo.mangareptile.ui.home.cell

import android.view.View
import com.neuifo.domain.model.dmzj.ComicUpdate
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.domain.util.ImageLoaderUtils
import com.neuifo.mangareptile.ui.base.listcell.SimpleCell
import com.neuifo.mangareptile.utils.notNull
import kotlinx.android.synthetic.main.item_comic.view.*

class ComicUpdateCell(private var comicUpdate: ComicUpdate) : SimpleCell() {


    override fun onBindData(iv: View) {
        ImageLoaderUtils.displayRoundCorners(iv.item_comic_cover, comicUpdate.cover, 20)
        iv.item_comic_title.text = comicUpdate.title
        iv.item_comic_update_chapter.text = comicUpdate.updateContent()
        iv.item_comic_read_chapter.text = comicUpdate.readContent()

    }

    override val itemClickable: Boolean
        get() = true

    override val haveItemClickBg: Boolean
        get() = true

    override val itemBackgroundResource: Int
        get() = R.drawable.selector_recycler_item_bg_black

    override val itemLayoutRes: Int
        get() = R.layout.item_comic

    /**
     * 这里目前不需要，毕竟只有2种类型
     */
    override fun getItemSpanSize(totalSpanCount: Int): Int = 1

}