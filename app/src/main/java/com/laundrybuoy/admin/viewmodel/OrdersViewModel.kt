package com.laundrybuoy.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.order.*
import com.laundrybuoy.admin.repository.OrderRepository
import com.laundrybuoy.admin.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    private var searchJob: Job? = null

    val searchLiveData: LiveData<NetworkResult<SearchResultModel>>
        get() = repository.searchResponse

    val orderDetailLiveData: LiveData<NetworkResult<OrderDetailResponse>>
        get() = repository.orderDetailResponse

    val getConcernLiveData: LiveData<NetworkResult<GetConcernResponse>>
        get() = repository.getConcernResponse

    val getNotesLiveData: LiveData<NetworkResult<GetNotesResponse>>
        get() = repository.getNotesResponse

    val updateRiderLiveData: LiveData<NetworkResult<GeneralResponse>>
        get() = repository.updateRiderResponse

    val updatePartnerLiveData: LiveData<NetworkResult<GeneralResponse>>
        get() = repository.updatePartnerResponse

    private val _orderListingLiveData =
        MutableLiveData<PagingData<AllOrderListModel.Data.Partner>>()

    fun searchAll(searchable: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            repository.searchApi(searchable)
        }
    }

    fun getDetailedOrder(orderId: String) {
        viewModelScope.launch {
            repository.getDetailedOrder(orderId)
        }
    }


    fun cancelSearchJob(){
        searchJob?.cancel()
    }

    fun getConcernsOrder(concernPayload: JsonObject) {
        viewModelScope.launch {
            repository.getConcern(concernPayload)
        }
    }

    fun getNotesOrder(notesPayload: JsonObject) {
        viewModelScope.launch {
            repository.getNotes(notesPayload)
        }
    }

    fun updateRider(orderPayload: JsonObject) {
        viewModelScope.launch {
            repository.updateRider(orderPayload)
        }
    }

    fun updatePartner(orderPayload: JsonObject) {
        viewModelScope.launch {
            repository.updatePartner(orderPayload)
        }
    }

    suspend fun getRiderOrderListing(payload: JsonObject): LiveData<PagingData<AllOrderListModel.Data.Partner>> {
        val response = repository.getRiderOrderListing(payload).cachedIn(viewModelScope)
        _orderListingLiveData.value = response.value
        return response
    }

    suspend fun getPartnerOrderListing(payload: JsonObject): LiveData<PagingData<AllOrderListModel.Data.Partner>> {
        val response = repository.getPartnerOrderListing(payload).cachedIn(viewModelScope)
        _orderListingLiveData.value = response.value
        return response
    }

    suspend fun getHomeOrderListing(payload: JsonObject): LiveData<PagingData<AllOrderListModel.Data.Partner>> {
        val response = repository.getHomeOrderListing(payload).cachedIn(viewModelScope)
        _orderListingLiveData.value = response.value
        return response
    }
}