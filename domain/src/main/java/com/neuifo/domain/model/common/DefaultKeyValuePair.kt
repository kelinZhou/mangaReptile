package com.neuifo.domain.model.common

import com.neuifo.domain.model.common.KeyValuePair


/**
 * **描述:** KeyValuePair的默认实现
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019-10-15  17:56
 *
 * **版本:** v 1.0.0
 */
class DefaultKeyValuePair(override val key: String, override val value: String) : KeyValuePair