package com.neuifo.data.cache.helper

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

/**
 * 初始化之前确保上一个链接已关闭
 */
class ComicDetailDbOpenHelper : SupportSQLiteOpenHelper.Callback(DATABASE_VERSION) {

    private fun upgradeTo(db: SupportSQLiteDatabase, version: Int) {
    }

    override fun onCreate(db: SupportSQLiteDatabase) {}
    override fun onUpgrade(
        db: SupportSQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        for (version in oldVersion + 1..newVersion) {
            upgradeTo(db, version)
        }
    }

    companion object {
        private const val DATABASE_VERSION = 1
        const val dbName = "comic_update.db"
    }
}