package com.neuifo.domain.model.auto.utils;


import androidx.annotation.NonNull;

import com.squareup.sqldelight.ColumnAdapter;

import java.util.Calendar;
import java.util.Date;

public /*static*/ class LongCalendarAdapter implements ColumnAdapter<Calendar, Long> {

    @NonNull
    @Override
    public Calendar decode(Long databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(databaseValue));
        return calendar;
    }

    @Override
    public Long encode(@NonNull Calendar value) {
        if (value == null) {
            Long.valueOf(0);
        }
        return value.getTime().getTime();
    }
}