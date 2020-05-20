package com.neuifo.data.interceptor

import com.neuifo.data.Constansts.CHANNEL
import com.neuifo.data.Constansts.DEBUG
import com.neuifo.data.Constansts.DEBUG_VERSION
import com.neuifo.data.Constansts.SOURCE
import com.neuifo.data.Constansts.TIMESTAMP
import com.neuifo.data.Constansts.TOKEN
import com.neuifo.data.Constansts.UID
import com.neuifo.data.Constansts.VERSION
import com.neuifo.data.Constansts.VERSION_CODE
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Okio
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.Charset

/**
 * 请求头添加信息 Security-Token:获取的ticket
 */
class HeadTokenInterceptor(private val ticket: String, private val uid: Long) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val method = originalRequest.method()

        /*Request.Builder builder = originalRequest.newBuilder();
        builder.addHeader(Constansts.INSTANCE.getCHANNEL(), Constansts.INSTANCE.getSOURCE());
        builder.addHeader(Constansts.INSTANCE.getVERSION(), Constansts.INSTANCE.getVERSION_CODE());
        builder.addHeader(Constansts.INSTANCE.getTIMESTAMP(), System.currentTimeMillis() + "");
        RequestBody requestBody = null;
        if (originalRequest.body() instanceof FormBody) {
            FormBody.Builder newFormBody = new FormBody.Builder();
            FormBody oidFormBody = (FormBody) originalRequest.body();
            for (int i = 0; i < oidFormBody.size(); i++) {
                newFormBody.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
            }
            requestBody = RequestBody.create(MediaType.parse("application/json"), newFormBody.toString());
            builder.method(originalRequest.method(), newFormBody.build());

            builder.post(requestBody);
            builder.put(requestBody);
        }*/
        if (method.endsWith("GET")) {
            val url = originalRequest.url().newBuilder()
                .addQueryParameter(CHANNEL, SOURCE)
                .addQueryParameter(VERSION, VERSION_CODE)
                .addQueryParameter(TIMESTAMP, System.currentTimeMillis().toString())
                .addQueryParameter(DEBUG, DEBUG_VERSION)
                .addQueryParameter(TOKEN, ticket)
                .addQueryParameter(UID, "$uid")
                .build()
            val request = originalRequest.newBuilder().url(url).build()
            return chain.proceed(request)
        } else if (method.endsWith("POST")) {
            val parameter = "&$TOKEN=$ticket&$UID=$uid"
            val newRequest = interceptRequest(originalRequest, parameter)
            return chain.proceed(newRequest)
        }
        val builder = originalRequest.newBuilder()
        val authorised = builder.build()
        return chain.proceed(authorised)
    }

    private fun alreadyHasAuthorizationHeader(originalRequest: Request): Boolean {
        return null != originalRequest.header("Security-Token")
    }

    @Throws(IOException::class)
    private fun interceptRequest(
        request: Request,
        parameter: String
    ): Request {
        val baos = ByteArrayOutputStream()
        val sink = Okio.sink(baos)
        val bufferedSink = Okio.buffer(sink)
        /**
         * Write old params
         */
        request.body()!!.writeTo(bufferedSink)
        /**
         * write to buffer additional params
         */
        bufferedSink.writeString(parameter, Charset.defaultCharset())
        val newRequestBody = RequestBody.create(
            request.body()!!.contentType(),
            bufferedSink.buffer().readUtf8()
        )
        return request.newBuilder().post(newRequestBody).build()
    }

}