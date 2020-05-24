package com.neuifo.domain.model.dmzj

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.neuifo.domain.model.base.WarpData

class ComicDetail(
    var id: Long,
    var title: String,
    var authors: MutableList<TagContent>,
    var types: MutableList<TagContent>,
    var cover: String,
    var status: MutableList<TagContent>,
    //subscribe_num	Integer	129
    @SerializedName("subscribe_num")
    var subscribeNum: Long,//订阅人数
    var chapters: MutableList<WarpData>,
    @SerializedName("last_update_chapter_name")
    var latest_update_chapter_name: String,
    @SerializedName("last_update_chapter_id")
    var latest_update_chapter_id: Long,
    @SerializedName("last_updatetime")
    var latest_update_time: Long,
    var last_read_name: String,
    var description: String,
    var last_read_chapter_id: Long
    //var comment:MutableList<Comment>
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.createTypedArrayList(TagContent.CREATOR),
        source.createTypedArrayList(TagContent.CREATOR),
        source.readString(),
        mutableListOf(),
        source.readLong(),
        //source.createTypedArrayList(WarpData.CREATOR),
        mutableListOf(),
        source.readString(),
        source.readLong(),
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(title)
        writeTypedList(authors)
        writeTypedList(types)
        writeString(cover)
        //writeString(status)
        writeLong(subscribeNum)
        //writeTypedList(chapters)
        writeString(latest_update_chapter_name)
        writeLong(latest_update_chapter_id)
        writeLong(latest_update_time)
        writeString(last_read_name)
        writeString(description)
        writeLong(last_read_chapter_id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ComicDetail> = object : Parcelable.Creator<ComicDetail> {
            override fun createFromParcel(source: Parcel): ComicDetail = ComicDetail(source)
            override fun newArray(size: Int): Array<ComicDetail?> = arrayOfNulls(size)
        }


        fun createShareData(id: Long, cover: String): ComicDetail {
            return ComicDetail(
                id,
                "",
                mutableListOf(),
                mutableListOf(),
                cover,
                mutableListOf(),
                0L,
                mutableListOf(),
                "",
                0L,
                0L,
                "",
                "",
                0L
            )
        }
    }
}