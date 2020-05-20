package com.neuifo.domain.model.base

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class WarpData<T>(
    @SerializedName("title")
    var name: String,
    var data: MutableList<T>
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        ArrayList<T>().apply { source.readList(this, ArrayList::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeList(data)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<WarpData<Any>> = object : Parcelable.Creator<WarpData<Any>> {
            override fun createFromParcel(source: Parcel): WarpData<Any> = WarpData(source)
            override fun newArray(size: Int): Array<WarpData<Any>?> = arrayOfNulls(size)
        }
    }
}