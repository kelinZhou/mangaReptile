package com.neuifo.mangareptile.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.neuifo.mangareptile.ui.detail.ComicDetailActivity

object Navigator {


    fun jumpToComicDetail(
        context: Activity,
        comicId: Long,
        bundle: Bundle,
        onFinishedEditor: (resultCode: Int, data: Intent) -> Unit
    ) {
        ComicDetailActivity.startToDetail(context, comicId, bundle, onFinishedEditor)
    }
}