package com.enes.appleapi.api

import android.content.Context
import com.enes.appleapi.BuildConfig
import com.enes.appleapi.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedInputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit
import java.io.IOException
import java.lang.AssertionError
import java.security.*
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*


class RetrofitManager {
    companion object {
        private const val REQUEST_TIMEOUT = 20
        private var client: OkHttpClient? = null
        fun getRetrofit(context: Context): ServiceProtocol {
            var retrofit: Retrofit? = null
            if (retrofit == null) {
                val okHttpBuilder: OkHttpClient.Builder = OkHttpClient().newBuilder()
                    .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                if (BuildConfig.DEBUG) {
                    val interceptor = HttpLoggingInterceptor()
                    interceptor.apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    okHttpBuilder.addInterceptor(interceptor)
                }

                client = okHttpBuilder.build()
                retrofit = Retrofit.Builder()
                    .baseUrl(Host.BASE)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                return retrofit.create(ServiceProtocol::class.java)
            } else {
                return retrofit.create(ServiceProtocol::class.java)
            }
        }

    }
}