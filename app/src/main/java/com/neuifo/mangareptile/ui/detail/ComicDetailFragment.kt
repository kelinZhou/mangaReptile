package com.neuifo.mangareptile.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.hw.ycshareelement.YcShareElement
import com.neuifo.data.cache.CacheFactory
import com.neuifo.data.domain.utils.LogHelper
import com.neuifo.domain.model.dmzj.Chapter
import com.neuifo.domain.model.dmzj.ComicDetail
import com.neuifo.domain.model.dmzj.ComicDetailWarpper
import com.neuifo.mangareptile.data.core.API
import com.neuifo.mangareptile.data.proxy.ProxyFactory
import com.neuifo.mangareptile.ui.Navigator
import com.neuifo.mangareptile.ui.base.listcell.SimpleCell
import com.neuifo.mangareptile.ui.base.presenter.ItemListFragmentPresenter
import com.neuifo.mangareptile.ui.detail.cell.ComicChapterCell
import com.neuifo.mangareptile.utils.statusbar.StatusBarHelper
import io.reactivex.Observable

class ComicDetailFragment :
    ItemListFragmentPresenter<ComicDetailDelegate, ComicDetailDelegate.ComicDetailDelegateCallback, Long, SimpleCell, ComicDetailWarpper>() {


    companion object {
        private const val COMIC_DETAIL_ID = "comic_detail_id"
        private const val COMIC_DETAIL_COVER = "comic_detail_cover"
        private const val COMIC_READ_CHAPTER_NAME = "comic_read_chapter_name"


        fun setComicId(intent: Intent, comicId: Long, cover: String) {
            intent.putExtra(COMIC_DETAIL_ID, comicId)
            intent.putExtra(COMIC_DETAIL_COVER, cover)
        }

        fun getResultIntent(data: String): Intent {
            return Intent().apply { putExtra(COMIC_READ_CHAPTER_NAME, data) }
        }

        fun getResultData(intent: Intent): String {
            return intent.getStringExtra(COMIC_READ_CHAPTER_NAME)
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDelegate?.setInitShowData(requireArguments().getString(COMIC_DETAIL_COVER))
        YcShareElement.setEnterTransitions(requireActivity(), viewDelegate)
        YcShareElement.startTransition(requireActivity())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //requireActivity().window.sharedElementReturnTransition = ExitTransition()
        }
    }

    private val comicDetail: ComicDetail by lazy {
        ComicDetail.createShareData(
            initialRequestId, requireArguments().getString(COMIC_DETAIL_COVER)
        )
    }

    override fun onRealResume() {
        super.onRealResume()
        //requireActivity().window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        hideToolbar()
        StatusBarHelper.setStatusBarDarkMode(requireActivity())
    }

    override fun onDestroyView() {
        YcShareElement.finishAfterTransition(requireActivity(), viewDelegate)
        super.onDestroyView()
    }

    override fun onInterceptBackPressed(): Boolean {
        requireActivity().setResult(
            Activity.RESULT_OK,
            getResultIntent(
                CacheFactory.instance.comicCache?.queryLastReadChapterName(initialRequestId) ?: ""
            )
        )
        return super.onInterceptBackPressed()
    }

    override fun transformUIData(
        page: Int,
        itemList: MutableList<SimpleCell>,
        data: ComicDetailWarpper
    ) {
        if (page == 1) {
            viewDelegate?.updateComicInfo(data.comicDetail)
            data.comicDetail.chapters.map { warpData ->
                itemList.add(ComicChapterCell(warpData) click@{ chapter ->
                    if (chapter.chapterId == Chapter.SAMPLE) {
                        Navigator.jumpToChapterDetail(
                            requireActivity(),
                            comicDetail.id,
                            data.comicDetail.title,
                            warpData
                        )
                        return@click
                    }
                    Navigator.jumpToGallery(
                        requireActivity(),
                        comicDetail.id,
                        chapter.chapterId,
                        warpData
                    )
                })
            }
        }

        //YcShareElement.setEnterTransitions(requireActivity(), comicDetailHeadCell)
        //itemList.add(comicDetailHeadCell)
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
        ComicDetailDelegate.ComicDetailDelegateCallback {
        override fun getComicCover(): ComicDetail = comicDetail

        override fun jumpToAuth(id: String) {
            LogHelper.system.e("作者id${id}")
        }

        override fun exit() {
            requireActivity().finish()
        }

        override fun subscrbie(flag: Boolean) {
            ProxyFactory.createIdProxy<Long, Boolean> {
                if (flag) API.DMZJ_Dmzj.subscribeComic(it) else API.DMZJ_Dmzj.unSubscribeComic(it)
            }.onSuccess { _, _ ->
                viewDelegate?.updateSubscribeText(flag)
            }.onFailed { _, _ ->
                viewDelegate?.updateSubscribeText(!flag)
            }.request(initialRequestId)
        }

        override fun downLoad() {
        }
    }

}