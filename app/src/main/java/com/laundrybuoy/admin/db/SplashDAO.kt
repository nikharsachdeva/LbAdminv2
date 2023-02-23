package com.laundrybuoy.admin.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.laundrybuoy.admin.model.splash.SplashDataModel

@Dao
interface SplashDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSplashData(data : SplashDataModel.SplashData)

    @Query("SELECT * FROM SplashData ORDER BY id DESC LIMIT 1")
    suspend fun getSplashData() : SplashDataModel.SplashData

}