package com.neuifo.domain.model.dmzj

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readLong(),
        source.readLong(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(chapterId)
        writeString(chapterTitle)
        writeLong(updateTime)
        writeLong(filesize)
        writeLong(chapterOrder)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Chapter> = object : Parcelable.Creator<Chapter> {
            override fun createFromParcel(source: Parcel): Chapter = Chapter(source)
            override fun newArray(size: Int): Array<Chapter?> = arrayOfNulls(size)
        }
    }
}