package com.neuifo.domain.model.base

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.neuifo.domain.model.dmzj.Chapter

class WarpData(
    @SerializedName("title")
    var name: String,
    var data: MutableList<Chapter>
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        arrayListOf<Chapter>().apply {
            source.readList(this, Chapter::class.java.classLoader)
        }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeList(data)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<WarpData> = object : Parcelable.Creator<WarpData> {
            override fun createFromParcel(source: Parcel): WarpData = WarpData(source)
            override fun newArray(size: Int): Array<WarpData?> = arrayOfNulls(size)
        }
    }
}