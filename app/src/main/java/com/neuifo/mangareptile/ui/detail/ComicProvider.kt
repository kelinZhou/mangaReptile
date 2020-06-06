package com.neuifo.mangareptile.ui.detail

import com.hippo.glgallery.GalleryProvider
import com.hippo.image.Image
import com.neuifo.data.domain.utils.LogHelper
import com.neuifo.domain.exception.ApiException
import com.neuifo.domain.model.DownloadProgressListener
import com.neuifo.domain.model.base.WarpData
import com.neuifo.domain.model.dmzj.Chapter
import com.neuifo.domain.model.dmzj.ChapterItem
import com.neuifo.mangareptile.data.ImageDataProxy
import com.neuifo.mangareptile.data.core.API
import com.neuifo.mangareptile.data.proxy.IdDataProxy
import com.neuifo.mangareptile.data.proxy.ProxyFactory
import com.neuifo.mangareptile.data.proxy.ProxyOwner
import com.neuifo.mangareptile.data.proxy.UnBounder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class ComicProvider(var comicId: Long, var currentChapter: Chapter) :
    GalleryProvider(),
    ProxyOwner {


    private val imageRequester: ImageDataProxy by lazy {
        ImageDataProxy()
    }

    var errorMsg: String = ""


    override fun stop() {
        super.stop()
        imageRequester.unbind(true)
    }


    private fun createRequestByIndex(
        index: Int,
        onDownloadProgressListener: DownloadProgressListener? = null
    ): ChapterItem? {
        var url: String? = currentChapter.pageUrl[index] ?: return null
        return ChapterItem(index, url!!, onDownloadProgressListener)
    }

    override fun getError(): String = errorMsg

    override fun onCancelRequest(index: Int) {
        imageRequester.onCancelRequest(createRequestByIndex(index))
    }

    override fun onForceRequest(index: Int) {
        onCancelRequest(index)
        onRequest(index)
    }

    override fun size(): Int = currentChapter.pageNums.toInt()

    override fun onRequest(index: Int) {
        LogHelper.system.e("onRequest")
        notifyPageWait(index)
        val request = createRequestByIndex(index, object : DownloadProgressListener(index) {

            override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                var progress = DecimalFormat("######0.0").format(bytesRead / (contentLength / 100f))
//                GlobalScope.launch {
//                    notifyPagePercent(downloadIndex, progress.toFloat())
//                }
            }
        })
        if (request == null) {
            notifyPageFailed(index, "请求参数生成失败。。")
            return
        }
        imageRequester.bind(this, object : IdDataProxy.IdDataCallback<ChapterItem, Image>() {
            override fun onSuccess(id: ChapterItem, data: Image) {
                notifyPageSucceed(id.index, data)
            }

            override fun onFailed(id: ChapterItem, e: ApiException) {
                errorMsg = e.displayMessage
                notifyPageFailed(id.index, error)
                LogHelper.system.e(e.displayMessage)
            }

        })
        imageRequester.request(request)
    }

    override fun attachToOwner(proxy: UnBounder) {
    }

}