package com.laundrybuoy.admin.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.JsonObject
import com.laundrybuoy.admin.adapter.notification.NotificationListingPagingSource
import com.laundrybuoy.admin.model.NotificationResponse
import com.laundrybuoy.admin.model.home.AdminGraphResponse
import com.laundrybuoy.admin.model.home.HomeFiltersModel
import com.laundrybuoy.admin.model.order.GeneralResponse
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.model.unapprovedvendors.UnApprovedVendorsModel
import com.laundrybuoy.admin.retrofit.AdminAPI
import com.laundrybuoy.admin.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class HomePageRepository @Inject constructor(private val adminAPI: AdminAPI) {

    private val _unapprovedVendorsResponse = MutableLiveData<NetworkResult<UnApprovedVendorsModel>>()
    val unapprovedVendorsResponse: LiveData<NetworkResult<UnApprovedVendorsModel>>
        get() = _unapprovedVendorsResponse

    private val _adminGraphResponse = MutableLiveData<NetworkResult<AdminGraphResponse>>()
    val adminGraphResponse: LiveData<NetworkResult<AdminGraphResponse>>
        get() = _adminGraphResponse

    private val _homeFiguresResponse = MutableLiveData<NetworkResult<HomeFiltersModel>>()
    val homeFiguresResponse: LiveData<NetworkResult<HomeFiltersModel>>
        get() = _homeFiguresResponse

    private val _readAllNotificationResponse = MutableLiveData<NetworkResult<GeneralResponse>>()
    val readAllNotificationResponse: LiveData<NetworkResult<GeneralResponse>>
        get() = _readAllNotificationResponse

    private val _deleteAllNotificationResponse = MutableLiveData<NetworkResult<GeneralResponse>>()
    val deleteAllNotificationResponse: LiveData<NetworkResult<GeneralResponse>>
        get() = _deleteAllNotificationResponse

    suspend fun fetchUnapprovedVendors(pageNo: Long) {
        _unapprovedVendorsResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getUnapprovedVendors(pageNo)
        if(response.isSuccessful && response.body()!=null){
            _unapprovedVendorsResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _unapprovedVendorsResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _unapprovedVendorsResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun fetchAdminGraphData(yearPayload : JsonObject) {
        _adminGraphResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getAdminGraph(yearPayload)
        if(response.isSuccessful && response.body()!=null){
            _adminGraphResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _adminGraphResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _adminGraphResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun getHomeFigures(yearPayload : JsonObject) {
        _homeFiguresResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.getHomeFigures(yearPayload)
        if(response.isSuccessful && response.body()!=null){
            _homeFiguresResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _homeFiguresResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _homeFiguresResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun getNotifications(filter : String): LiveData<PagingData<NotificationResponse.Data.Partner>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                NotificationListingPagingSource(adminAPI,filter)
            }
        ).liveData

    }

    suspend fun readAllNotifications(filter : String) {
        _readAllNotificationResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.readAllNotifications(filter)
        if(response.isSuccessful && response.body()!=null){
            _readAllNotificationResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _readAllNotificationResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _readAllNotificationResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }

    suspend fun deleteAllNotifications(filter : String) {
        _deleteAllNotificationResponse.postValue(NetworkResult.Loading())
        val response = adminAPI.deleteAllNotifications(filter)
        if(response.isSuccessful && response.body()!=null){
            _deleteAllNotificationResponse.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _deleteAllNotificationResponse.postValue((NetworkResult.Error(errorObj.getString("message"))))
        }else{
            _deleteAllNotificationResponse.postValue((NetworkResult.Error("Something went wrong!!")))
        }
    }


}