package com.neuifo.domain.model.auto.utils;


import androidx.annotation.NonNull;

import com.neuifo.domain.utils.EnumDeserializers;
import com.squareup.sqldelight.ColumnAdapter;

public /*final*/ class LongEnumColumnAdapter<T extends Enum<T> & EnumDeserializers.SerializableEnum> implements ColumnAdapter<T, Long> {

    public static <T extends Enum<T> & EnumDeserializers.SerializableEnum> LongEnumColumnAdapter<T> create(Class<T> cls) {
        if (cls == null) throw new NullPointerException("cls == null");
        return new LongEnumColumnAdapter<>(cls);
    }

    public static <T extends Enum<T> & EnumDeserializers.SerializableEnum> LongEnumColumnAdapter<T> create(Class<T> cls, T defaultForNullValue) {
        if (cls == null) throw new NullPointerException("cls == null");
        return new LongEnumColumnAdapter<>(cls, defaultForNullValue);
    }

    private final Class<T> cls;
    private T defaultForNullValue;

    private LongEnumColumnAdapter(Class<T> cls) {
        this.cls = cls;
    }

    public LongEnumColumnAdapter(Class<T> cls, T defaultForNullValue) {
        this.cls = cls;
        this.defaultForNullValue = defaultForNullValue;
    }

    @Override
    @NonNull
    public T decode(Long databaseValue) {
        // return Enum.valueOf(cls, databaseValue);
        if (databaseValue == null && defaultForNullValue != null) {
            return defaultForNullValue;
        }
        return LongEnumColumnAdapter.valueOf(cls, databaseValue.intValue());
    }

    @Override
    public Long encode(@NonNull T value) {
        if (value == null && defaultForNullValue != null) {
            return Long.valueOf(defaultForNullValue.getId());
        }
        return Long.valueOf(value.getId());
    }

    public static <T extends Enum<T> & EnumDeserializers.SerializableEnum> T valueOf(Class<T> enumType,
                                                                                     Integer id) {
        if (enumType == null)
            throw new NullPointerException("enumType == null");
        if (id == null)
            throw new NullPointerException("Name is null");
        // T[] values = Enum.getSharedConstants(enumType);
        T[] values = enumType.getEnumConstants();
        T result = null;
        if (values != null) {
            for (T value : values) {
                if (id.equals(value.getId())) {
                    result = value;
                }
            }
        } else {
            throw new IllegalArgumentException(enumType.toString() + " is not an enum type.");
        }

        if (result != null)
            return result;
        throw new IllegalArgumentException(
                "No enum constant " + enumType.getCanonicalName() + "." + id);
    }

    public static <T extends Enum<T> & EnumDeserializers.SerializableEnum> T valueOf(Class<T> enumType,
                                                                                     String name) {
        if (enumType == null)
            throw new NullPointerException("enumType == null");
        if (name == null)
            throw new NullPointerException("Name is null");
        // T[] values = Enum.getSharedConstants(enumType);
        T[] values = enumType.getEnumConstants();
        T result = null;
        if (values != null) {
            for (T value : values) {
                if (name.equals(value.name())) {
                    result = value;
                }
            }
        } else {
            throw new IllegalArgumentException(enumType.toString() + " is not an enum type.");
        }

        if (result != null)
            return result;
        throw new IllegalArgumentException(
                "No enum constant " + enumType.getCanonicalName() + "." + name);
    }
}

//public final class LongEnumColumnAdapter<T extends Enum<T>> implements ColumnAdapter<T, String> {
//
//    public static <T extends Enum<T>> LongEnumColumnAdapter<T> create(Class<T> cls) {
//        if (cls == null) throw new NullPointerException("cls == null");
//        return new LongEnumColumnAdapter<>(cls);
//    }
//
//    private final Class<T> cls;
//
//    private LongEnumColumnAdapter(Class<T> cls) {
//        this.cls = cls;
//    }
//
//    @Override
//    @NonNull
//    public T decode(String databaseValue) {
//        return Enum.valueOf(cls, databaseValue);
//    }
//
//    @Override
//    public String encode(@NonNull T value) {
//        return value.name();
//    }
//}


