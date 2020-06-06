package com.neuifo.domain.model.base

interface BaseComment {
    val index: Int
    val isBottom: Boolean
    val parentUrl: String
    val parentName: String
}