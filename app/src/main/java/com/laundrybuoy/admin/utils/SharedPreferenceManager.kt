package com.laundrybuoy.admin.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.laundrybuoy.admin.R
import io.reactivex.Completable

object SharedPreferenceManager {

    private const val FCM_TOKEN = "fcm_token"
    private const val BEARER_TOKEN = "bearer_token"

    var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    fun getFCMToken(): String {
        return sharedPreferences?.getString(FCM_TOKEN, "").toString()
    }

    fun setFCMToken(value: String) {
        sharedPreferences?.edit {
            putString(
                FCM_TOKEN,
                value
            )
        }
    }

    fun getBearerToken(): String {
        return sharedPreferences?.getString(BEARER_TOKEN, "").toString()
    }

    fun setBearerToken(value: String) {
        sharedPreferences?.edit {
            putString(
                BEARER_TOKEN,
                value
            )
        }
    }

}