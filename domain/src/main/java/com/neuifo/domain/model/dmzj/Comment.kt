package com.neuifo.domain.model.dmzj

import android.os.Parcel
import android.os.Parcelable
import com.neuifo.domain.model.base.BaseComment

class Comment(
    var avatar_url: String,
    var content: String,
    var create_time: Long,
    var id: String,
    var is_goods: String,
    var like_amount: String,
    var nickname: String,
    var obj_id: String,
    var origin_comment_id: String,
    var sender_uid: String,
    var sex: String,
    var upload_images: String,
    override var index: Int,
    override var isBottom: Boolean,
    override var parentUrl: String,
    override var parentName: String
) : BaseComment, Parcelable ,Comparable<Comment>{



    override fun compareTo(other: Comment): Int {
        return if (this.create_time > other.create_time) -1 else 1
    }

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt(),
        1 == source.readInt(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(avatar_url)
        writeString(content)
        writeLong(create_time)
        writeString(id)
        writeString(is_goods)
        writeString(like_amount)
        writeString(nickname)
        writeString(obj_id)
        writeString(origin_comment_id)
        writeString(sender_uid)
        writeString(sex)
        writeString(upload_images)
        writeInt(index)
        writeInt((if (isBottom) 1 else 0))
        writeString(parentUrl)
        writeString(parentName)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Comment

        if (id != other.id) return false
        if (index != other.index) return false
        if (isBottom != other.isBottom) return false
        if (parentUrl != other.parentUrl) return false
        if (parentName != other.parentName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + index
        result = 31 * result + isBottom.hashCode()
        result = 31 * result + parentUrl.hashCode()
        result = 31 * result + parentName.hashCode()
        return result
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Comment> = object : Parcelable.Creator<Comment> {
            override fun createFromParcel(source: Parcel): Comment = Comment(source)
            override fun newArray(size: Int): Array<Comment?> = arrayOfNulls(size)
        }
    }
}