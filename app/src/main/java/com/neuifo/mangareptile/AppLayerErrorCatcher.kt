package com.neuifo.mangareptile

/**
 * **描述:** Data层。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/2/26  11:16 AM
 *
 * **版本:** v 1.0.0
 */
object AppLayerErrorCatcher {

    private var errorCatcher: ((Throwable) -> Unit)? = null

    fun catchError(l: (e: Throwable) -> Unit) {
        errorCatcher = l
    }

    fun throwException(e: Throwable) {
        errorCatcher?.invoke(e)
    }
}