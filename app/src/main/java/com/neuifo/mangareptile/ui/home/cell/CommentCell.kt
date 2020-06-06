package com.neuifo.mangareptile.ui.home.cell

import android.view.View
import com.neuifo.domain.model.dmzj.Comment
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.domain.util.ImageLoaderUtils
import com.neuifo.mangareptile.ui.base.listcell.SimpleCell
import com.neuifo.mangareptile.utils.ViewUtils
import com.neuifo.mangareptile.utils.getVisiable
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentCell(var comment: Comment) : SimpleCell() {


    override val itemClickable: Boolean
        get() = true

    override val haveItemClickBg: Boolean
        get() = true

    override val itemBackgroundResource: Int
        get() = R.drawable.selector_recycler_item_bg_black

    override val itemLayoutRes: Int
        get() = R.layout.item_comment

    override fun onBindData(iv: View) {
        iv.item_comment_parent_cover.visibility = View.GONE
        iv.item_comment_parent_name.visibility = View.GONE
        iv.item_comment_index.visibility = View.VISIBLE
        iv.item_comment_index.text = "#${comment.index}"
        iv.item_comment_child_content.setTextColor(getColor(R.color.white))
        iv.item_comment_child_content.text = ViewUtils.decodeString(
            "${comment.nickname} : ${comment.content}",
            "${comment.nickname}",
            "#18F304"
        )
        when {
            comment.index == 0 -> {//原本评论
                iv.item_comment_parent_cover.visibility = comment.isBottom.getVisiable()
                iv.item_comment_parent_name.visibility = comment.isBottom.getVisiable()
                iv.item_comment_index.visibility = View.GONE
                iv.item_comment_child_content.text = comment.content
                iv.item_comment_child_content.background = null
                iv.item_comment_child_content.setTextColor(getColor(R.color.white))
                ImageLoaderUtils.displayRound(
                    iv.item_comment_parent_cover,
                    comment.avatar_url,
                    R.drawable.avatar_default
                )
                iv.item_comment_parent_name.text = comment.nickname
            }
            comment.index == 1 -> {//顶部
                iv.item_comment_parent_cover.visibility = View.VISIBLE
                iv.item_comment_parent_name.visibility = View.VISIBLE
                ImageLoaderUtils.displayRound(
                    iv.item_comment_parent_cover,
                    comment.parentUrl,
                    R.drawable.avatar_default
                )
                iv.item_comment_parent_name.text = comment.parentName

                iv.item_comment_index.visibility = (!comment.isBottom).getVisiable()

                iv.item_comment_child_content.background =
                    if (comment.isBottom) getDrawable(R.drawable.bg_comment_full) else getDrawable(R.drawable.bg_comment_first)
            }
            comment.isBottom -> {//底部
                iv.item_comment_child_content.background = getDrawable(R.drawable.bg_comment_bottom)
            }
            else -> {//中间
                iv.item_comment_child_content.background = getDrawable(R.drawable.bg_comment_middle)
            }
        }

    }
}