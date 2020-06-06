package com.neuifo.data.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.neuifo.domain.model.dmzj.Comment
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.Type


class CommentConverter internal constructor(
    private val gson: Gson,
    private val adapter: TypeAdapter<Any>,
    private val type: Type
) : Converter<ResponseBody, MutableList<Comment>> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): MutableList<Comment> {
        val response = value.string()

        val data = mutableListOf<Comment>()
        val jsonObject = JSONObject(response)
        val commentIds = jsonObject.getJSONArray("commentIds")
        if (commentIds.length() == 0) {
            return data
        }
        val comments = jsonObject.getJSONObject("comments")
        var tempList = mutableListOf<Comment>()
        for (i: Int in 0 until commentIds.length()) {
            val split = (commentIds[i] as String).split(",")
            var tempCover = ""
            var tempName = ""
            tempList.clear()
            split.mapIndexed index@{ index, id ->
                var tempSource: JSONObject? = null
                try {
                    tempSource = comments.getJSONObject(id)
                } catch (e: Exception) {

                }
                if (tempSource == null) return@index
                val comment =
                    gson.fromJson(tempSource.toString(), Comment::class.java)
                if (index == 0) {
                    comment.index = 0
                    tempCover = comment.avatar_url
                    tempName = comment.nickname
                    comment.isBottom = split.size == 1
                } else {
                    comment.index = split.size - index
                    comment.isBottom = index == 1
                    comment.parentName = tempName
                    comment.parentUrl = if (tempCover.isEmpty()) comment.avatar_url else tempCover
                }
                tempList.add(comment)
            }
            tempList.reverse()
            data.addAll(tempList)
        }
        return data
    }

}