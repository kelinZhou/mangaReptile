package com.neuifo.mangareptile.ui.detail.cell

import android.view.View
import com.neuifo.domain.model.base.WarpData
import com.neuifo.domain.model.dmzj.Chapter
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.ui.base.listcell.SimpleCell
import kotlinx.android.synthetic.main.item_comic_chapter.view.*


class ComicChapterCell(
    var chapter: WarpData<Chapter>,
    var chapterClick: ((tag: Chapter) -> Unit)? = null
) :
    SimpleCell() {

    override fun onBindData(iv: View) {
        iv.item_comic_chapter_title.text = "*${chapter.name}*"
        iv.item_comic_chapter_tags.setOnTagClickListener { _, tagView, tag, _ ->
            tag as Chapter
            if (!tag.isSelected) {
                tag.isSelected = true
            }
            if (tag.showMarker) {
                tag.showMarker = false
            }
            iv.item_comic_chapter_tags.setTags(chapter.data)
            chapterClick?.invoke(tag)
        }
        iv.item_comic_chapter_tags.setTags(chapter.data)
    }

    override val itemLayoutRes: Int
        get() = R.layout.item_comic_chapter

}