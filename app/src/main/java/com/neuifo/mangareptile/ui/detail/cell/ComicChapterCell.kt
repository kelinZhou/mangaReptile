package com.neuifo.mangareptile.ui.detail.cell

import android.view.View
import com.neuifo.data.Constansts
import com.neuifo.domain.model.base.WarpData
import com.neuifo.domain.model.dmzj.Chapter
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.ui.base.listcell.SimpleCell
import kotlinx.android.synthetic.main.item_comic_chapter.view.*


class ComicChapterCell(
    var chapter: WarpData,
    var chapterClick: ((tag: Chapter) -> Unit)? = null
) :
    SimpleCell() {

    val tempChapter: MutableList<Chapter> by lazy {
        arrayListOf<Chapter>()
    }

    override fun onBindData(iv: View) {
        tempChapter.clear()
        if (chapter.data.size > Constansts.MAX_CHAPTER_SIZE) {
            chapter.data.mapIndexed { index, chapter ->
                if (index <= Constansts.MAX_CHAPTER_SIZE) {
                    tempChapter.add(chapter)
                }
            }
            tempChapter.add(Chapter.createSample())
        } else {
            tempChapter.addAll(chapter.data)
        }
        iv.item_comic_chapter_title.text = "*${chapter.name}*"
        iv.item_comic_chapter_tags.setOnTagClickListener { _, tagView, tag, _ ->
            tag as Chapter
            if (!tag.isSelected) {
                tag.isSelected = true
            }
            if (tag.showMarker) {
                tag.showMarker = false
            }
            iv.item_comic_chapter_tags.setTags(tempChapter)
            chapterClick?.invoke(tag)
        }
        iv.item_comic_chapter_tags.setTags(tempChapter)
    }

    override val itemLayoutRes: Int
        get() = R.layout.item_comic_chapter

}