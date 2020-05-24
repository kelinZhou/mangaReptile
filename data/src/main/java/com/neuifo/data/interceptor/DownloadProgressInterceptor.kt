package com.neuifo.data.interceptor

import com.neuifo.domain.model.DownloadProgressListener
import com.neuifo.data.util.DownloadProgressResponseBody
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class DownloadProgressInterceptor(
    var progressListener: DownloadProgressListener?
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val builder = originalResponse.newBuilder()
        val downloadIdentifier = originalResponse.request()
            .header(DOWNLOAD_IDENTIFIER_HEADER)
        val isAStream =
            originalResponse.header("content-type", "") == "application/octet-stream"
        val fileIdentifierIsSet =
            downloadIdentifier != null && !downloadIdentifier.isEmpty()
        if (isAStream && fileIdentifierIsSet) { // someone need progress informations !
            builder.body(
                DownloadProgressResponseBody(
                    downloadIdentifier!!,
                    originalResponse.body()!!,
                    progressListener
                )
            )
        } else { // do nothing if it's not a file with an identifier :)
            builder.body(originalResponse.body())
        }
        return builder.build()
    }

    companion object {
        const val DOWNLOAD_IDENTIFIER_HEADER = "download-identifier"
    }
}