package com.neuifo.domain.model.dmzj

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class TagContent(
    @SerializedName("tag_id")
    var id: Long,
    @SerializedName("tag_name")
    var name: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TagContent> = object : Parcelable.Creator<TagContent> {
            override fun createFromParcel(source: Parcel): TagContent = TagContent(source)
            override fun newArray(size: Int): Array<TagContent?> = arrayOfNulls(size)
        }
    }
}