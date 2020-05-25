package com.neuifo.domain.model.dmzj

import com.neuifo.domain.model.DownloadProgressListener

class ChapterItem(
    var index: Int,
    var imageUrl: String,
    var downloadProgressListener: DownloadProgressListener?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChapterItem

        if (index != other.index) return false
        if (imageUrl != other.imageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = index
        result = 31 * result + imageUrl.hashCode()
        return result
    }
}