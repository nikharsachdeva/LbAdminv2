package com.laundrybuoy.admin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.auth.RequestOtpResponse
import com.laundrybuoy.admin.model.auth.VerifyOtpResponse
import com.laundrybuoy.admin.model.splash.SplashDataModel
import com.laundrybuoy.admin.retrofit.AdminAPI
import com.laundrybuoy.admin.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class AuthRepository @Inject constructor(private val adminAPI: AdminAPI) {

    private val _requestOTPResponse = MutableLiveData<NetworkResult<RequestOtpResponse>>()
    val requestOTPResponse: LiveData<NetworkResult<RequestOtpResponse>>
        get() = _requestOTPResponse

    private val _verifyOTPResponse = MutableLiveData<NetworkResult<VerifyOtpResponse>>()
    val verifyOTPResponse: LiveData<NetworkResult<VerifyOtpResponse>>
        get() = _verifyOTPResponse

    private val _splashDataResponse = MutableLiveData<NetworkResult<SplashDataModel>>()
    val splashDataResponse: LiveData<NetworkResult<SplashDataModel>>
        get() = _splashDataResponse

    suspend fun requestOTP(otpPayload : JsonObject) {
        _requestOTPResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.requestOTP(otpPayload)
        if(response.isSuccessful && response.body()!=null){
            _requestOTPResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _requestOTPResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _requestOTPResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun verifyOTP(verifyPayload : JsonObject) {
        _verifyOTPResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.verifyOTP(verifyPayload)
        if(response.isSuccessful && response.body()!=null){
            _verifyOTPResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _verifyOTPResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _verifyOTPResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getSplashDataApi() {
        _splashDataResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getSplashData()
        if(response.isSuccessful && response.body()!=null){
            _splashDataResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _splashDataResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _splashDataResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

}