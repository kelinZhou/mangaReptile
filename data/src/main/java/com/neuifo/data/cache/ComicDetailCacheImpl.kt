package com.neuifo.data.cache

import android.content.Context
import com.neuifo.data.cache.basic.ComicCache
import com.neuifo.data.cache.helper.ComicDetailDbOpenHelper
import com.neuifo.data.util.DbModule
import com.neuifo.domain.beans.ComicDB
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
}