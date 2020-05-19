package com.neuifo.domain.model.base

import java.io.Serializable

/**
 * <strong>描述: </strong> 标签模型。
 * <p><strong>创建人: </strong> kelin
 * <p><strong>创建时间: </strong> 2018/8/14  下午3:56
 * <p><strong>版本: </strong> v 1.0.0
 */
interface TagModel : Serializable {
    var isSelected: Boolean
    var tagString: String
    val showMarker: Boolean
}