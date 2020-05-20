package com.neuifo.data.cache.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * 自定义存储路径
 */
public class DataBaseContext extends ContextWrapper {

    String path;

    public DataBaseContext(Context base, String path) {
        super(base);
        this.path = path;
    }


    /** 重写数据库路径方法 **/
    @Override
    public File getDatabasePath(String name) {
//        String path1= SDCardUtil.getDiskFilePath(name);
//        String dirPath=path1.replace(name,"");

        String dirPath = Environment.getExternalStorageDirectory() + File.separator;

        String path = null;
        File parentFile = new File(dirPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        String parentPath = parentFile.getAbsolutePath();
        if (parentPath.lastIndexOf("\\/") != -1) {
            path = dirPath + File.separator + name;
        } else {
            path = dirPath + name;
        }
        File file = new File(path);

        return file;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(), factory, errorHandler);
    }
}
