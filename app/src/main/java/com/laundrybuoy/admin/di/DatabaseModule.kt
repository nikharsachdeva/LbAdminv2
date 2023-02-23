package com.laundrybuoy.admin.di

import android.content.Context
import androidx.room.Room
import com.laundrybuoy.admin.db.LaundryBuoyDB
import com.laundrybuoy.admin.db.SplashDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideLaundryDB(@ApplicationContext context : Context) : LaundryBuoyDB{
        return Room.databaseBuilder(context, LaundryBuoyDB::class.java, "LaundryBuoyDB").build()
    }

    @Singleton
    @Provides
    fun provideSplashDao(db : LaundryBuoyDB) : SplashDAO {
        return db.getSplashDAO()
    }

}