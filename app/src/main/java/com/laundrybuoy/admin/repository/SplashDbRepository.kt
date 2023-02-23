package com.laundrybuoy.admin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.laundrybuoy.admin.db.SplashDAO
import com.laundrybuoy.admin.model.splash.SplashDataModel
import javax.inject.Inject

class SplashDbRepository @Inject constructor(
    private val splashDao: SplashDAO
) {

    private val _splashDataResponse = MutableLiveData<SplashDataModel.SplashData>()
    val getSplashLiveDatDb: LiveData<SplashDataModel.SplashData>
        get() = _splashDataResponse

    suspend fun insertSplashData(data: SplashDataModel.SplashData) {
        splashDao.addSplashData(data)
    }

    suspend fun getSplashData() {
        val response = splashDao.getSplashData()
        if(response!=null){
            _splashDataResponse.postValue(response)
        }

    }

}