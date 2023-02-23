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
import com.laundrybuoy.admin.model.customer.CustomerTransactionModel
import com.laundrybuoy.admin.model.filter.ServicesModel
import com.laundrybuoy.admin.model.order.GeneralResponse
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.model.profile.GetCouponsResponse
import com.laundrybuoy.admin.model.profile.GetQrResponse
import com.laundrybuoy.admin.model.rider.*
import com.laundrybuoy.admin.model.transaction.TransactionResponse
import com.laundrybuoy.admin.model.vendor.*
import com.laundrybuoy.admin.repository.CustomerRepository
import com.laundrybuoy.admin.repository.RiderRepository
import com.laundrybuoy.admin.repository.VendorRepository
import com.laundrybuoy.admin.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val repository: CustomerRepository) : ViewModel() {

//     val _customerCoinsLiveData =
//        MutableLiveData<PagingData<CustomerTransactionModel.Data.Partner>>()


    val _customerCoinsLiveData = MutableLiveData<PagingData<CustomerTransactionModel.Data.Partner>>()
    val customerCoinsLiveData: LiveData<PagingData<CustomerTransactionModel.Data.Partner>>
        get() = _customerCoinsLiveData

    suspend fun getCustomerCoins(durationPayload: JsonObject): LiveData<PagingData<CustomerTransactionModel.Data.Partner>> {
        val response = repository.getCustomerCoins(durationPayload).cachedIn(viewModelScope)
        _customerCoinsLiveData.value = response.value
        return response
    }
}