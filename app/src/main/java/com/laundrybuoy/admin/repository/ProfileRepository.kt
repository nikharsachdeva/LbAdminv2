package com.laundrybuoy.admin.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.JsonObject
import com.laundrybuoy.admin.adapter.all.AllItemPagingSource
import com.laundrybuoy.admin.model.auth.RequestOtpResponse
import com.laundrybuoy.admin.model.auth.VerifyOtpResponse
import com.laundrybuoy.admin.model.order.EligibleVendorsModel
import com.laundrybuoy.admin.model.profile.*
import com.laundrybuoy.admin.model.vendor.VendorAttendanceModelNew
import com.laundrybuoy.admin.retrofit.AdminAPI
import com.laundrybuoy.admin.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val adminAPI: AdminAPI) {


    private val _eligibleVendorsResponse = MutableLiveData<NetworkResult<EligibleVendorsModel>>()
    val eligibleVendorsResponse: LiveData<NetworkResult<EligibleVendorsModel>>
        get() = _eligibleVendorsResponse

    private val _qrCodesResponse = MutableLiveData<NetworkResult<GetQrResponse>>()
    val qrCodesResponse: LiveData<NetworkResult<GetQrResponse>>
        get() = _qrCodesResponse

    private val _addQrCodeResponse = MutableLiveData<NetworkResult<AddQrResponse>>()
    val addQrCodeResponse: LiveData<NetworkResult<AddQrResponse>>
        get() = _addQrCodeResponse

    private val _addCouponResponse = MutableLiveData<NetworkResult<AddCouponResponse>>()
    val addCouponResponse: LiveData<NetworkResult<AddCouponResponse>>
        get() = _addCouponResponse

    private val _deleteQrCodeResponse = MutableLiveData<NetworkResult<DeleteQrResponse>>()
    val deleteQrCodeResponse: LiveData<NetworkResult<DeleteQrResponse>>
        get() = _deleteQrCodeResponse

    private val _disableCouponResponse = MutableLiveData<NetworkResult<DeleteCouponResponse>>()
    val disableCouponResponse: LiveData<NetworkResult<DeleteCouponResponse>>
        get() = _disableCouponResponse

    private val _getCouponResponse = MutableLiveData<NetworkResult<GetCouponsResponse>>()
    val getCouponResponse: LiveData<NetworkResult<GetCouponsResponse>>
        get() = _getCouponResponse

    private val _getPackageResponse = MutableLiveData<NetworkResult<GetPackagesResponse>>()
    val getPackageResponse: LiveData<NetworkResult<GetPackagesResponse>>
        get() = _getPackageResponse

    private val _disablePackageResponse = MutableLiveData<NetworkResult<DeleteCouponResponse>>()
    val disablePackageResponse: LiveData<NetworkResult<DeleteCouponResponse>>
        get() = _disablePackageResponse

    private val _getSubscriptionResponse = MutableLiveData<NetworkResult<GetSubscriptionResponse>>()
    val getSubscriptionResponse: LiveData<NetworkResult<GetSubscriptionResponse>>
        get() = _getSubscriptionResponse

    private val _disableSubscriptionResponse = MutableLiveData<NetworkResult<DeleteCouponResponse>>()
    val disableSubscriptionResponse: LiveData<NetworkResult<DeleteCouponResponse>>
        get() = _disableSubscriptionResponse

    private val _addSubscriptionResponse = MutableLiveData<NetworkResult<AddSubscriptionResponse>>()
    val addSubscriptionResponse: LiveData<NetworkResult<AddSubscriptionResponse>>
        get() = _addSubscriptionResponse

    private val _addPackageResponse = MutableLiveData<NetworkResult<AddPackageResponse>>()
    val addPackageResponse: LiveData<NetworkResult<AddPackageResponse>>
        get() = _addPackageResponse

    private val _vendorLeaderboardResponse = MutableLiveData<NetworkResult<LeaderboardResponse>>()
    val vendorLeaderboardResponse: LiveData<NetworkResult<LeaderboardResponse>>
        get() = _vendorLeaderboardResponse

    private val _riderLeaderboardResponse = MutableLiveData<NetworkResult<LeaderboardResponse>>()
    val riderLeaderboardResponse: LiveData<NetworkResult<LeaderboardResponse>>
        get() = _riderLeaderboardResponse

    suspend fun getEligibleVendors(orderPayload: JsonObject) {
        _eligibleVendorsResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getEligibleVendors(orderPayload)
        if(response.isSuccessful && response.body()!=null){
            _eligibleVendorsResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _eligibleVendorsResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _eligibleVendorsResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }
    
    suspend fun getAllItems(filter: String, search : String?=null,filterSelected : String?=null): LiveData<PagingData<AllListingResponse.Data.Partner>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                AllItemPagingSource(adminAPI,filter,search,filterSelected)
            }
        ).liveData
    }

    suspend fun getAllQrCodes() {
        _qrCodesResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getAllQrCodes()
        if(response.isSuccessful && response.body()!=null){
            _qrCodesResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _qrCodesResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _qrCodesResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun addQrCode(payload: AddQrPayload) {
        _addQrCodeResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.addQrCodes(payload)
        if(response.isSuccessful && response.body()!=null){
            _addQrCodeResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _addQrCodeResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _addQrCodeResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun deleteQrCode(qrId: String) {
        _deleteQrCodeResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.deleteQr(qrId)
        if(response.isSuccessful && response.body()!=null){
            _deleteQrCodeResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _deleteQrCodeResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _deleteQrCodeResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun addCouponCode(payload: AddCouponPayload) {
        _addCouponResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.addCoupon(payload)
        if(response.isSuccessful && response.body()!=null){
            _addCouponResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _addCouponResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _addCouponResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun getCouponList(filter : String) {
        _getCouponResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getAllCoupons(filter)
        if(response.isSuccessful && response.body()!=null){
            _getCouponResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _getCouponResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _getCouponResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun disableCoupon(payload: JsonObject) {
        _disableCouponResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.disableCoupon(payload)
        if(response.isSuccessful && response.body()!=null){
            _disableCouponResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _disableCouponResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _disableCouponResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun getPackageList(filter : String) {
        _getPackageResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getPackages(filter)
        if(response.isSuccessful && response.body()!=null){
            _getPackageResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _getPackageResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _getPackageResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun disablePackage(payload: JsonObject) {
        _disablePackageResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.disablePackage(payload)
        if(response.isSuccessful && response.body()!=null){
            _disablePackageResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _disablePackageResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _disablePackageResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun getSubscriptionList(filter : String) {
        _getSubscriptionResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getSubscription(filter)
        if(response.isSuccessful && response.body()!=null){
            _getSubscriptionResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _getSubscriptionResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _getSubscriptionResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun disableSubscription(payload: JsonObject) {
        _disableSubscriptionResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.disableSubscription(payload)
        if(response.isSuccessful && response.body()!=null){
            _disableSubscriptionResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _disableSubscriptionResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _disableSubscriptionResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun addSubscription(payload: AddSubscriptionPayload) {
        _addSubscriptionResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.addSubscription(payload)
        if(response.isSuccessful && response.body()!=null){
            _addSubscriptionResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _addSubscriptionResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _addSubscriptionResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun addPackage(payload: AddPackagePayload) {
        _addPackageResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.addPackage(payload)
        if(response.isSuccessful && response.body()!=null){
            _addPackageResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _addPackageResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _addPackageResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun getVendorLeaderboard(payload: LeaderboardPayload) {
        _vendorLeaderboardResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getPartnerRankings(payload)
        if(response.isSuccessful && response.body()!=null){
            _vendorLeaderboardResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _vendorLeaderboardResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _vendorLeaderboardResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun getRiderLeaderboard(payload: LeaderboardPayload) {
        _riderLeaderboardResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getRiderRankings(payload)
        if(response.isSuccessful && response.body()!=null){
            _riderLeaderboardResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _riderLeaderboardResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _riderLeaderboardResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

}