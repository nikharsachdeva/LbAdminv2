package com.laundrybuoy.admin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.laundrybuoy.admin.model.splash.SplashDataModel

@Database(
    entities = [SplashDataModel.SplashData::class],
    version = 1
)
@TypeConverters(SplashConverter::class)
abstract class LaundryBuoyDB : RoomDatabase() {

    abstract fun getSplashDAO(): SplashDAO


}