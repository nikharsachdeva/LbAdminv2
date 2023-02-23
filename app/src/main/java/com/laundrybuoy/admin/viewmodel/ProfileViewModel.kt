package com.laundrybuoy.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.UploadedDocsModel
import com.laundrybuoy.admin.model.order.EligibleVendorsModel
import com.laundrybuoy.admin.model.profile.*
import com.laundrybuoy.admin.repository.ProfileRepository
import com.laundrybuoy.admin.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository) :
    ViewModel() {

    private val _allListLiveData = MutableLiveData<PagingData<AllListingResponse.Data.Partner>>()

    val eligibleVendorsLiveData: LiveData<NetworkResult<EligibleVendorsModel>>
        get() = repository.eligibleVendorsResponse

    val addQrLiveData: LiveData<NetworkResult<AddQrResponse>>
        get() = repository.addQrCodeResponse

    val deleteQrLiveData: LiveData<NetworkResult<DeleteQrResponse>>
        get() = repository.deleteQrCodeResponse

    val _couponsLiveData = MutableLiveData<MutableList<GetCouponsResponse.Data>>()
    val getCouponsLiveData: LiveData<NetworkResult<GetCouponsResponse>>
        get() = repository.getCouponResponse

    val addCouponLiveData: LiveData<NetworkResult<AddCouponResponse>>
        get() = repository.addCouponResponse

    val disableCouponLiveData: LiveData<NetworkResult<DeleteCouponResponse>>
        get() = repository.disableCouponResponse

    private var _filterSelected = MutableLiveData<String?>()
    val filterSelectedLD: LiveData<String?>
        get() = _filterSelected

    val _qrDocLiveData = MutableLiveData<MutableList<GetQrResponse.Data>>()
    val qrCodesLiveData: LiveData<NetworkResult<GetQrResponse>>
        get() = repository.qrCodesResponse

    val _addedQrImageLiveData = MutableLiveData<String?>()

    val _packagesLiveData = MutableLiveData<MutableList<GetPackagesResponse.Data>>()
    val getPackagesLiveData: LiveData<NetworkResult<GetPackagesResponse>>
        get() = repository.getPackageResponse

    val disablePackageLiveData: LiveData<NetworkResult<DeleteCouponResponse>>
        get() = repository.disablePackageResponse

    val getSubscriptionLiveData: LiveData<NetworkResult<GetSubscriptionResponse>>
        get() = repository.getSubscriptionResponse

    val disableSubscriptionLiveData: LiveData<NetworkResult<DeleteCouponResponse>>
        get() = repository.disableSubscriptionResponse

    val addSubscriptionLiveData: LiveData<NetworkResult<AddSubscriptionResponse>>
        get() = repository.addSubscriptionResponse

    val addPackageLiveData: LiveData<NetworkResult<AddPackageResponse>>
        get() = repository.addPackageResponse

    private var _benefitArray = MutableLiveData<MutableList<String>>()
    val benefitArray: LiveData<MutableList<String>>
        get() = _benefitArray

    val vendorLeaderboardLiveData: LiveData<NetworkResult<LeaderboardResponse>>
        get() = repository.vendorLeaderboardResponse

    val riderLeaderboardLiveData: LiveData<NetworkResult<LeaderboardResponse>>
        get() = repository.riderLeaderboardResponse

    fun setAllFilterSelected(filterSelected: String?=null) {
        _filterSelected.value = filterSelected
    }

    fun getEligibleVendors(orderPayload: JsonObject) {
        viewModelScope.launch {
            repository.getEligibleVendors(orderPayload)
        }
    }

    suspend fun getAllItems(
        filter: String,
        search: String? = null,
        filterSelected: String? = null
    ): LiveData<PagingData<AllListingResponse.Data.Partner>> {

        val response = repository.getAllItems(filter, search,filterSelected).cachedIn(viewModelScope)
        _allListLiveData.value = response.value

        return response
    }

    fun pushUpdatedBenefitList(items: MutableList<String>) {
        _benefitArray.value = items
    }

    fun setQrDocs(docList : MutableList<GetQrResponse.Data>){
        _qrDocLiveData.postValue(docList)
    }

    fun setCouponsDocs(docList : MutableList<GetCouponsResponse.Data>){
        _couponsLiveData.postValue(docList)
    }

    fun setAddedQrImage(qrImage : String?){
        _addedQrImageLiveData.postValue(qrImage)
    }

    fun getAllQrCodes() {
        viewModelScope.launch {
            repository.getAllQrCodes()
        }
    }

    fun addQrCode(payload : AddQrPayload) {
        viewModelScope.launch {
            repository.addQrCode(payload)
        }
    }

    fun deleteQr(qrId : String) {
        viewModelScope.launch {
            repository.deleteQrCode(qrId)
        }
    }

    fun addCoupon(payload : AddCouponPayload) {
        viewModelScope.launch {
            repository.addCouponCode(payload)
        }
    }

    fun getAllCoupons(filter: String) {
        viewModelScope.launch {
            repository.getCouponList(filter)
        }
    }

    fun disableCoupon(payload: JsonObject) {
        viewModelScope.launch {
            repository.disableCoupon(payload)
        }
    }

    fun getAllPackages(filter: String) {
        viewModelScope.launch {
            repository.getPackageList(filter)
        }
    }

    fun disablePackage(payload: JsonObject) {
        viewModelScope.launch {
            repository.disablePackage(payload)
        }
    }

    fun getAllSubscription(filter: String) {
        viewModelScope.launch {
            repository.getSubscriptionList(filter)
        }
    }

    fun disableSubscription(payload: JsonObject) {
        viewModelScope.launch {
            repository.disableSubscription(payload)
        }
    }

    fun addSubscription(payload : AddSubscriptionPayload) {
        viewModelScope.launch {
            repository.addSubscription(payload)
        }
    }

    fun addPackage(payload : AddPackagePayload) {
        viewModelScope.launch {
            repository.addPackage(payload)
        }
    }

    fun getVendorLeaderboard(payload : LeaderboardPayload) {
        viewModelScope.launch {
            repository.getVendorLeaderboard(payload)
        }
    }

    fun getRiderLeaderboard(payload : LeaderboardPayload) {
        viewModelScope.launch {
            repository.getRiderLeaderboard(payload)
        }
    }

}