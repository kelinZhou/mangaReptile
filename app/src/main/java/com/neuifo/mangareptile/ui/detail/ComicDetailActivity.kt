package com.neuifo.mangareptile.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.kelin.okpermission.OkActivityResult
import com.neuifo.mangareptile.SystemError
import com.neuifo.mangareptile.ui.base.swip.SwipeBackFragmentActivity

class ComicDetailActivity : SwipeBackFragmentActivity() {

    companion object {
        private const val COMIC_DETAIL = 0x100


        fun startToDetail(
            context: Activity,
            comicId: Long,
            bundle: Bundle,
            onFinishedEditor: (resultCode: Int, data: Intent) -> Unit
        ) {
            val intent = getJumpIntent(context, ComicDetailActivity::class.java, COMIC_DETAIL)
            ComicDetailFragment.setComicId(intent, comicId)
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

    }


    override fun getCurrentFragment(targetPage: Int, intent: Intent): Fragment {
        return when (targetPage) {
            COMIC_DETAIL -> {
                title = ""
                createFragmentByClass(ComicDetailFragment::class.java, intent)
            }
            else -> onJumpError(SystemError.TARGET_PAGE_TYPE_NOT_HANDLER)
        }
    }

}