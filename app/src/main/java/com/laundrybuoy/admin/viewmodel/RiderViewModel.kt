package com.laundrybuoy.admin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.UploadedDocsModel
import com.laundrybuoy.admin.model.filter.ServicesModel
import com.laundrybuoy.admin.model.order.GeneralResponse
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.model.rider.*
import com.laundrybuoy.admin.model.transaction.TransactionResponse
import com.laundrybuoy.admin.model.vendor.*
import com.laundrybuoy.admin.repository.RiderRepository
import com.laundrybuoy.admin.repository.VendorRepository
import com.laundrybuoy.admin.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class RiderViewModel @Inject constructor(private val repository: RiderRepository) : ViewModel() {

    val riderProfileLiveData: LiveData<NetworkResult<RiderProfileModel>>
        get() = repository.riderProfileResponse

    val updateRiderLiveData: LiveData<NetworkResult<GeneralResponse>>
        get() = repository.updateRiderResponse

    val uploadDocLiveData: LiveData<NetworkResult<GeneralResponse>>
        get() = repository.uploadDocResponse

    val riderAttendanceLiveData: LiveData<NetworkResult<RiderAttendanceModel>>
        get() = repository.riderAttendanceResponse

    val riderTransactionLiveData: LiveData<NetworkResult<RiderTransactionModel>>
        get() = repository.riderTransactionResponse

    val approveClaimLiveData: LiveData<NetworkResult<RiderClaimApproved>>
        get() = repository.approveClaimResponse

    val markRiderAttendanceLiveData: LiveData<NetworkResult<GeneralResponse>>
        get() = repository.markRiderAttendanceResponse


    val _homeFigYearLiveData = MutableLiveData<String>()
    val riderHomeFigLiveData: LiveData<NetworkResult<RiderHomeFigures>>
        get() = repository.riderHomeFiguresResponse


    private var _markAttendanceList = MutableLiveData<MutableList<RiderAttendanceModel.Data>>()
    val markAttendanceList: LiveData<MutableList<RiderAttendanceModel.Data>>
        get() = _markAttendanceList

    private var _tempAttendanceList = MutableLiveData<MutableList<RiderAttendanceModel.Data>>()

    val _riderTransactionPeriodLD = MutableLiveData<JsonObject>()

    val riderRatingLiveData: LiveData<NetworkResult<VendorRatingResponse>>
        get() = repository.riderRatingResponse

    val _chartYearLiveData = MutableLiveData<String>()
    val riderGraphLiveData: LiveData<NetworkResult<RiderGraphResponse>>
        get() = repository.riderGraphResponse

    private val _riderOrdersLiveData =
        MutableLiveData<PagingData<VendorOrderListingModel.Data.Partner>>()

    init {
        _tempAttendanceList.value = arrayListOf()
    }

    fun getRiderProfile(riderObj: JsonObject) {
        viewModelScope.launch {
            repository.getRiderProfile(riderObj)
        }
    }

    fun updateRider(riderId: String,payload: JsonObject){
        viewModelScope.launch {
            repository.updateRider(riderId,payload)
        }
    }

    fun uploadDocNew2(body: MultipartBody.Part) {
        viewModelScope.launch {
            repository.uploadDocsNew(body)
        }
    }

    fun getRiderAttendance(riderObj: JsonObject) {
        viewModelScope.launch {
            repository.getRiderAttendance(riderObj)
        }
    }

    fun selectedAttendanceDate(attendance : RiderAttendanceModel.Data){
        if(attendance.isSelected){
            _tempAttendanceList.value?.add(attendance)
        }else{
            _tempAttendanceList.value?.remove(attendance)
        }
        _markAttendanceList.postValue(_tempAttendanceList.value)
    }

    fun makeAttendanceNull(){
        _tempAttendanceList.value?.clear()
        _markAttendanceList.postValue(arrayListOf())

    }

    fun getRiderTransaction(riderObj: JsonObject) {
        viewModelScope.launch {
            repository.getRiderTransactions(riderObj)
        }
    }

    fun riderTransactionPeriod(period: JsonObject) {
        _riderTransactionPeriodLD.postValue(period)
    }

    fun approveClaim(riderObj: JsonObject) {
        viewModelScope.launch {
            repository.approveClaim(riderObj)
        }
    }

    fun getRiderRating(payload: JsonObject) {
        viewModelScope.launch {
            repository.getRiderRating(payload)
        }
    }

    fun setHomeFiguresDuration(year: String) {
        _homeFigYearLiveData.postValue(year)
    }

    fun getRiderHomeFigures(payload: JsonObject) {
        viewModelScope.launch {
            repository.getRiderHomeFigures(payload)
        }
    }

    fun markRiderAttendance(payload: ApproveRiderAttendance) {
        viewModelScope.launch {
            repository.markRiderAttendance(payload)
        }
    }
    fun setChartYear(year: String) {
        _chartYearLiveData.postValue(year)
    }

    fun getRiderGraph(payload: JsonObject) {
        viewModelScope.launch {
            repository.getRiderGraph(payload)
        }
    }

    suspend fun getRiderOrdersByMonth(durationPayload: JsonObject): LiveData<PagingData<VendorOrderListingModel.Data.Partner>> {
        val response = repository.getRiderOrdersByMonth(durationPayload).cachedIn(viewModelScope)
        _riderOrdersLiveData.value = response.value
        return response
    }
}