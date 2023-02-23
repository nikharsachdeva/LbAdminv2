package com.laundrybuoy.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.UploadedDocsModel
import com.laundrybuoy.admin.model.filter.ServicesModel
import com.laundrybuoy.admin.model.order.GeneralResponse
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.model.transaction.TransactionResponse
import com.laundrybuoy.admin.model.vendor.*
import com.laundrybuoy.admin.repository.VendorRepository
import com.laundrybuoy.admin.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class VendorViewModel @Inject constructor(private val repository: VendorRepository) : ViewModel() {


    val attendanceLiveData: LiveData<NetworkResult<VendorAttendanceModelNew>>
        get() = repository.attendanceResponse

    val _transactionLiveData =
        MutableLiveData<PagingData<TransactionResponse.Data.Result.Partner>>()
    val _selectedTransactionLiveData =
        MutableLiveData<MutableList<TransactionResponse.Data.Result.Partner>>()

    val vendorTransactionMetaLiveData: LiveData<NetworkResult<TransactionResponse>>
        get() = repository.vendorTransactionMetadataResponse

    val vendorOrderGraphLiveData: LiveData<NetworkResult<VendorOrderGraphModel>>
        get() = repository.vendorOrderGraphResponse

    private val _vendorOrdersLiveData =
        MutableLiveData<PagingData<VendorOrderListingModel.Data.Partner>>()

    val _chartYearLiveData = MutableLiveData<String>()
    val _vendorDocLiveData = MutableLiveData<MutableList<UploadedDocsModel.UploadedDocsModelItem>>()

    val settleVendorLiveData: LiveData<NetworkResult<VendorSettleResponse>>
        get() = repository.settleVendorResponse

    val vendorProfileLiveData: LiveData<NetworkResult<VendorProfileModel>>
        get() = repository.vendorProfileResponse

    val vendorFiguresLiveData: LiveData<NetworkResult<VendorFiguresModel>>
        get() = repository.vendorFiguresResponse

    val vendorWorkAddLiveData: LiveData<NetworkResult<GeneralResponse>>
        get() = repository.updateVendorWorkAddResponse

    private var _pincodeArray = MutableLiveData<MutableList<String>>()
    val pincodeArray: LiveData<MutableList<String>>
        get() = _pincodeArray

    private var _serviceArraySize = MutableLiveData<List<ServicesModel.Data>>()
    val serviceArraySize: LiveData<List<ServicesModel.Data>>
        get() = _serviceArraySize

    val servicesLiveData: LiveData<NetworkResult<ServicesModel>>
        get() = repository.fetchServicesResponse

    val vendorNumberLiveData: LiveData<NetworkResult<GeneralResponse>>
        get() = repository.updateVendorNumberResponse

    val updateVendorLiveData: LiveData<NetworkResult<GeneralResponse>>
        get() = repository.updateVendorResponse

    val uploadDocLiveData: LiveData<NetworkResult<GeneralResponse>>
        get() = repository.uploadDocResponse

    val partnerRatingLiveData: LiveData<NetworkResult<VendorRatingResponse>>
        get() = repository.partnerRatingResponse

    fun fetchServices(){
        viewModelScope.launch {
            repository.fetchServices()
        }
    }

    fun pushUpdatedServiceList(items: List<ServicesModel.Data>) {
        _serviceArraySize.value = items
    }

    fun pushUpdatedPincodeList(items: MutableList<String>) {
        _pincodeArray.value = items
    }

    suspend fun getVendorOrders(durationPayload: JsonObject): LiveData<PagingData<VendorOrderListingModel.Data.Partner>> {
        val response = repository.getVendorOrders(durationPayload).cachedIn(viewModelScope)
        _vendorOrdersLiveData.value = response.value
        return response
    }


    suspend fun getVendorTransaction(
        type: String,
        partnerPayload: JsonObject,
    ): LiveData<PagingData<TransactionResponse.Data.Result.Partner>> {
        val response =
            repository.getVendorTransaction(type, partnerPayload).cachedIn(viewModelScope)
        _transactionLiveData.value = response.value
        return response
    }

    fun setSelectedTransactionList(selectedList: MutableList<TransactionResponse.Data.Result.Partner>) {
        _selectedTransactionLiveData.postValue(selectedList)
    }

    fun getVendorTransactionMeta(type: String, partnerPayload: JsonObject) {
        viewModelScope.launch {
            repository.getVendorTransactionMetadata(type, partnerPayload)
        }
    }

    fun getVendorOrderGraph(partnerPayload: JsonObject) {
        viewModelScope.launch {
            repository.getVendorOrderGraph(partnerPayload)
        }
    }

    fun getAttendance(attendancePayload: JsonObject) {
        viewModelScope.launch {
            repository.getAttendance(attendancePayload)
        }
    }

    fun setChartYear(year: String) {
        _chartYearLiveData.postValue(year)
    }

    fun settleVendor(settleVendorPayload: VendorSettlePayload) {
        viewModelScope.launch {
            repository.settleVendor(settleVendorPayload)
        }
    }

    fun getVendorProfile(vendorObj: JsonObject) {
        viewModelScope.launch {
            repository.getVendorProfile(vendorObj)
        }
    }

    fun getVendorFigures(vendorObj: JsonObject) {
        viewModelScope.launch {
            repository.getVendorFigures(vendorObj)
        }
    }

    fun updateVendorWorkAdd(partnerId: String, addressModel: VendorWorkAddressModel) {
        viewModelScope.launch {
            repository.updateVendorWorkAddress(partnerId, addressModel)
        }
    }

    fun updateVendorNumber(partnerId: String,payload: JsonObject){
        viewModelScope.launch {
            repository.updateVendorNumber(partnerId,payload)
        }
    }

    fun updateVendor(partnerId: String,payload: JsonObject){
        viewModelScope.launch {
            repository.updateVendor(partnerId,payload)
        }
    }

    fun setVendorDocs(docList : MutableList<UploadedDocsModel.UploadedDocsModelItem>){
        _vendorDocLiveData.postValue(docList)
    }

    fun uploadDocNew2(body: MultipartBody.Part) {
        viewModelScope.launch {
            repository.uploadDocsNew(body)
        }
    }

    fun getPartnerRating(payload: JsonObject) {
        viewModelScope.launch {
            repository.getPartnerRating(payload)
        }
    }


}