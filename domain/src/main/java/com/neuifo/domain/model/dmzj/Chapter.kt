package com.neuifo.domain.model.dmzj

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.neuifo.domain.model.base.TagModel


/**
chapter_id	Integer	99726
chapter_title	String	48.2话
updatetime	Integer	1584695737
filesize	Integer	3294302
chapter_order	Integer	640

chapter_id	Integer	99726
comic_id	Integer	13930
title	String	第48.2话
chapter_order	Integer	640
direction	Integer	1
page_url	Array
picnum	Integer	18
comment_count	Integer	27
 *
 *
 */
class Chapter(
    @SerializedName("chapter_id")
    var chapterId: Long,
    @SerializedName(value = "tag", alternate = ["chapter_title", "title"])
    var chapterTitle: String,
    @SerializedName("updatetime")
    var updateTime: Long,
    @SerializedName("filesize")
    var filesize: Long,
    @SerializedName("chapter_order")
    var chapterOrder: Long,
    @SerializedName("page_url")
    var pageUrl: MutableList<String>,
    @SerializedName("picnum")
    var pageNums: Long
) : TagModel, Parcelable {
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

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readLong(),
        source.readLong(),
        source.readLong(),
        arrayListOf<String>().apply {
            source.readList(this, String::class.java.classLoader)
        },
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(chapterId)
        writeString(chapterTitle)
        writeLong(updateTime)
        writeLong(filesize)
        writeLong(chapterOrder)
        writeStringList(pageUrl)
        writeLong(pageNums)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Chapter> = object : Parcelable.Creator<Chapter> {
            override fun createFromParcel(source: Parcel): Chapter = Chapter(source)
            override fun newArray(size: Int): Array<Chapter?> = arrayOfNulls(size)
        }
    }
}