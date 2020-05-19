package com.neuifo.mangareptile.utils

fun String?.notNull(): String {
    return if (this.isNullOrEmpty()) "" else this
}