package com.neuifo.domain.model.dmzj

import com.google.gson.annotations.SerializedName
import com.neuifo.domain.model.base.HttpResult
import com.neuifo.domain.model.common.BaseKeyValuePair

class ComicDetail(
    var id: Long,
    var title: String,
    var authors: MutableList<BaseKeyValuePair<Long, String>>,
    var types: MutableList<BaseKeyValuePair<Long, String>>,
    var cover: String,
    var status: String,
    //subscribe_num	Integer	129
    @SerializedName("subscribe_num")
    var subscribeNum: Long,
    private var chapters: HttpResult<MutableList<Chapter>>,
    @SerializedName("last_update_chapter_name")
    var latest_update_chapter_name: String,
    @SerializedName("last_update_chapter_id")
    var latest_update_chapter_id: Long,
    @SerializedName("last_updatetime")
    var latest_update_time: Long,
    var last_read_name: String,
    var last_read_chapter_id: Long
) {

    fun getChapters(): MutableList<Chapter> {
        return if (this.chapters.isEmpty) {
            arrayListOf()
        } else {
            this.chapters.data
        }
    }
}