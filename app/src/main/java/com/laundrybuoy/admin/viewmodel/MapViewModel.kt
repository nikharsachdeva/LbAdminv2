package com.laundrybuoy.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.auth.RequestOtpResponse
import com.laundrybuoy.admin.model.auth.VerifyOtpResponse
import com.laundrybuoy.admin.model.map.MapCoordinatesModel
import com.laundrybuoy.admin.model.splash.SplashDataModel
import com.laundrybuoy.admin.repository.AuthRepository
import com.laundrybuoy.admin.repository.MapRepository
import com.laundrybuoy.admin.repository.SplashDbRepository
import com.laundrybuoy.admin.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: MapRepository) : ViewModel() {

    val mapCoordinatesLiveData: LiveData<NetworkResult<MapCoordinatesModel>>
        get() = repository.fetchCoordinatesResponse

    fun getMapCoordinates(role : String) {
        viewModelScope.launch {
            repository.getCoordinates(role)
        }
    }

}