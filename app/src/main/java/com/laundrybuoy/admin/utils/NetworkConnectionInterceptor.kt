package com.laundrybuoy.admin.utils

import okhttp3.Interceptor
import okhttp3.Response
import java.lang.Exception

class NetworkConnectionInterceptor() : Interceptor {

    val status_connected = 0

    override fun intercept(chain: Interceptor.Chain): Response {

        if (isInternetActiveWithPing() != status_connected) {
            throw NoInternetException("Please make sure you have an active internet connection!")
        }

        return chain.proceed(chain.request())


    }

    private fun isInternetActiveWithPing(): Int {
        return try {
            val runtime = Runtime.getRuntime()
            val process = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            return process.waitFor()
        } catch (ex: Exception) {
            -1
        }
    }
}