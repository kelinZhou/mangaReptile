package com.neuifo.mangareptile.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo
import com.neuifo.data.cache.CacheFactory
import com.neuifo.domain.model.dmzj.ComicDetail
import com.neuifo.domain.utils.DateHelper
import com.neuifo.domain.utils.DateType
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.domain.util.ImageLoaderUtils
import com.neuifo.mangareptile.ui.base.delegate.ItemListDelegate
import com.neuifo.mangareptile.ui.base.listcell.SimpleCell
import com.neuifo.mangareptile.ui.base.presenter.ViewPresenter
import com.neuifo.mangareptile.utils.*
import kotlinx.android.synthetic.main.item_comic_detail_bar_head.*
import kotlinx.android.synthetic.main.layout_comic_detail.*
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

    fun updateComicInfo(comicDetail: ComicDetail) {
        layout_comic_detail_head_title.text = comicDetail.title
        layout_item_comic_detail_toolbar_title.text = comicDetail.title
        layout_comic_detail_head_subscribe.text =
            CacheFactory.instance.comicCache?.hasSubscribed(comicDetail.id)?.getSubscribed()
                ?.getSubscribedText()

        layout_comic_detail_head_author.text =
            KFontHandler.format("作者:${comicDetail.authors.joinToString("  ") { "<kFont clickable=${it.id} style=underline>${it.name}</kFont>" }}") { authorId ->
                viewCallback.jumpToAuth(authorId)
            }

        layout_comic_detail_head_status.text = "${DateHelper.formatData(
            DateType.DEFAULT_DATE_FORMAT,
            comicDetail.latest_update_time * 1000
        )}  ${comicDetail.status.joinToString("/") { it.name }}"
        layout_comic_detail_comic_desc.text = comicDetail.description
        layout_comic_detail_head_subscribe.setOnClickListener {
            viewCallback.subscrbie(!layout_comic_detail_head_subscribe.text.contains("取消"))
        }
    }

    var titleHeight = 0
    var titleScale = 0.0f

    override fun presentView(
        viewPresenter: ViewPresenter<ComicDetailDelegateCallback>,
        savedInstanceState: Bundle?
    ) {
        super.presentView(viewPresenter, savedInstanceState)
        ViewUtils.measureView(main_appbar)
        val initHeight = main_appbar.measuredHeight
        main_appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (titleHeight == 0) {
                //titleHeight = title!!.height
                val distanceTitle =
                    layout_item_comic_detail_toolbar_title!!.top + (titleHeight) / 2.0f
                titleScale = distanceTitle / (initHeight)
            }
            //layout_item_comic_detail_toolbar_title.translationY = titleScale * verticalOffset
        })


        main_appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if (state == State.EXPANDED) { //展开状态
                    layout_item_comic_detail_toolbar_title.visibility = View.GONE
                    layout_item_comic_detail_back.visibility = View.GONE
                    layout_item_comic_detail_download.visibility = View.GONE
                } else if (state == State.COLLAPSED) { //折叠状态
                    layout_item_comic_detail_back.visibility = View.VISIBLE
                    layout_item_comic_detail_download.visibility = View.VISIBLE
                    layout_item_comic_detail_toolbar_title.visibility = View.VISIBLE
                } else { //中间状态

                }
            }
        })

        bindClickEvent(layout_item_comic_detail_back, layout_item_comic_detail_download)
    }


    override fun onViewClick(v: View) {
        super.onViewClick(v)
        when (v) {
            layout_item_comic_detail_download -> {
                viewCallback.downLoad()
            }
            layout_item_comic_detail_back -> {
                viewCallback.exit()
            }
        }
    }

    interface ComicDetailDelegateCallback : ItemListDelegateCallback<SimpleCell> {
        fun getComicCover(): ComicDetail

        fun jumpToAuth(id: String)

        fun exit()
        fun subscrbie(flag: Boolean)
        fun downLoad()
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