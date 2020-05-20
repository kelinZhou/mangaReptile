package com.neuifo.mangareptile.ui.home

import com.neuifo.domain.model.dmzj.ComicUpdate
import com.neuifo.mangareptile.data.core.API
import com.neuifo.mangareptile.ui.home.cell.ComicUpdateCell
import com.neuifo.mangareptile.ui.base.presenter.ItemListFragmentPresenter
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