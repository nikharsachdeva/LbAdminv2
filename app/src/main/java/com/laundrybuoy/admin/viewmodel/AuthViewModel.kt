package com.laundrybuoy.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.auth.RequestOtpResponse
import com.laundrybuoy.admin.model.auth.VerifyOtpResponse
import com.laundrybuoy.admin.model.splash.SplashDataModel
import com.laundrybuoy.admin.repository.AuthRepository
import com.laundrybuoy.admin.repository.SplashDbRepository
import com.laundrybuoy.admin.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val splashRepository: SplashDbRepository,
) : ViewModel() {

    val requestOTPLiveData: LiveData<NetworkResult<RequestOtpResponse>>
        get() = repository.requestOTPResponse

    val verifyOTPLiveData: LiveData<NetworkResult<VerifyOtpResponse>>
        get() = repository.verifyOTPResponse

    val splashDataLiveData: LiveData<NetworkResult<SplashDataModel>>
        get() = repository.splashDataResponse

    val splashDataDb: LiveData<SplashDataModel.SplashData>
        get() = splashRepository.getSplashLiveDatDb

    private var _otpInfo = MutableLiveData<JsonObject>()
    val otpInfo: LiveData<JsonObject>
        get() = _otpInfo

    fun setOtpInfo(otpInfo: JsonObject) {
        _otpInfo.value = otpInfo
    }

    fun requestOTP(otpPayload: JsonObject) {
        viewModelScope.launch {
            repository.requestOTP(otpPayload)
        }
    }

    fun verifyOTP(verifyPayload: JsonObject) {
        viewModelScope.launch {
            repository.verifyOTP(verifyPayload)
        }
    }

    fun getSplashDataDb() {
        viewModelScope.launch {
            splashRepository.getSplashData()
        }
    }

    suspend fun saveSplashDataDb(data: SplashDataModel.SplashData) {
        splashRepository.insertSplashData(data)
    }

    fun getSplashDataApi() {
        viewModelScope.launch {
            repository.getSplashDataApi()
        }
    }

}