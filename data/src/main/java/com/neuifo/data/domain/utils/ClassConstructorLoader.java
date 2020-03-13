package com.neuifo.data.domain.utils;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * Created by neuifo on 2016/11/16.
 */

public class ClassConstructorLoader {

    public static <C> Constructor<C> getAppropriateConstructor(Class<C> c, Object[] initArgs) {
        if (initArgs == null)
            initArgs = new Object[0];
        for (Constructor con : c.getDeclaredConstructors()) {
            Class[] types = con.getParameterTypes();
            if (types.length != initArgs.length)
                continue;
            boolean match = true;
            for (int i = 0; i < types.length; i++) {
                Class<?> need = types[i], got = initArgs[i].getClass();
                if (!need.isAssignableFrom(got)) {
                    if (need.isPrimitive()) {
                        match = (int.class.equals(need) && Integer.class.equals(got))
                                || (long.class.equals(need) && Long.class.equals(got))
                                || (char.class.equals(need) && Character.class.equals(got))
                                || (short.class.equals(need) && Short.class.equals(got))
                                || (boolean.class.equals(need) && Boolean.class.equals(got))
                                || (byte.class.equals(need) && Byte.class.equals(got)
                                || (float.class.equals(need) && Float.class.equals(got)));
                        // if (!match)System.out.println("Not match " + need + "; " + got + "; " + initArgs[i]);
                    } else {
                        // System.out.println("Not match " + need + "; " + got + "; " + initArgs[i]);
                        match = false;
                    }
                }
                if (!match)
                    break;
            }
            if (match)
                return con;
        }
        throw new IllegalArgumentException("Cannot find an appropriate constructor for class " + c + " and arguments " + Arrays.toString(initArgs));
    }
}
