package com.neuifo.domain.model.auto.utils;


import com.neuifo.domain.modeltype.BooleanEntity;
import com.squareup.sqldelight.ColumnAdapter;

import java.util.Calendar;
import java.util.Date;


public class ColumnAdapterHelper {

    // 统一加上一个默认值，如果为为null就是false
    public static final ColumnAdapter<BooleanEntity, Long> BOOLEAN_ENUM_ADAPTER = LongEnumColumnAdapter.create(BooleanEntity.class, BooleanEntity.FALSE);

    public static final ColumnAdapter<Calendar, Long> CALENDAR_TYPE_ADAPTER = new LongCalendarAdapter();

    public static final ColumnAdapter<Date, Long> DATE_TYPE_ADAPTER = new LongDateAdapter();
}


