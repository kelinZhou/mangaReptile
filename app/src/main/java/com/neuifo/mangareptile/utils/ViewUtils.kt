package com.neuifo.mangareptile.utils

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.JsonReader
import android.view.View
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.neuifo.domain.model.dmzj.ComicUpdate
import org.json.JSONArray
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object ViewUtils {

    fun decodeString(
        source: String,
        regx: String,
        color: String,
        size: Float = 1F
    ): SpannableString {
        return decodeString(source, arrayOf(regx), arrayOf(color), floatArrayOf(size), null, null)
    }

    fun decodeBoldString(source: String): SpannableString {
        return decodeBoldString(SpannableString(source), arrayOf(source))
    }

    fun decodeBoldString(source: SpannableString, regxs: Array<String>): SpannableString {
        val spannableString = SpannableString(source)
        for (i in regxs.indices) {
            val regx = regxs[i]
            val indexOf = spannableString.indexOf(regx, 0, true)
            if (indexOf >= 0) {
                spannableString.setSpan(
                    StyleSpan(Typeface.BOLD),
                    indexOf,
                    indexOf + regx.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannableString
    }

    /**
     * 注意数组长度是否匹配
     */
    @JvmOverloads
    fun decodeString(
        source: String,
        regxs: Array<String>,
        colors: Array<String>,
        size: FloatArray,
        draws: Array<Drawable>? = null,
        drawsposi: Array<IntArray>? = null
    ): SpannableString {

        val spannableString = SpannableString(source)

        for (i in regxs.indices) {
            val regx = regxs[i]
            val indexOf = source.indexOf(regx, 0, true)
            if (indexOf >= 0) {
                val stringSpan = ForegroundColorSpan(Color.parseColor(colors[i]))
                spannableString.setSpan(
                    stringSpan,
                    indexOf,
                    indexOf + regx.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                val s = size[i]
                if (s > 0) {
                    val relativeSizeSpan = RelativeSizeSpan(s)
                    spannableString.setSpan(
                        relativeSizeSpan,
                        indexOf,
                        indexOf + regx.length,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }

        if (draws != null && draws.size > 0) {
            for (i in draws.indices) {
                val drawable = draws[i]
                val posi = drawsposi!![i]
                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                val imageSpan = ImageSpan(drawable)
                spannableString.setSpan(
                    imageSpan,
                    posi[0],
                    posi[1],
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannableString
    }

    fun decodeString(source: String, tags: List<String>, bgColor: String): SpannableString {
        val spannableString = SpannableString(source)
        for (regx in tags) {
            val roundBackgroundColorSpan = RoundBackgroundColorSpan(
                Color.parseColor(bgColor),
                Color.parseColor("#4A4A4A")
            )
            spannableString.setSpan(
                roundBackgroundColorSpan,
                source.lastIndexOf(regx),
                source.lastIndexOf(regx) + regx.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        return spannableString
    }

    enum class CompoundOri {
        LEFT, RIGHT, TOP, BOTTOM
    }

    fun setCompoundNullDrawable(view: TextView) {
        view.setCompoundDrawables(null, null, null, null)
    }

    fun setCompoundDrawable(
        view: TextView,
        resId: Int,
        compoundOri: CompoundOri,
        drawablePadding: Int = 10
    ) {
        var drawable: Drawable? = null
        try {
            drawable = view.resources.getDrawable(resId)
        } catch (ignored: Exception) {
        }

        drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        when (compoundOri) {
            ViewUtils.CompoundOri.LEFT -> view.setCompoundDrawables(drawable, null, null, null)
            ViewUtils.CompoundOri.RIGHT -> view.setCompoundDrawables(null, null, drawable, null)
            ViewUtils.CompoundOri.TOP -> view.setCompoundDrawables(null, drawable, null, null)
            ViewUtils.CompoundOri.BOTTOM -> view.setCompoundDrawables(null, null, null, drawable)
        }
        view.compoundDrawablePadding = MeasureUtil.dp2px(view.context, drawablePadding.toFloat())
    }


    fun measureView(view: View) {
        val w = View.MeasureSpec.makeMeasureSpec(
            0,
            View.MeasureSpec.UNSPECIFIED
        )
        val h = View.MeasureSpec.makeMeasureSpec(
            0,
            View.MeasureSpec.UNSPECIFIED
        )
        view.measure(w, h)
    }


    fun decodeJson(inputStream: InputStream): MutableList<ComicUpdate> {
        //val reader = JsonReader(InputStreamReader(inputStream, "UTF-8"))
        val gson = GsonBuilder().create()
        val jsonArray: JSONArray
        var reader: BufferedReader? = null
        val jsonStrs = StringBuilder()
        try {
            if (inputStream == null) {
                return mutableListOf()
            }
            val inputStreamReader =
                InputStreamReader(inputStream, "UTF-8")
            reader = BufferedReader(inputStreamReader)
            var tempStr: String? = null
            while (reader.readLine().also { tempStr = it } != null) {
                jsonStrs.append(tempStr)
            }
            reader.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        jsonArray = JSONArray(jsonStrs.toString().trim())

        val result = mutableListOf<ComicUpdate>()
        for (i: Int in 0 until jsonArray.length()) {
            val data = gson.fromJson(jsonArray.getJSONObject(i).toString(), DmzjDb::class.java)
            result.add(
                ComicUpdate(
                    data.id,
                    data.title,
                    data.authors.joinToString("/"),
                    data.types.joinToString { "/" },
                    data.cover,
                    data.status.joinToString { "/" },
                    data.last_update_chapter_name,
                    data.last_update_chapter_id,
                    data.last_updatetime,
                    "",
                    0L
                )
            )
        }
        return result
    }

    data class DmzjDb(
        val authors: List<String>,
        val cover: String,
        val id: Long,
        val last_update_chapter_id: Long,
        val last_update_chapter_name: String,
        val last_updatetime: Long,
        val status: List<String>,
        val title: String,
        val types: List<String>
    )
}