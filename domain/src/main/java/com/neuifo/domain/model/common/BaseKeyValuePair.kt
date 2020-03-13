package com.neuifo.domain.model.common

import java.io.Serializable

interface BaseKeyValuePair<T, V> : Serializable {
    val key: T
    val value: V
}