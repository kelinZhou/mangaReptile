package com.neuifo.domain.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class EnumDeserializers {
    private static final String TAG = "EnumDeserializers";

    public interface SerializableEnum /*extends Enum<SerializableEnum>*/ {
        int getId();
    }

    public interface MatchDefaultSerializableEnum extends SerializableEnum {
        boolean isDefault();
    }

    public interface CommenSerializableEnum {
        String getValue();
    }


    /**
     * http://www.cnblogs.com/linjzong/p/5201565.html
     * <p>
     * Gson(final Excluder excluder, final FieldNamingStrategy fieldNamingPolicy,
     * final Map<Type, InstanceCreator<?>> instanceCreators, boolean serializeNulls,
     * boolean complexMapKeySerialization, boolean generateNonExecutableGson, boolean htmlSafe,
     * boolean prettyPrinting, boolean serializeSpecialFloatingPointValues,
     * LongSerializationPolicy longSerializationPolicy,
     * List<TypeAdapterFactory> typeAdapterFactories) {
     * this.constructorConstructor = new ConstructorConstructor(instanceCreators);
     * this.serializeNulls = serializeNulls;
     * this.generateNonExecutableJson = generateNonExecutableGson;
     * this.htmlSafe = htmlSafe;
     * this.prettyPrinting = prettyPrinting;
     * <p>
     * List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>();
     * <p>
     * // built-in type adapters that cannot be overridden
     * factories.add(TypeAdapters.JSON_ELEMENT_FACTORY);
     * factories.add(ObjectTypeAdapter.FACTORY);
     * <p>
     * // the excluder must precede all adapters that handle user-defined types
     * factories.add(excluder);
     * <p>
     * // user's type adapters
     * factories.addAll(typeAdapterFactories);
     * <p>
     * // type adapters for basic platform types
     * factories.add(TypeAdapters.STRING_FACTORY);
     * factories.add(TypeAdapters.INTEGER_FACTORY);
     * factories.add(TypeAdapters.BOOLEAN_FACTORY);
     * factories.add(TypeAdapters.BYTE_FACTORY);
     * factories.add(TypeAdapters.SHORT_FACTORY);
     * factories.add(TypeAdapters.newFactory(long.class, Long.class,
     * longAdapter(longSerializationPolicy)));
     * factories.add(TypeAdapters.newFactory(double.class, Double.class,
     * doubleAdapter(serializeSpecialFloatingPointValues)));
     * factories.add(TypeAdapters.newFactory(float.class, Float.class,
     * floatAdapter(serializeSpecialFloatingPointValues)));
     * factories.add(TypeAdapters.NUMBER_FACTORY);
     * factories.add(TypeAdapters.CHARACTER_FACTORY);
     * factories.add(TypeAdapters.STRING_BUILDER_FACTORY);
     * factories.add(TypeAdapters.STRING_BUFFER_FACTORY);
     * factories.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
     * factories.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
     * factories.add(TypeAdapters.URL_FACTORY);
     * factories.add(TypeAdapters.URI_FACTORY);
     * factories.add(TypeAdapters.UUID_FACTORY);
     * factories.add(TypeAdapters.LOCALE_FACTORY);
     * factories.add(TypeAdapters.INET_ADDRESS_FACTORY);
     * factories.add(TypeAdapters.BIT_SET_FACTORY);
     * factories.add(DateTypeAdapter.FACTORY);
     * factories.add(TypeAdapters.CALENDAR_FACTORY);
     * factories.add(TimeTypeAdapter.FACTORY);
     * factories.add(SqlDateTypeAdapter.FACTORY);
     * factories.add(TypeAdapters.TIMESTAMP_FACTORY);
     * factories.add(ArrayTypeAdapter.FACTORY);
     * factories.add(TypeAdapters.ENUM_FACTORY);
     * factories.add(TypeAdapters.CLASS_FACTORY);
     * <p>
     * // type adapters for composite and user-defined types
     * factories.add(new CollectionTypeAdapterFactory(constructorConstructor));
     * factories.add(new MapTypeAdapterFactory(constructorConstructor, complexMapKeySerialization));
     * factories.add(new ReflectiveTypeAdapterFactory(
     * constructorConstructor, fieldNamingPolicy, excluder));
     * <p>
     * this.factories = Collections.unmodifiableList(factories);
     * }
     * <p>
     * http://stackoverflow.com/questions/8211304/using-enums-while-parsing-json-with-gson
     * <p>
     * 枚举不用自己写：
     * public class Item {
     *
     * @param <T>
     * @SerializedName("status") private Status currentState = null;
     * <p>
     * // other fields, getters, setters, constructor and other code...
     * <p>
     * public enum Status {
     * @SerializedName("0") BUY,
     * @SerializedName("1") DOWNLOAD,
     * @SerializedName("2") DOWNLOADING,
     * @SerializedName("3") OPEN
     * }
     * }
     */
    private static /*final*/ class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
        private final Map<String, T> nameToConstant = new HashMap<String, T>();
        private final Map<T, String> constantToName = new HashMap<T, String>();

        public EnumTypeAdapter(Class<T> classOfT) {
            try {
                for (T constant : classOfT.getEnumConstants()) {
                    String name = constant.name();
                    SerializedName annotation = classOfT.getField(name).getAnnotation(SerializedName.class);
                    if (annotation != null) {
                        name = annotation.value();
                        for (String alternate : annotation.alternate()) {
                            nameToConstant.put(alternate, constant);
                        }
                    }
                    nameToConstant.put(name, constant);
                    constantToName.put(constant, name);
                }
            } catch (NoSuchFieldException e) {
                throw new AssertionError(e);
            }
        }

        @Override
        public T read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return nameToConstant.get(in.nextString());
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            out.value(value == null ? null : constantToName.get(value));
        }
    }

    public static abstract class CommonEnumSerializer<E extends EnumDeserializers.CommenSerializableEnum> implements JsonSerializer<E>, JsonDeserializer<E> {
        @Override
        public JsonElement serialize(E src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getValue());
        }

        @Override
        public E deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String value = json.getAsString();

            E v = deserialize(getSerializableValues(), value);
            if (v == null) {
                v = getDefaultValue();
            }
            return v;
        }

        protected abstract E[] getSerializableValues();

        protected E getDefaultValue() {
            E[] values = getSerializableValues();
            for (E e : values) {
                if (e instanceof MatchDefaultSerializableEnum) {
                    boolean isDefault = ((MatchDefaultSerializableEnum) e).isDefault();
                    if (isDefault) {
                        return e;
                    }
                }
            }
            return null;
        }

        protected E deserialize(E[] values, String value) {
            int len = values.length;
            for (int i = 0; i < len; i++) {
                if (values[i].getValue().equals(value)) {
                    return values[i];
                }
            }
            return null;
        }
    }



    public static abstract class IntEnumEnumSerializer<E extends SerializableEnum> implements JsonSerializer<E>, JsonDeserializer<E> {

        @Override
        public JsonElement serialize(E src, Type typeOfSrc, JsonSerializationContext context) {
            // Class c = state.getClass();
            // return new JsonPrimitive(src.ordinal());
            // return new JsonPrimitive(src.get());
            // Enum<? extends SerializableEnum> a = null;

            // Enum<? extends Object> b = null;
            // Enum<? extends String> c = null;
            return new JsonPrimitive(src.getId());
        }


        @Override
        public E deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            int value = json.getAsInt();

            E v = deserialize(getSerializableValues(), value);
            if (v == null) {
                v = getDefaultValue();
            }
            return v;
        }

        protected abstract E[] getSerializableValues();

        protected E getDefaultValue() {
            E[] values = getSerializableValues();
            for (E e : values) {
                if (e instanceof MatchDefaultSerializableEnum) {
                    boolean isDefault = ((MatchDefaultSerializableEnum) e).isDefault();
                    if (isDefault) {
                        return e;
                    }
                }
            }
            return null;
        }

        protected E deserialize(E[] values, int id) {
            int len = values.length;
            for (int i = 0; i < len; i++) {
                if (values[i].getId() == id) {
                    return values[i];
                }
            }
            return null;
        }
    }


}
