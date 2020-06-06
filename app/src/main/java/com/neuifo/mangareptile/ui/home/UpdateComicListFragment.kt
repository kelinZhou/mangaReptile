package com.neuifo.mangareptile.ui.home

import android.os.Bundle
import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.IShareElementSelector
import com.hw.ycshareelement.transition.ShareElementInfo
import com.neuifo.domain.model.dmzj.ComicUpdate
import com.neuifo.mangareptile.data.core.API
import com.neuifo.mangareptile.ui.Navigator
import com.neuifo.mangareptile.ui.base.presenter.ItemListFragmentPresenter
import com.neuifo.mangareptile.ui.detail.ComicDetailFragment
import com.neuifo.mangareptile.ui.home.cell.ComicUpdateCell
import io.reactivex.Observable

class UpdateComicListFragment :
    ItemListFragmentPresenter<UpdateComicListDelegate, UpdateComicListDelegate.UpdateComicListDelegateCallBack, String, ComicUpdateCell, MutableList<ComicUpdate>>() {


    companion object {

        const val DATA_TYPE = "DATA_TYPE"

        fun getInstance(isHome: Boolean): UpdateComicListFragment {
            return UpdateComicListFragment().apply {
                val bundle = Bundle()
                bundle.putBoolean(DATA_TYPE, isHome)
                arguments = bundle
            }
        }
    }

    override fun transformUIData(
        page: Int,
        itemList: MutableList<ComicUpdateCell>,
        data: MutableList<ComicUpdate>
    ) {
        data.map {
            itemList.add(ComicUpdateCell(it))
        }
    }

    override fun addMoreData(
        initialData: MutableList<ComicUpdate>,
        data: MutableList<ComicUpdate>
    ): MutableList<ComicUpdate> {
        return initialData.apply {
            addAll(data)
        }.distinct().toMutableList()
    }

    override fun onInterceptListItemClick(position: Int, item: ComicUpdateCell): Boolean {
        val options = YcShareElement.buildOptionsBundle(requireActivity(), item)
        Navigator.jumpToComicDetail(
            requireActivity(),
            item.comicUpdate.id,
            item.comicUpdate.cover,
            options
        ) { resultCode, data ->
            val resultData = ComicDetailFragment.getResultData(data)
            if (!resultData.isNullOrEmpty()) {
                item.comicUpdate.last_read_name = resultData
                viewDelegate?.notifyItem(position)
            }
            YcShareElement.onActivityReenter(requireActivity(), resultCode, data,
                IShareElementSelector { _: MutableList<ShareElementInfo<*>> ->
                })
        }
        return super.onInterceptListItemClick(position, item)
    }

    override fun getApiObservable(
        id: String,
        page: Int,
        size: Int
    ): Observable<MutableList<ComicUpdate>> =
        if (requireArguments().getBoolean(DATA_TYPE))
            API.DMZJ_Dmzj.getLastestList(page)
        else API.DMZJ_Dmzj.getSubscribe(page)

    override val initialRequestId: String
        get() = ""

    override val viewCallback: UpdateComicListDelegate.UpdateComicListDelegateCallBack
        get() = UpdateComicListFragmentCallback()


    private inner class UpdateComicListFragmentCallback : ItemListDelegateCallbackImpl(),
        UpdateComicListDelegate.UpdateComicListDelegateCallBack
}