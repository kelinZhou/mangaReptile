package com.neuifo.mangareptile.data.proxy

/**
 * **描述:** 定义Proxy的拥有者。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019-07-18  15:44
 *
 * **版本:** v 1.0.0
 */
interface ProxyOwner {
    fun attachToOwner(proxy: UnBounder)
}