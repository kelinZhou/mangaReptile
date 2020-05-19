package com.neuifo.domain.model.dmzj

/**
 * id	Integer	22027
title	String	瑞克与莫蒂
islong	Integer	2
authors	String	ONI PRESS
types	String	科幻
cover	String	https://images.dmzj.com/webpic/1/ruikeyutimo.JPG
status	String	连载中
last_update_chapter_name	String	第47卷
last_update_chapter_id	Integer	98555
last_update_time	Integer	1583992455


latest_update_chapter_name TEXT,
latest_update_chapter_id INTEGER,
last_read_name TEXT,
last_read_chapter_id INTEGER,

 */
class ComicUpdate(
    var id: Long,
    var title: String,
    var authors: String,
    var types: String,
    var cover: String,
    var status: String,
    var latest_update_chapter_name: String,
    var latest_update_chapter_id: Long,
    var latest_update_time: Long,
    var last_read_name:String,
    var last_read_chapter_id:Long
)