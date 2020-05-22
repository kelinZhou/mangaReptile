package com.neuifo.domain.model.dmzj

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.neuifo.domain.model.base.TagModel

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
) : Parcelable, TagModel {
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

    private var picked: Boolean = false
    private var read: Boolean = false

    override var isSelected: Boolean
        get() = picked
        set(value) {
            picked = value
        }
    override var tagString: String
        get() = chapterTitle
        set(value) {
            chapterTitle = value
        }
    override var showMarker: Boolean
        get() = read
        set(value) {
            read = value
        }
}