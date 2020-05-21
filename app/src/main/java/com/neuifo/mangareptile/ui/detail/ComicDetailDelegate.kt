package com.neuifo.mangareptile.ui.detail

import androidx.core.view.ViewCompat
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo
import com.neuifo.domain.model.dmzj.ComicDetail
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.domain.util.ImageLoaderUtils
import com.neuifo.mangareptile.ui.base.delegate.ItemListDelegate
import com.neuifo.mangareptile.ui.base.listcell.SimpleCell
import kotlinx.android.synthetic.main.layout_comic_detail_head.*

class ComicDetailDelegate :
    ItemListDelegate<ComicDetailDelegate.ComicDetailDelegateCallback, SimpleCell>(),
    IShareElements {

    override val swipeLayoutId: Int
        get() = -1

    override val dataViewId: Int
        get() = R.id.list_view


    fun setInitShowData(cover: String) {
        ImageLoaderUtils.displayRoundCorners(layout_comic_detail_head_cover, cover, 20)
        ViewCompat.setTransitionName(layout_comic_detail_head_cover, cover)
    }

    interface ComicDetailDelegateCallback : ItemListDelegateCallback<SimpleCell> {
        fun getComicCover(): ComicDetail
    }

    override val rootLayoutId: Int
        get() = R.layout.layout_comic_detail

    override fun getShareElements(): Array<ShareElementInfo<ComicDetail>> {
        return arrayOf(
            ShareElementInfo<ComicDetail>(
                layout_comic_detail_head_cover,
                viewCallback.getComicCover()
            )
        )
    }

}