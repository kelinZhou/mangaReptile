package com.neuifo.data.domain.utils


import android.text.TextUtils
import android.util.Log
import com.neuifo.domain.model.common.KeyValuePair
import java.util.*

class LogHelper private constructor(
    /**
     * 开发者。
     */
    private val developer: DEVELOPER
) {


    /**
     * 获取当前方法的详细信息
     * 具体到方法名、方法行，方法所在类的文件名
     *
     * @return
     */
    private//本地方法native  jni
    //线程
    //构造方法
    val functionName: String?
        get() {
            val sts = Thread.currentThread().stackTrace ?: return null
            for (st in sts) {
                if (st.isNativeMethod) {
                    continue
                }
                if (st.className == Thread::class.java.name) {
                    continue
                }
                if (st.className == this.javaClass.name) {
                    continue
                }
                val split = st.className.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val className = if (split.size == 0) st.className else split[split.size - 1]
                return ("[ Thread: " + Thread.currentThread().name + " Class: "
                        + className + " Line:" + st.lineNumber + " Method: "
                        + st.methodName + " ]")
            }
            return null
        }

    private enum class DEVELOPER constructor(var devName: String, var tag: String) :
        KeyValuePair {

        NEUIFO("neuifo", "@NEUIFO@:"),
        SYSTEM("SYSTEM", "@SYSTEM@:"),
        ;


        override val key: String
            get() = devName
        override val value: String
            get() = tag
    }

    private fun showLog(): Boolean {
        // 只有系统和本人的日志才能输出
        return !sIsFilterOtherDeveloperLog || sCurDeveloperName?.equals(
            developer.key,
            ignoreCase = true
        ) == true || DEVELOPER.SYSTEM.key == developer.key
    }


    fun i(msg: String) {
        i(null, msg)
    }

    fun i(tag: String?, msg: String) {
        if (sIsDebug && showLog() && !TextUtils.isEmpty(msg)) {
            Log.i(tag + "   [+" + developer.value + "]", "$msg--$functionName")
        }
    }

    fun d(tag: String, msg: String) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
            return
        }

        if (sIsDebug && showLog()) {
            Log.d(tag + "   [+" + developer.value + "]", msg)
        }
    }

    fun d(msg: String) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        if (sIsDebug && showLog()) {
            Log.d("[+" + developer.value + "]", "$msg--$functionName")
        }
    }

    fun e(tag: String, msg: String) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
            return
        }
        if (sIsDebug && showLog()) {
            Log.e(tag + "   [+" + developer.value + "]", msg)
        }
    }

    fun e(msg: String) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        if (sIsDebug && showLog()) {
            Log.e("[+" + developer.value + "]", "$msg--$functionName")
        }
    }


    fun e(tag: String, msg: String, e: Exception) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
            return
        }

        if (sIsDebug && showLog()) {
            Log.e(tag + "   [+" + developer.value + "]", msg, e)
        }
    }

    companion object {
        /**
         * 过滤其他开发者日志
         */
        private val FILTER_OTHER_DEVELOPER_LOG = true

        private var sIsDebug = true
        private var sCurDeveloperName: String? = null
        private var sIsFilterOtherDeveloperLog = true
        /**
         * 表示输出方法信息及位置。
         */
        private var sLogMethodInfo = true
        private val sLoggerTable = Hashtable<String, LogHelper>()

        /**
         * 初始化函数。需要在Application启动的时候进行初始化。
         *
         * @param developerName           开发者名称，也是电脑名称。如果你的电脑没有设置过名称请设置一下。
         * @param isDebug                 当前是否是debug模式。
         * @param logMethodInfo           是否打印日志所处的方法的信息。
         * @param filterOtherDeveloperLog 是否过滤其他开发者日志，如果为true表示你讲看不到其他开发者的日志，false则可以。默认为true。
         */
        @JvmOverloads
        fun init(
            developerName: String,
            isDebug: Boolean,
            logMethodInfo: Boolean = sLogMethodInfo,
            filterOtherDeveloperLog: Boolean = FILTER_OTHER_DEVELOPER_LOG
        ) {
            sCurDeveloperName = developerName
            sIsDebug = isDebug
            sLogMethodInfo = logMethodInfo
            sIsFilterOtherDeveloperLog = filterOtherDeveloperLog
        }

        val neuifo: LogHelper
            get() = getLogger(DEVELOPER.NEUIFO)

        val system: LogHelper
            get() = getLogger(DEVELOPER.SYSTEM)

        private fun getLogger(developer: DEVELOPER): LogHelper {
            var classLogger = sLoggerTable[developer.key]
            if (classLogger == null) {
                classLogger = LogHelper(developer)
                sLoggerTable[developer.key] = classLogger
            }
            return classLogger
        }


        fun logAll(tag: String, message: String) {
            val sb = StringBuffer()
            val sts = Thread.currentThread().stackTrace
            for (i in sts.indices) {
                val methodName = sts[i].methodName
                val className = sts[i].className
                val lineNumber = sts[i].lineNumber
                sb.append(methodName).append("(").append(className).append("-").append(lineNumber)
                    .append(");")
                    .append("\n")
            }
            Log.i(tag, "$message-->$sb")
        }


        private val trace: String = ""
            get() {
                return if (field.isEmpty()) {
                    val st = Thread.currentThread().stackTrace[5]
                    val fullClassName = st.className
                    val className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
                    val lineNumber = st.lineNumber
                    "($className:$lineNumber)"
                } else if (field.isEmpty()) {
                    try {
                        Throwable().stackTrace[2].toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ""
                    }
                } else {
                    field
                }
            }
    }
}
