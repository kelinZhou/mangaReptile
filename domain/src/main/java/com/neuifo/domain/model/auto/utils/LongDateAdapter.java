package com.neuifo.domain.model.auto.utils;


import androidx.annotation.NonNull;

import com.squareup.sqldelight.ColumnAdapter;

import java.util.Date;

public /*static*/ class LongDateAdapter implements ColumnAdapter<Date, Long> {

    @NonNull
    @Override
    public Date decode(Long databaseValue) {
        if (databaseValue == null || databaseValue == 0) {
            return null;
        }
        return new Date(databaseValue);
    }

    @Override
    public Long encode(@NonNull Date value) {
        if (value == null) {
            return Long.valueOf(0);
        }
        return value.getTime();
    }
}