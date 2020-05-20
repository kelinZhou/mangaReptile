package com.neuifo.domain.model.dmzj

import com.google.gson.annotations.SerializedName

class Chapter(
    @SerializedName("chapter_id")
    var chapterId: Long,
    @SerializedName("chapter_title")
    var chapterTitle: String,
    @SerializedName("updatetime")
    var updateTime: Long,
    @SerializedName("filesize")
    var filesize: Long,
    @SerializedName("chapter_order")
    var chapterOrder: Long
)