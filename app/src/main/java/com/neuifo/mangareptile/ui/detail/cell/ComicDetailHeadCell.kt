package com.neuifo.mangareptile.ui.detail.cell

import android.os.Parcelable
import android.view.View
import androidx.core.view.ViewCompat
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo
import com.neuifo.domain.model.dmzj.ComicDetail
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.domain.util.ImageLoaderUtils
import com.neuifo.mangareptile.ui.base.listcell.SimpleCell
import kotlinx.android.synthetic.main.layout_comic_detail_head.view.*


class ComicDetailHeadCell(var comicDetail: ComicDetail) : SimpleCell(), IShareElements {


    override fun onBindData(iv: View) {
        ImageLoaderUtils.displayRoundCorners(iv.layout_comic_detail_head_cover, comicDetail.cover, 20)
        ViewCompat.setTransitionName(iv.layout_comic_detail_head_cover, comicDetail.cover)
    }

    override val itemLayoutRes: Int
        get() = R.layout.layout_comic_detail_head

    override fun getShareElements(): Array<ShareElementInfo<ComicDetail>> {
        return arrayOf(ShareElementInfo<ComicDetail>(getView(R.id.layout_comic_detail_head_cover), comicDetail))
    }

}