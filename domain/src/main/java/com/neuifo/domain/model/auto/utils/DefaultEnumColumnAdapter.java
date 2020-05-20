package com.neuifo.domain.model.auto.utils;


import androidx.annotation.NonNull;

import com.squareup.sqldelight.ColumnAdapter;

import org.xml.sax.ErrorHandler;


public final class DefaultEnumColumnAdapter<T extends Enum<T>> implements ColumnAdapter<T, String> {
    public static <T extends Enum<T>> DefaultEnumColumnAdapter<T> create(Class<T> cls, T defVal) {
        if (cls == null) throw new NullPointerException("cls == null");
        return new DefaultEnumColumnAdapter<>(cls, defVal);
    }

    private final Class<T> cls;
    private final T defVal;

    private DefaultEnumColumnAdapter(Class<T> cls, T defVal) {
        this.cls = cls;
        this.defVal = defVal;
    }

    @Override
    @NonNull
    public T decode(String databaseValue) {
        if (databaseValue == null) {
            return defVal;
        }
        try {
            T v = Enum.valueOf(cls, databaseValue);
            return v;
        } catch (Exception e) {
            return defVal;
        }
    }

    @Override
    public String encode(@NonNull T value) {
        if (value == null) {
            return defVal.name();
        }
        return value.name();
    }
}
