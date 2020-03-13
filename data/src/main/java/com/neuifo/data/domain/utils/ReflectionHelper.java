package com.neuifo.data.domain.utils;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangshenglong on 16/4/3.
 */
public class ReflectionHelper {

    /**
     * http://www.artima.com/weblogs/viewpost.jsp?thread=208860
     * <p>
     * Gson方式：
     * <p>
     * java.lang.reflect.Type fooType = new TypeToken<Foo<Bar>>() {}.getValue();
     * gson.toJson(foo, fooType);
     * gson.fromJson(json, fooType)
     * <p>
     * <p>
     * ParameterizedType为Type子接口
     * <p>
     * 对于一个类如果继承了一个带具体泛型参数的泛型类,可以通过以下方式获取实际泛型类型
     * class AnonymousArrayListA extends ArrayList<String> {
     * <p>
     * }
     * AnonymousArrayListA obj = ...;
     * Class typeA = (Class) ((ParameterizedType) obj.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
     * <p>
     * <p>
     * 但是这个类本身是参数化的，则无法获取泛型参数（以下方式不行，需要其他方式，比如上面的gson方式）:
     * <p>
     * class AnonymousArrayListB<T> extends ArrayList<T> {
     * <p>
     * }
     * AnonymousArrayListB<String> obj = ...;
     * ...
     */
    static Class<?> getClass(Type type) {

        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = getClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static <T> List<Class<?>> getTypeArguments(Class<T> baseClass, Class<? extends T> childClass) {


        Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
        Type type = childClass;
        // start walking up the inheritance hierarchy until we hit baseClass
        while (!getClass(type).equals(baseClass)) {
            if (type instanceof Class) {
                // there is no useful information for us in raw types, so just keep going.
                type = ((Class) type).getGenericSuperclass();
            } else {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> rawType = (Class) parameterizedType.getRawType();

                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
                }

                if (!rawType.equals(baseClass)) {
                    type = rawType.getGenericSuperclass();
                }
            }
        }

        // finally, for each actual type argument provided to baseClass, determine (if possible)
        // the raw class for that type argument.
        //此处Type的kotlin初始化有问题
        Type[] actualTypeArguments;
        if (type instanceof Class) {
            actualTypeArguments = ((Class) type).getTypeParameters();
        } else {
            actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        }
        List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
        // resolve types by chasing down type variables.
        for (Type baseType : actualTypeArguments) {
            while (resolvedTypes.containsKey(baseType)) {
                baseType = resolvedTypes.get(baseType);
            }
            typeArgumentsAsClasses.add(getClass(baseType));
        }
        return typeArgumentsAsClasses;
    }
}
