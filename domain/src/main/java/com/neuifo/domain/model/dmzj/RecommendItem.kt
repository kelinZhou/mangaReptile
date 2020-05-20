package com.neuifo.domain.model.dmzj

import com.google.gson.annotations.SerializedName

class RecommendItem(
    var category_id: Long = 49,
    var titlt: String,
    var sort: Int,
    @SerializedName("data")
    var items: MutableList<ComicUpdate>
)