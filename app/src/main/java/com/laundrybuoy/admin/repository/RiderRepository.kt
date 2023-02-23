package com.laundrybuoy.admin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.JsonObject
import com.laundrybuoy.admin.adapter.order.paging.PartnerOrderPagingSource
import com.laundrybuoy.admin.adapter.rider.RiderOrderPagingSource
import com.laundrybuoy.admin.adapter.transaction.VendorTransactionPagingSource
import com.laundrybuoy.admin.model.filter.ServicesModel
import com.laundrybuoy.admin.model.order.GeneralResponse
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.model.rider.*
import com.laundrybuoy.admin.model.transaction.TransactionResponse
import com.laundrybuoy.admin.model.vendor.*
import com.laundrybuoy.admin.retrofit.AdminAPI
import com.laundrybuoy.admin.utils.NetworkResult
import okhttp3.MultipartBody
import org.json.JSONObject
import javax.inject.Inject

class RiderRepository @Inject constructor(private val adminAPI: AdminAPI) {

    private val _riderProfileResponse = MutableLiveData<NetworkResult<RiderProfileModel>>()
    val riderProfileResponse: LiveData<NetworkResult<RiderProfileModel>>
        get() = _riderProfileResponse

    private val _updateRiderResponse = MutableLiveData<NetworkResult<GeneralResponse>>()
    val updateRiderResponse: LiveData<NetworkResult<GeneralResponse>>
        get() = _updateRiderResponse

    private val _uploadDocResponse = MutableLiveData<NetworkResult<GeneralResponse>>()
    val uploadDocResponse: LiveData<NetworkResult<GeneralResponse>>
        get() = _uploadDocResponse

    private val _riderAttendanceResponse = MutableLiveData<NetworkResult<RiderAttendanceModel>>()
    val riderAttendanceResponse: LiveData<NetworkResult<RiderAttendanceModel>>
        get() = _riderAttendanceResponse

    private val _riderTransactionResponse = MutableLiveData<NetworkResult<RiderTransactionModel>>()
    val riderTransactionResponse: LiveData<NetworkResult<RiderTransactionModel>>
        get() = _riderTransactionResponse

    private val _approveClaimResponse = MutableLiveData<NetworkResult<RiderClaimApproved>>()
    val approveClaimResponse: LiveData<NetworkResult<RiderClaimApproved>>
        get() = _approveClaimResponse

    private val _riderRatingResponse = MutableLiveData<NetworkResult<VendorRatingResponse>>()
    val riderRatingResponse: LiveData<NetworkResult<VendorRatingResponse>>
        get() = _riderRatingResponse

    private val _riderHomeFiguresResponse = MutableLiveData<NetworkResult<RiderHomeFigures>>()
    val riderHomeFiguresResponse: LiveData<NetworkResult<RiderHomeFigures>>
        get() = _riderHomeFiguresResponse

    private val _markRiderAttendanceResponse = MutableLiveData<NetworkResult<GeneralResponse>>()
    val markRiderAttendanceResponse: LiveData<NetworkResult<GeneralResponse>>
        get() = _markRiderAttendanceResponse

    private val _riderGraphResponse = MutableLiveData<NetworkResult<RiderGraphResponse>>()
    val riderGraphResponse: LiveData<NetworkResult<RiderGraphResponse>>
        get() = _riderGraphResponse

    suspend fun getRiderProfile(payload : JsonObject) {
        _riderProfileResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getRiderProfile(payload)
        if(response.isSuccessful && response.body()!=null){
            _riderProfileResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _riderProfileResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _riderProfileResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun updateRider(riderId : String,payload: JsonObject) {
        _updateRiderResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.updateRider(riderId,payload)
        if(response.isSuccessful && response.body()!=null){
            _updateRiderResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _updateRiderResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _updateRiderResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun uploadDocsNew(body: MultipartBody.Part) {
        _uploadDocResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.uploadDocsNew2(body)
        if (response.isSuccessful && response.body() != null) {
            _uploadDocResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _uploadDocResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        } else {
            _uploadDocResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getRiderAttendance(payload: JsonObject) {
        _riderAttendanceResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getRiderAttendance(payload)
        if (response.isSuccessful && response.body() != null) {
            _riderAttendanceResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _riderAttendanceResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        } else {
            _riderAttendanceResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getRiderTransactions(payload: JsonObject) {
        _riderTransactionResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getRiderTransaction(payload)
        if (response.isSuccessful && response.body() != null) {
            _riderTransactionResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _riderTransactionResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        } else {
            _riderTransactionResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }


    suspend fun approveClaim(payload: JsonObject) {
        _approveClaimResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.approveClaim(payload)
        if (response.isSuccessful && response.body() != null) {
            _approveClaimResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _approveClaimResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        } else {
            _approveClaimResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getRiderRating(payload: JsonObject) {
        _riderRatingResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getRiderRating(payload)
        if(response.isSuccessful && response.body()!=null){
            _riderRatingResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _riderRatingResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _riderRatingResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getRiderHomeFigures(payload: JsonObject) {
        _riderHomeFiguresResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getRiderHomeFigures(payload)
        if(response.isSuccessful && response.body()!=null){
            _riderHomeFiguresResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _riderHomeFiguresResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _riderHomeFiguresResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun markRiderAttendance(payload: ApproveRiderAttendance) {
        _markRiderAttendanceResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.markRiderAttendance(payload)
        if(response.isSuccessful && response.body()!=null){
            _markRiderAttendanceResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _markRiderAttendanceResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _markRiderAttendanceResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getRiderGraph(payload: JsonObject) {
        _riderGraphResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getRiderChartData(payload)
        if(response.isSuccessful && response.body()!=null){
            _riderGraphResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _riderGraphResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _riderGraphResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getRiderOrdersByMonth(datePayload : JsonObject): LiveData<PagingData<VendorOrderListingModel.Data.Partner>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                RiderOrderPagingSource(adminAPI,datePayload)
            }
        ).liveData
    }


}