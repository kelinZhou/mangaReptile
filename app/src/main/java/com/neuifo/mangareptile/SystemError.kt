package com.neuifo.mangareptile

/**
 * **描述:** 系统错误码。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/3/25  3:31 PM
 *
 * **版本:** v 1.0.0
 */
enum class SystemError(val code: Int, val text: String) {
    /**
     * 表示在启动一个新的页面时本来需要传入必要参数，但是在新的页面启动时却没有获得到必要参数。
     */
    NULL_ARGUMENT(500, "参数错误:500，请联系客服~"),
    /**
     * 表示在启动一个新的页面时缺少了必要的页面类型标识。
     */
    UNKNOWN_TARGET_PAGE(501, "页面类型错误:501，请联系客服~"),
    /**
     * 表示在跳转到某一页面时并未真正的对页面类型为处理。
     */
    TARGET_PAGE_TYPE_NOT_HANDLER(502, "页面类型错误:502, 请联系客服~")
}