package com.neuifo.domain.model

abstract class DownloadProgressListener(var downloadIndex: Int) {

    /***
     * progress=(bytesRead / (contentLength / 100f)).toInt()
     */
    abstract fun update(
        bytesRead: Long,
        contentLength: Long,
        done: Boolean
    )
}