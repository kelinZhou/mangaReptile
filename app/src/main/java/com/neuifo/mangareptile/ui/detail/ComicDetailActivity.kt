package com.neuifo.mangareptile.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hw.ycshareelement.YcShareElement
import com.kelin.okpermission.OkActivityResult
import com.neuifo.domain.model.base.WarpData
import com.neuifo.mangareptile.SystemError
import com.neuifo.mangareptile.ui.base.swip.SwipeBackFragmentActivity

class ComicDetailActivity : SwipeBackFragmentActivity() {


    override val enableSwipe: Boolean
        get() = false

    companion object {
        private const val COMIC_DETAIL = 0x100
        private const val COMIC_CHAPTER_DETAIL = 0x101

        fun startToDetail(
            context: Activity,
            comicId: Long,
            cover: String,
            bundle: Bundle,
            onFinishedEditor: (resultCode: Int, data: Intent) -> Unit
        ) {
            val intent = getJumpIntent(context, ComicDetailActivity::class.java, COMIC_DETAIL)
            ComicDetailFragment.setComicId(intent, comicId, cover)
            OkActivityResult.instance.startActivityForResult(
                context,
                intent,
                bundle
            ) { resultCode: Int, data: Intent, e: Exception? ->
                if (e == null && resultCode == Activity.RESULT_OK) {
                    onFinishedEditor(resultCode, data)
                }
            }
        }

        fun startToChapterDetail(
            context: Activity,
            comicId: Long,
            title: String,
            warpData: WarpData
        ) {
            val jumpIntent =
                getJumpIntent(context, ComicDetailActivity::class.java, COMIC_CHAPTER_DETAIL)
            ChapterListFragment.setData(jumpIntent, comicId, title, warpData)
            context.startActivity(jumpIntent)
        }

    }

    override fun getCurrentFragment(targetPage: Int, intent: Intent): Fragment {
        return when (targetPage) {
            COMIC_DETAIL -> {
                title = ""
                createFragmentByClass(ComicDetailFragment::class.java, intent)
            }
            COMIC_CHAPTER_DETAIL -> {
                title = ""
                createFragmentByClass(ChapterListFragment::class.java, intent)
            }
            else -> onJumpError(SystemError.TARGET_PAGE_TYPE_NOT_HANDLER)
        }
    }


    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        YcShareElement.onActivityReenter(this, resultCode, data) {}
    }

}