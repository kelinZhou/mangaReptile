package com.neuifo.mangareptile.data

import com.hippo.image.Image
import com.neuifo.data.domain.utils.LogHelper
import com.neuifo.domain.model.dmzj.ChapterItem
import com.neuifo.mangareptile.data.core.API
import com.neuifo.mangareptile.data.proxy.ActionParameter
import com.neuifo.mangareptile.data.proxy.IdActionDataProxy
import com.neuifo.mangareptile.data.usecase.ApiIdRequestUseCase
import com.neuifo.mangareptile.data.usecase.UseCase
import io.reactivex.observers.DisposableObserver

class ImageDataProxy : IdActionDataProxy<ChapterItem, Image>() {

    private val callBacks: MutableSet<DisposableObserver<Image>> by lazy {
        HashSet<DisposableObserver<Image>>()
    }


    override fun createUseCase(
        chapterItem: ChapterItem,
        action: ActionParameter
    ): UseCase<Image> {
        return ApiIdRequestUseCase(chapterItem) { id ->
            API.DMZJ_Dmzj.getImage(id.imageUrl, id.downloadProgressListener).map { data ->
                LogHelper.system.e("图片解码中...")
                Image.decode(data, false)!!
            }
        }
    }

    fun request(chapterItem: ChapterItem) {
        request(ActionParameter.createInstance(), chapterItem)
    }

    override fun unbind(thorough: Boolean) {
        callBacks.clear()
        super.unbind(thorough)
    }

    @Synchronized
    fun onCancelRequest(chapterItem: ChapterItem?) {
        if (chapterItem == null) {
            return
        }
        val createCallback = createCallback(chapterItem, ActionParameter.createInstance())
        if (callBacks.contains(createCallback)) {
            var old = callBacks.find { it == createCallback }
            old?.dispose()
            callBacks.remove(old)
        }
    }

    override fun createCallback(
        id: ChapterItem,
        action: ActionParameter
    ): DisposableObserver<Image> {
        var dispose = super.createCallback(id, action)
        if (callBacks.contains(dispose)) {
            callBacks.add(dispose)
        }
        return dispose
    }

}