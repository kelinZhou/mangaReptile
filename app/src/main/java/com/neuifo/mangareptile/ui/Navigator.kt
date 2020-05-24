package com.neuifo.mangareptile.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.neuifo.domain.model.base.WarpData
import com.neuifo.domain.model.dmzj.Chapter
import com.neuifo.mangareptile.ui.detail.ComicDetailActivity
import com.neuifo.mangareptile.ui.detail.ComicReadActivity

object Navigator {


    fun jumpToComicDetail(
        context: Activity,
        comicId: Long,
        cover: String,
        bundle: Bundle,
        onFinishedEditor: (resultCode: Int, data: Intent) -> Unit
    ) {
        ComicDetailActivity.startToDetail(context, comicId, cover, bundle, onFinishedEditor)
    }

    fun jumpToGallery(
        context: Activity,
        comicId: Long,
        chapterId: Long,
        chapter: WarpData
    ) {
        val intent = Intent(context, ComicReadActivity::class.java)
        ComicReadActivity.setData(intent, comicId, chapterId, chapter)
        context.startActivity(intent)
    }
}