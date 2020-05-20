package com.neuifo.domain.model.dmzj

class Comment(
    var avatar_url: String,
    var content: String,
    var create_time: String,
    var id: String,
    var is_goods: String,
    var like_amount: String,
    var nickname: String,
    var obj_id: String,
    var origin_comment_id: String,
    var sender_uid: String,
    var sex: String,
    var upload_images: String,
    var parents: MutableList<Comment>
)