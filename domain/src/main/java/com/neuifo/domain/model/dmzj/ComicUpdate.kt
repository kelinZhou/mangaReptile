package com.neuifo.domain.model.dmzj

import com.google.gson.annotations.SerializedName

/**
 * id	Integer	22027
title	String	瑞克与莫蒂
islong	Integer	2
authors	String	ONI PRESS
types	String	科幻
cover	String	https://images.dmzj.com/webpic/1/ruikeyutimo.JPG
status	String	连载中
last_update_chapter_name	String	第47卷
last_update_chapter_id	Integer	98555
last_update_time	Integer	1583992455


latest_update_chapter_name TEXT,
latest_update_chapter_id INTEGER,
last_read_name TEXT,
last_read_chapter_id INTEGER,
last_update_chapter_id	Integer	98598

 */
class ComicUpdate(
    var id: Long,
    var title: String,
    var authors: String,
    var types: String,
    var cover: String,
    var status: String,
    @SerializedName("last_update_chapter_name")
    var latest_update_chapter_name: String,
    @SerializedName("last_update_chapter_id")
    var latest_update_chapter_id: Long,
    @SerializedName("last_updatetime")
    var latest_update_time: Long,
    var last_read_name: String,
    var last_read_chapter_id: Long
) : Comparable<ComicUpdate> {

    override fun compareTo(other: ComicUpdate): Int {
        return if (this.latest_update_time > other.latest_update_time) -1 else 1
    }

    fun updateContent(): String {
        return if (latest_update_chapter_name.isNullOrEmpty()) {
            "首页推荐"
        } else {
            "更新:${latest_update_chapter_name}"
        }
    }

    fun readContent(): String {
        return if (last_read_chapter_id == 0L) {
            "未读"
        } else {
            "看到:${last_read_name}"
        }
    }

    companion object {
        fun createReadInfo(
            id: Long,
            last_read_name: String?,
            last_read_chapter_id: Long?
        ): ComicUpdate {
            return ComicUpdate(
                id,
                "",
                "",
                "",
                "",
                "",
                "",
                0L,
                0L,
                last_read_name ?: "未读",
                last_read_chapter_id ?: 0L
            )
        }
    }
}