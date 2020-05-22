package com.neuifo.data.cache

import android.content.Context
import com.neuifo.data.cache.basic.ComicCache
import com.neuifo.data.cache.helper.ComicDetailDbOpenHelper
import com.neuifo.data.util.DbModule
import com.neuifo.domain.beans.ComicDB
import com.neuifo.domain.model.dmzj.ComicDetail
import com.neuifo.domain.model.dmzj.ComicUpdate
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.runtime.rx.asObservable
import com.squareup.sqldelight.runtime.rx.mapToList
import io.reactivex.Observable

class ComicDetailCacheImpl(var context: Context) : ComicCache {


    private val comicDB: ComicDB

    init {
        val sqLiteOpenHelper = ComicDetailDbOpenHelper()
        val supportSQLiteOpenHelper =
            DbModule.create(context, ComicDetailDbOpenHelper.dbName, sqLiteOpenHelper)

        val driver = AndroidSqliteDriver(supportSQLiteOpenHelper)
        ComicDB.Schema.create(driver)

        comicDB = ComicDB.invoke(driver)
    }


    override fun queryComicByIDs(ids: List<Long>): Observable<MutableList<ComicUpdate>> {
        return comicDB.dbComicDetailInfoQueries.query_list_comic(ids)
            .asObservable()
            .mapToList().map { itemList ->
                //这里暂时不通过auto_value转换
                itemList.map {
                    ComicUpdate.createReadInfo(
                        it.id,
                        it.last_read_name,
                        it.last_read_chapter_id
                    )
                }.toMutableList()
            }
    }

    override fun saveComicDetail(detail: ComicDetail) {
        comicDB.dbComicDetailInfoQueries.transaction {
            comicDB.dbComicDetailInfoQueries.save_comic_info(
                detail.id,
                detail.title,
                detail.authors.joinToString("/") { it.name },
                detail.types.joinToString("/") { it.name },
                detail.cover,
                detail.status.joinToString("/") { it.name },
                detail.latest_update_chapter_name,
                detail.latest_update_chapter_id,
                detail.latest_update_time,
                detail.description
            )
        }
    }

    override fun queryLastReadChapterId(comicId: Long): Long {
        return comicDB.dbComicDetailInfoQueries.query_last_read_chapter_id(comicId).executeAsOneOrNull()?.last_read_chapter_id
            ?: 0L
    }
}