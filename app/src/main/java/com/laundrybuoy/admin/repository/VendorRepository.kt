package com.laundrybuoy.admin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.JsonObject
import com.laundrybuoy.admin.adapter.order.paging.PartnerOrderPagingSource
import com.laundrybuoy.admin.adapter.transaction.VendorTransactionPagingSource
import com.laundrybuoy.admin.model.filter.ServicesModel
import com.laundrybuoy.admin.model.order.GeneralResponse
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.model.transaction.TransactionResponse
import com.laundrybuoy.admin.model.vendor.*
import com.laundrybuoy.admin.retrofit.AdminAPI
import com.laundrybuoy.admin.utils.NetworkResult
import okhttp3.MultipartBody
import org.json.JSONObject
import javax.inject.Inject

class VendorRepository @Inject constructor(private val adminAPI: AdminAPI) {

    private val _attendanceResponse = MutableLiveData<NetworkResult<VendorAttendanceModelNew>>()
    val attendanceResponse: LiveData<NetworkResult<VendorAttendanceModelNew>>
        get() = _attendanceResponse

    private val _vendorTransactionMetadataResponse = MutableLiveData<NetworkResult<TransactionResponse>>()
    val vendorTransactionMetadataResponse: LiveData<NetworkResult<TransactionResponse>>
        get() = _vendorTransactionMetadataResponse

    private val _vendorOrderGraphResponse = MutableLiveData<NetworkResult<VendorOrderGraphModel>>()
    val vendorOrderGraphResponse: LiveData<NetworkResult<VendorOrderGraphModel>>
        get() = _vendorOrderGraphResponse

    private val _settleVendorResponse = MutableLiveData<NetworkResult<VendorSettleResponse>>()
    val settleVendorResponse: LiveData<NetworkResult<VendorSettleResponse>>
        get() = _settleVendorResponse

    private val _vendorProfileResponse = MutableLiveData<NetworkResult<VendorProfileModel>>()
    val vendorProfileResponse: LiveData<NetworkResult<VendorProfileModel>>
        get() = _vendorProfileResponse

    private val _vendorFiguresResponse = MutableLiveData<NetworkResult<VendorFiguresModel>>()
    val vendorFiguresResponse: LiveData<NetworkResult<VendorFiguresModel>>
        get() = _vendorFiguresResponse

    private val _updateVendorWorkAddResponse = MutableLiveData<NetworkResult<GeneralResponse>>()
    val updateVendorWorkAddResponse: LiveData<NetworkResult<GeneralResponse>>
        get() = _updateVendorWorkAddResponse


    private val _fetchServicesResponse = MutableLiveData<NetworkResult<ServicesModel>>()
    val fetchServicesResponse: LiveData<NetworkResult<ServicesModel>>
        get() = _fetchServicesResponse

    private val _updateVendorNumberResponse = MutableLiveData<NetworkResult<GeneralResponse>>()
    val updateVendorNumberResponse: LiveData<NetworkResult<GeneralResponse>>
        get() = _updateVendorNumberResponse

    private val _updateVendorResponse = MutableLiveData<NetworkResult<GeneralResponse>>()
    val updateVendorResponse: LiveData<NetworkResult<GeneralResponse>>
        get() = _updateVendorResponse

    private val _partnerRatingResponse = MutableLiveData<NetworkResult<VendorRatingResponse>>()
    val partnerRatingResponse: LiveData<NetworkResult<VendorRatingResponse>>
        get() = _partnerRatingResponse

    private val _uploadDocResponse = MutableLiveData<NetworkResult<GeneralResponse>>()
    val uploadDocResponse: LiveData<NetworkResult<GeneralResponse>>
        get() = _uploadDocResponse

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

    suspend fun fetchServices() {
        _fetchServicesResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.fetchServices()
        if(response.isSuccessful && response.body()!=null){
            _fetchServicesResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _fetchServicesResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _fetchServicesResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getVendorTransaction(type: String, partnerPayload: JsonObject): LiveData<PagingData<TransactionResponse.Data.Result.Partner>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                VendorTransactionPagingSource(adminAPI,type,partnerPayload)
            }
        ).liveData
    }

    suspend fun getVendorOrders(datePayload : JsonObject): LiveData<PagingData<VendorOrderListingModel.Data.Partner>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                PartnerOrderPagingSource(adminAPI,datePayload)
            }
        ).liveData
    }

    suspend fun getVendorTransactionMetadata(type: String, partnerPayload: JsonObject) {
        _vendorTransactionMetadataResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getVendorTransactionMetadata(type,partnerPayload)
        if(response.isSuccessful && response.body()!=null){
            _vendorTransactionMetadataResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _vendorTransactionMetadataResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _vendorTransactionMetadataResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getVendorOrderGraph(partnerPayload: JsonObject) {
        _vendorOrderGraphResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getPartnerGraphData(partnerPayload)
        if(response.isSuccessful && response.body()!=null){
            _vendorOrderGraphResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _vendorOrderGraphResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _vendorOrderGraphResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getAttendance(attendancePayload : JsonObject) {
        _attendanceResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getAttendance(attendancePayload)
        if(response.isSuccessful && response.body()!=null){
            _attendanceResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _attendanceResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _attendanceResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun settleVendor(settlePayload : VendorSettlePayload) {
        _settleVendorResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.settlePartner(settlePayload)
        if(response.isSuccessful && response.body()!=null){
            _settleVendorResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _settleVendorResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _settleVendorResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getVendorProfile(payload : JsonObject) {
        _vendorProfileResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getVendorProfile(payload)
        if(response.isSuccessful && response.body()!=null){
            _vendorProfileResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _vendorProfileResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _vendorProfileResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getVendorFigures(payload : JsonObject) {
        _vendorFiguresResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getVendorFigures(payload)
        if(response.isSuccessful && response.body()!=null){
            _vendorFiguresResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _vendorFiguresResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _vendorFiguresResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun updateVendorWorkAddress(vendorId : String,workAddress : VendorWorkAddressModel) {
        _updateVendorWorkAddResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.updateVendorWorkAddress(vendorId,workAddress)
        if(response.isSuccessful && response.body()!=null){
            _updateVendorWorkAddResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _updateVendorWorkAddResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _updateVendorWorkAddResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun updateVendorNumber(vendorId : String,payload: JsonObject) {
        _updateVendorNumberResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.updateVendorNumber(vendorId,payload)
        if(response.isSuccessful && response.body()!=null){
            _updateVendorNumberResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _updateVendorNumberResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _updateVendorNumberResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun updateVendor(vendorId : String,payload: JsonObject) {
        _updateVendorResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.updateVendor(vendorId,payload)
        if(response.isSuccessful && response.body()!=null){
            _updateVendorResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _updateVendorResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _updateVendorResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

    suspend fun getPartnerRating(payload: JsonObject) {
        _partnerRatingResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getPartnerRating(payload)
        if(response.isSuccessful && response.body()!=null){
            _partnerRatingResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _partnerRatingResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _partnerRatingResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

}