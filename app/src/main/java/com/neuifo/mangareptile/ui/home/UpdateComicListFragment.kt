package com.neuifo.mangareptile.ui.home

import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.IShareElementSelector
import com.hw.ycshareelement.transition.ShareElementInfo
import com.neuifo.domain.model.dmzj.ComicDetail
import com.neuifo.domain.model.dmzj.ComicUpdate
import com.neuifo.mangareptile.data.core.API
import com.neuifo.mangareptile.ui.Navigator
import com.neuifo.mangareptile.ui.base.presenter.ItemListFragmentPresenter
import com.neuifo.mangareptile.ui.detail.ExitTransition
import com.neuifo.mangareptile.ui.home.cell.ComicUpdateCell
import com.neuifo.mangareptile.utils.ToastUtil
import io.reactivex.Observable

class UpdateComicListFragment :
    ItemListFragmentPresenter<UpdateComicListDelegate, UpdateComicListDelegate.UpdateComicListDelegateCallBack, String, ComicUpdateCell, MutableList<ComicUpdate>>() {


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
        initialData.addAll(data)
        return initialData
    }


    override fun onInterceptListItemClick(position: Int, item: ComicUpdateCell): Boolean {
        val options = YcShareElement.buildOptionsBundle(requireActivity(), item)
        Navigator.jumpToComicDetail(
            requireActivity(),
            item.comicUpdate.id,
            item.comicUpdate.cover,
            options
        ) { resultCode, data ->
            YcShareElement.onActivityReenter(requireActivity(), resultCode, data,
                IShareElementSelector { list: MutableList<ShareElementInfo<*>> ->
                    val shareElementInfo = list[0]
                })
        }
        return super.onInterceptListItemClick(position, item)
    }

    override fun getApiObservable(
        id: String,
        page: Int,
        size: Int
    ): Observable<MutableList<ComicUpdate>> = API.DMZJ_Dmzj.getLastestList(page)

    override val initialRequestId: String
        get() = ""

    override val viewCallback: UpdateComicListDelegate.UpdateComicListDelegateCallBack
        get() = UpdateComicListFragmentCallback()


    private inner class UpdateComicListFragmentCallback : ItemListDelegateCallbackImpl(),
        UpdateComicListDelegate.UpdateComicListDelegateCallBack {

    }
}