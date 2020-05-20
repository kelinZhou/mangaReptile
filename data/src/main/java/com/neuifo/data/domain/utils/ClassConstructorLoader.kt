package com.neuifo.data.domain.utils

import java.lang.reflect.Constructor
import java.util.*

/**
 * Created by neuifo on 2016/11/16.
 */
object ClassConstructorLoader {
    fun <C> getAppropriateConstructor(
        c: Class<C>,
        initArgs: Array<Any?>?
    ): Constructor<C> {
        var initArgs = initArgs
        if (initArgs == null) initArgs = arrayOfNulls(0)
        for (con in c.declaredConstructors) {
            val types = con.parameterTypes
            if (types.size != initArgs.size) continue
            var match = true
            for (i in types.indices) {
                val need = types[i]
                val got: Class<*> = initArgs[i]!!.javaClass
                if (!need.isAssignableFrom(got)) {
                    match = if (need.isPrimitive) {
                        (Int::class.javaPrimitiveType == need && Int::class.java == got
                                || Long::class.javaPrimitiveType == need && Long::class.java == got
                                || Char::class.javaPrimitiveType == need && Char::class.java == got
                                || Short::class.javaPrimitiveType == need && Short::class.java == got
                                || Boolean::class.javaPrimitiveType == need && Boolean::class.java == got
                                || (Byte::class.javaPrimitiveType == need && Byte::class.java == got
                                || Float::class.javaPrimitiveType == need && Float::class.java == got))
                        // if (!match)System.out.println("Not match " + need + "; " + got + "; " + initArgs[i]);
                    } else {
                        // System.out.println("Not match " + need + "; " + got + "; " + initArgs[i]);
                        false
                    }
                }
                if (!match) break
            }
            if (match) return con as Constructor<C>
        }
        throw IllegalArgumentException(
            "Cannot find an appropriate constructor for class $c and arguments " + Arrays.toString(
                initArgs
            )
        )
    }
}