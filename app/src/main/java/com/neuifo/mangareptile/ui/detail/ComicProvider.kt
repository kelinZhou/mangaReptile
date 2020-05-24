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


class ComicProvider(var comicId: Long, var startChapter: Long, var chapter: WarpData) :
    GalleryProvider(),
    ProxyOwner {

    private val chapterRequester: IdDataProxy<Long, Chapter> by lazy {
        ProxyFactory.createIdProxy<Long, Chapter> { id ->
            API.DMZJ_Dmzj.getChapter(comicId, id)
        }.bind(this)
            .onSuccess { id, data ->
                currentChapter = data
                onRequest(0)
            }
            .onFailed { id, e ->
                e.displayMessage
            }
    }

    private val imageRequester: ImageDataProxy by lazy {
        ImageDataProxy()
    }

    var currentChapter: Chapter? = null
    var errorMsg: String = ""



    override fun start() {
        super.start()
        LogHelper.system.e("start")
        chapterRequester.request(startChapter)
    }

    override fun stop() {
        super.stop()
        LogHelper.system.e("stop")
        imageRequester.unbind(true)
        chapterRequester.unbind(true)
    }


    private fun createRequestByIndex(
        index: Int,
        onDownloadProgressListener: DownloadProgressListener? = null
    ): ChapterItem? {
        var url: String? = currentChapter?.pageUrl?.get(index) ?: return null
        return ChapterItem(index, url!!, onDownloadProgressListener)
    }

    override fun getError(): String = errorMsg

    override fun onCancelRequest(index: Int) {
        LogHelper.system.e("onCancelRequest")
        imageRequester.onCancelRequest(createRequestByIndex(index))
    }

    override fun onForceRequest(index: Int) {
        LogHelper.system.e("onForceRequest")
        onCancelRequest(index)
        onRequest(index)
    }

    override fun size(): Int = currentChapter?.pageNums?.toInt() ?: STATE_ERROR

    override fun onRequest(index: Int) {
        LogHelper.system.e("onRequest")
        val request = createRequestByIndex(index, object : DownloadProgressListener(index) {
            override fun update(
                downloadIdentifier: String?,
                bytesRead: Long,
                contentLength: Long,
                done: Boolean
            ) {
                LogHelper.system.e("progress:" + bytesRead / (contentLength / 100f))
                notifyPagePercent(downloadIndex, bytesRead / (contentLength / 100f))
            }
        })
        if (request == null) {
            notifyPageFailed(index, "请求参数生成失败。。")
            return
        }
        imageRequester.bind(this, object : IdDataProxy.IdDataCallback<ChapterItem, Image>() {
            override fun onSuccess(id: ChapterItem, data: Image) {
                LogHelper.system.e("图片解码成功...")
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