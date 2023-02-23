package com.laundrybuoy.admin.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.laundrybuoy.admin.model.splash.SplashDataModel


class SplashConverter {
    @TypeConverter
    fun listToJson(value: List<SplashDataModel.SplashData.FilterObj>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<SplashDataModel.SplashData.FilterObj>::class.java).toList()
}