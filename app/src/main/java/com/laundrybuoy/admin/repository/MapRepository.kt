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
import com.laundrybuoy.admin.model.map.MapCoordinatesModel
import com.laundrybuoy.admin.model.order.GeneralResponse
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.model.transaction.TransactionResponse
import com.laundrybuoy.admin.model.vendor.*
import com.laundrybuoy.admin.retrofit.AdminAPI
import com.laundrybuoy.admin.utils.NetworkResult
import okhttp3.MultipartBody
import org.json.JSONObject
import javax.inject.Inject

class MapRepository @Inject constructor(private val adminAPI: AdminAPI) {

    private val _fetchCoordinatesResponse = MutableLiveData<NetworkResult<MapCoordinatesModel>>()
    val fetchCoordinatesResponse: LiveData<NetworkResult<MapCoordinatesModel>>
        get() = _fetchCoordinatesResponse


    suspend fun getCoordinates(role : String) {
        _fetchCoordinatesResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getCoordinates(role)
        if(response.isSuccessful && response.body()!=null){
            _fetchCoordinatesResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _fetchCoordinatesResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _fetchCoordinatesResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }

    }

}