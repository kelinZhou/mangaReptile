package com.neuifo.mangareptile.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.neuifo.domain.model.base.WarpData
import com.neuifo.mangareptile.ui.Navigator
import com.neuifo.mangareptile.ui.base.presenter.ItemListFragmentPresenter
import com.neuifo.mangareptile.ui.detail.cell.ComicChapterCell
import io.reactivex.Observable

class ChapterListFragment :
    ItemListFragmentPresenter<ChapterListDelegate, ChapterListDelegate.ChapterListDelegateCallBack, Any, ComicChapterCell, WarpData>() {

    override val setupMode: Int
        get() = SETUP_NO_PROXY


    override val isEnablePage: Boolean
        get() = false


    companion object {
        private const val CHAPTER_DATA = "CHAPTER_DATA"
        private const val CHAPTER_TITLE = "CHAPTER_TITLE"
        private const val COMIC_ID = "COMIC_ID"

        fun setData(intent: Intent, comicId: Long, title: String, warpData: WarpData) {
            intent.putExtra(CHAPTER_DATA, warpData)
            intent.putExtra(CHAPTER_TITLE, title)
            intent.putExtra(COMIC_ID, comicId)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data =
            arrayListOf<ComicChapterCell>().apply {
                val data = requireArguments().getParcelable(CHAPTER_DATA) as WarpData
                add(ComicChapterCell(data, true) { tag ->
                    Navigator.jumpToGallery(
                        requireActivity(),
                        requireArguments().getLong(COMIC_ID),
                        tag.chapterId,
                        data
                    )
                })
            }

        viewDelegate?.setInitialData(data)
    }


    override fun transformUIData(
        page: Int,
        itemList: MutableList<ComicChapterCell>,
        data: WarpData
    ) {
    }

    override fun onInterceptListItemClick(position: Int, item: ComicChapterCell): Boolean {
        return super.onInterceptListItemClick(position, item)
    }

    override fun onRealResume() {
        super.onRealResume()
        setTitle(requireArguments().getString(CHAPTER_TITLE), true)
    }

    override fun getApiObservable(id: Any, page: Int, size: Int): Observable<WarpData> {
        return Observable.just(WarpData("", mutableListOf()))
    }

    override val initialRequestId: Any
        get() = ""

    override val viewCallback: ChapterListDelegate.ChapterListDelegateCallBack
        get() = ChapterListFragmentCallback()


    private inner class ChapterListFragmentCallback : ItemListDelegateCallbackImpl(),
        ChapterListDelegate.ChapterListDelegateCallBack

}