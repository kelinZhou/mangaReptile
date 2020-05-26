package com.neuifo.domain.model.dmzj

import android.os.Parcel
import android.os.Parcelable
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

last_update_chapter_name	String	第73话

latest_update_chapter_name TEXT,
latest_update_chapter_id INTEGER,
last_read_name TEXT,
last_read_chapter_id INTEGER,
last_update_chapter_id	Integer	98598

 */
class ComicUpdate(
    var id: Long,
    @SerializedName(value = "title_tag", alternate = ["title", "name"])
    var title: String,
    var authors: String,
    var types: String,
    @SerializedName(value = "cover_tag", alternate = ["cover", "sub_img"])
    var cover: String,
    var status: String,
    @SerializedName(value = "chapter_name", alternate = ["last_update_chapter_name", "sub_update"])
    var latest_update_chapter_name: String,
    @SerializedName("last_update_chapter_id")
    var latest_update_chapter_id: Long,
    @SerializedName(value = "update_time", alternate = ["last_update_time", "sub_uptime"])
    var latest_update_time: Long,
    var last_read_name: String,
    var last_read_chapter_id: Long
) : Comparable<ComicUpdate>, Parcelable {
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

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readLong(),
        source.readLong(),
        source.readString(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(title)
        writeString(authors)
        writeString(types)
        writeString(cover)
        writeString(status)
        writeString(latest_update_chapter_name)
        writeLong(latest_update_chapter_id)
        writeLong(latest_update_time)
        writeString(last_read_name)
        writeLong(last_read_chapter_id)
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

        @JvmField
        val CREATOR: Parcelable.Creator<ComicUpdate> = object : Parcelable.Creator<ComicUpdate> {
            override fun createFromParcel(source: Parcel): ComicUpdate = ComicUpdate(source)
            override fun newArray(size: Int): Array<ComicUpdate?> = arrayOfNulls(size)
        }
    }
}