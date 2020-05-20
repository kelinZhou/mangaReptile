package com.neuifo.data.util;

import android.content.Context;

import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

public final class DbModule {

    public static androidx.sqlite.db.SupportSQLiteOpenHelper create(Context context, String name, androidx.sqlite.db.SupportSQLiteOpenHelper.Callback callback) {
        androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration configuration = androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(name)
                .callback(callback)
                .build();
        FrameworkSQLiteOpenHelperFactory factory = new FrameworkSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper helper = factory.create(configuration);
        return helper;
    }
}