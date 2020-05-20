package com.neuifo.domain.model.auto

import androidx.annotation.Nullable
import com.google.auto.value.AutoValue
import com.neuifo.domain.beans.Comic_detail_info
import org.jetbrains.annotations.NotNull

@AutoValue
abstract class AutoValueComic:Comic_detail_info {

    companion object {
        fun create(
            @NotNull id: Long,
            @NotNull title: String,
            @Nullable authors: String?,
            @Nullable types: String?,
            @Nullable cover: String?,
            @Nullable status: String?,
            @Nullable latest_update_chapter_name: String?,
            @Nullable latest_update_chapter_id: Long?,
            @Nullable latest_update_time: Long?,
            @Nullable last_read_name: String?,
            @Nullable last_read_chapter_id: Long?
        ): AutoValueComic {
            TODO("暂不实现")
            /*return AutoValue_AutoValueComic(
                id,
                title,
                authors,
                types,
                cover,
                status,
                latest_update_chapter_name,
                latest_update_chapter_id,
                latest_update_time,
                last_read_name,
                last_read_chapter_id
            )*/
        }
    }
}