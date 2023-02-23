package com.laundrybuoy.admin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.JsonObject
import com.laundrybuoy.admin.adapter.order.paging.*
import com.laundrybuoy.admin.model.order.*
import com.laundrybuoy.admin.retrofit.AdminAPI
import com.laundrybuoy.admin.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class OrderRepository @Inject constructor(private val adminAPI: AdminAPI) {

    private val _searchResponse = MutableLiveData<NetworkResult<SearchResultModel>>()
    val searchResponse: LiveData<NetworkResult<SearchResultModel>>
        get() = _searchResponse

    private val _orderDetailResponse = MutableLiveData<NetworkResult<OrderDetailResponse>>()
    val orderDetailResponse: LiveData<NetworkResult<OrderDetailResponse>>
        get() = _orderDetailResponse

    private val _getConcernResponse = MutableLiveData<NetworkResult<GetConcernResponse>>()
    val getConcernResponse: LiveData<NetworkResult<GetConcernResponse>>
        get() = _getConcernResponse

    private val _getNotesResponse = MutableLiveData<NetworkResult<GetNotesResponse>>()
    val getNotesResponse: LiveData<NetworkResult<GetNotesResponse>>
        get() = _getNotesResponse

    private val _updateRiderResponse = MutableLiveData<NetworkResult<GeneralResponse>>()
    val updateRiderResponse: LiveData<NetworkResult<GeneralResponse>>
        get() = _updateRiderResponse

    private val _updatePartnerResponse = MutableLiveData<NetworkResult<GeneralResponse>>()
    val updatePartnerResponse: LiveData<NetworkResult<GeneralResponse>>
        get() = _updatePartnerResponse


    suspend fun searchApi(searchable : String) {
        _searchResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.searchAll(searchable)
        if(response.isSuccessful && response.body()!=null){
            _searchResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _searchResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _searchResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun getDetailedOrder(orderId : String) {
        _orderDetailResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getOrderDetails(orderId)
        if(response.isSuccessful && response.body()!=null){
            _orderDetailResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _orderDetailResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _orderDetailResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun getConcern(concernPayload: JsonObject) {
        _getConcernResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getConcern(concernPayload)
        if(response.isSuccessful && response.body()!=null){
            _getConcernResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _getConcernResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _getConcernResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun getNotes(notesPayload: JsonObject) {
        _getNotesResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getNotes(notesPayload)
        if(response.isSuccessful && response.body()!=null){
            _getNotesResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _getNotesResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _getNotesResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun updateRider(orderPayload: JsonObject) {
        _updateRiderResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.updateRider(orderPayload)
        if(response.isSuccessful && response.body()!=null){
            _updateRiderResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _updateRiderResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _updateRiderResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun updatePartner(orderPayload: JsonObject) {
        _updatePartnerResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.updateVendor(orderPayload)
        if(response.isSuccessful && response.body()!=null){
            _updatePartnerResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            _updatePartnerResponse.postValue((NetworkResult.Error(response.code().toString())))
        }else{
            _updatePartnerResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun getRiderOrderListing(datePayload : JsonObject): LiveData<PagingData<AllOrderListModel.Data.Partner>> {
            return Pager(
                config = PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true
                ),
                pagingSourceFactory = {
                    RiderAllOrderListingPs(adminAPI,datePayload)
                }
            ).liveData

    }

    suspend fun getPartnerOrderListing(datePayload : JsonObject): LiveData<PagingData<AllOrderListModel.Data.Partner>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                PartnerAllOrderListingPs(adminAPI,datePayload)
            }
        ).liveData

    }

    suspend fun getHomeOrderListing(datePayload : JsonObject): LiveData<PagingData<AllOrderListModel.Data.Partner>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                HomeAllOrderListingPs(adminAPI,datePayload)
            }
        ).liveData

    }

}