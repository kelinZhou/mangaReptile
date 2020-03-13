package com.neuifo.mangareptile.data.subscriber

import android.net.ParseException
import android.text.TextUtils
import com.google.gson.JsonParseException
import com.neuifo.domain.exception.ApiException
import com.neuifo.mangareptile.AppLayerErrorCatcher
import org.json.JSONException

import java.io.IOException
import java.net.UnknownHostException


/**
 * Created by neuifo on 16/9/9.
 * 处理网络异常
 */
abstract class NetErrorHandler<T> : ErrorHandlerSubscriber<T>() {

    override fun dealError(e: Throwable) {
        if (e is UnknownHostException || TextUtils.equals("GaiException", e.javaClass.simpleName)) {
            handleNetworkError(ApiException(ApiException.Error.NETWORK_UNAVAILABLE))
        } else if (e is JsonParseException || e is JSONException || e is ParseException) {
            handleDataError(ApiException(ApiException.Error.RESULT_ERROR, e))
            printError(e, "ParseException Exception")
        } else if (e is IOException) {
            // 均视为网络错误
            handleNetworkError(ApiException(ApiException.Error.RESULT_ERROR, e))
            printError(e, "IOException Exception")
        } else if (e is ApiException) {
            handleCustomError(e)
        } else {
            // 未知错误
            handleUnknownError(ApiException(ApiException.Error.UNKNOWN_ERROR.code, e.message, e))
            printError(e, "UNKNOWN Exception")
        }
        onFinished(false)
    }

    private fun handleCustomError(ex: ApiException) {
        try {
            onError(ex)
        } catch (e: Exception) {
            printError(e, "handleCustomError error")
            AppLayerErrorCatcher.throwException(e)
        }
    }

    /**
     * 服务器返回数据的错误(无法解析)
     */
    protected open fun handleDataError(ex: ApiException) {
        try {
            onError(ex)
        } catch (e: Exception) {
            printError(e, "handleDataError error")
            AppLayerErrorCatcher.throwException(e)
        }
    }


    /**
     * http权限错误，需要实现重新登录操作
     */
    protected open fun handlePermissionError(ex: ApiException) {
        try {
            onError(ex)
        } catch (e: Exception) {
            printError(e, "handlePermissionError")
            AppLayerErrorCatcher.throwException(e)
        }
    }

    protected open fun handleArgumentError(ex: ApiException) {
        try {
            onError(ex)
        } catch (e: Exception) {
            AppLayerErrorCatcher.throwException(e)
        }
    }


    protected open fun handleNetworkError(ex: ApiException) {
        try {
            onError(ex)
        } catch (e: Exception) {
            printError(e, "handleNetworkError error")
            AppLayerErrorCatcher.throwException(e)
        }
    }

    protected open fun handleUnknownError(ex: ApiException) {
        try {
            onError(ex)
        } catch (e: Exception) {
            printError(e, "handleUnknownError error")
            AppLayerErrorCatcher.throwException(e)
        }
    }
}
