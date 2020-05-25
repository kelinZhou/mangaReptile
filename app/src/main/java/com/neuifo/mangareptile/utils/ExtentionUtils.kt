package com.neuifo.mangareptile.utils

import android.view.View

fun String?.notNull(): String {
    return if (this.isNullOrEmpty()) "" else this
}

fun Boolean.getVisiable(): Int {
    return if (this) View.VISIBLE else View.GONE
}