package com.neuifo.mangareptile.ui.detail

import android.content.Intent
import com.hw.ycshareelement.YcShareElement
import com.neuifo.domain.model.dmzj.ComicDetailWarpper
import com.neuifo.mangareptile.data.core.API
import com.neuifo.mangareptile.ui.base.listcell.SimpleCell
import com.neuifo.mangareptile.ui.base.presenter.ItemListFragmentPresenter
import com.neuifo.mangareptile.ui.detail.cell.ComicDetailHeadCell
import io.reactivex.Observable

class ComicDetailFragment :
    ItemListFragmentPresenter<ComicDetailDelegate, ComicDetailDelegate.ComicDetailDelegateCallback, Long, SimpleCell, ComicDetailWarpper>() {


    companion object {
        private const val COMIC_DETAIL_ID = "comic_detail_id"

        fun setComicId(intent: Intent, comicId: Long) {
            intent.putExtra(COMIC_DETAIL_ID, comicId)
        }
    }

    override fun transformUIData(
        page: Int,
        itemList: MutableList<SimpleCell>,
        data: ComicDetailWarpper
    ) {
        val comicDetailHeadCell = ComicDetailHeadCell(data.comicDetail)
        YcShareElement.setEnterTransitions(requireActivity(), comicDetailHeadCell)
        itemList.add(comicDetailHeadCell)
        YcShareElement.postStartTransition(requireActivity())
    }

    override fun checkIfGotAllData(data: ComicDetailWarpper): Boolean {
        return data.comment.isEmpty() || data.comment.size < 20
    }

    override fun getApiObservable(
        id: Long,
        page: Int,
        size: Int
    ): Observable<ComicDetailWarpper> = API.DMZJ_Dmzj.getComicDetail(id, page, size)


    override fun addMoreData(
        initialData: ComicDetailWarpper,
        data: ComicDetailWarpper
    ): ComicDetailWarpper {
        initialData.comment.addAll(data.comment)
        return initialData
    }

    override val initialRequestId: Long by lazy {
        requireArguments().getLong(COMIC_DETAIL_ID)
    }


    override val viewCallback: ComicDetailDelegate.ComicDetailDelegateCallback
        get() = ComicDetailFragmentCallback()


    private inner class ComicDetailFragmentCallback : ItemListDelegateCallbackImpl(),
        ComicDetailDelegate.ComicDetailDelegateCallback

}