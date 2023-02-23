package com.laundrybuoy.admin

import android.app.Application
import com.laundrybuoy.admin.utils.SharedPreferenceManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AdminApp : Application() {

    companion object {
        lateinit var instance: AdminApp
        private const val TAG = "ApplicationClass"

        @JvmName("getInstance1")
        fun getInstance(): AdminApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        SharedPreferenceManager.init(instance)
    }

}