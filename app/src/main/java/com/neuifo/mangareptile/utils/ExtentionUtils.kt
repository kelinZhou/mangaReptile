package com.neuifo.mangareptile.utils

import android.view.View

fun String?.notNull(): String {
    return if (this.isNullOrEmpty()) "" else this
}

fun Boolean.getSubscribedText(): String {
    return if (this) "取消订阅" else "订阅漫画"
}

fun Boolean.getSubscribedAction(): String {
    return if (this) "订阅成功" else "取消订阅成功"
}

fun Boolean.getVisiable(): Int {
    return if (this) View.VISIBLE else View.GONE
}

fun Int.getSubscribed(): Boolean {
    return this == 1
}