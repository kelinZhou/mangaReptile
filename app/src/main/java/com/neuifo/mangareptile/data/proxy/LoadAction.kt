package com.neuifo.mangareptile.data.proxy

/**
 * **描述:** 用户请求数据的动作。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/4/3  1:48 PM
 *
 * **版本:** v 1.0.0
 */
enum class LoadAction {
    /**
     * 没有数据load。对于分页的，load总是第一页的数据
     */
    LOAD,
    /**
     * load失败，retry（这个不叫refresh！！！！）。对于分页的，load总是第一页的数据
     */
    RETRY,
    /**
     * 已经load成功，再次load。对于分页的，load总是第一页的数据
     */
    REFRESH,
    /**
     * 加载更多(分页加载)。
     */
    LOAD_MORE
}