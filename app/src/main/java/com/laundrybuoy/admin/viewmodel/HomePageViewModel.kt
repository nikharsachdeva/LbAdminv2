package com.laundrybuoy.admin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.NotificationResponse
import com.laundrybuoy.admin.model.home.AdminGraphResponse
import com.laundrybuoy.admin.model.home.HomeFiltersModel
import com.laundrybuoy.admin.model.order.GeneralResponse
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.model.unapprovedvendors.UnApprovedVendorsModel
import com.laundrybuoy.admin.repository.HomePageRepository
import com.laundrybuoy.admin.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(private val repository: HomePageRepository) : ViewModel(){

    val unapprovedVendorsLiveData: LiveData<NetworkResult<UnApprovedVendorsModel>>
        get() = repository.unapprovedVendorsResponse

    val _graphYearLiveData = MutableLiveData<Int>()
    val adminGraphLiveData: LiveData<NetworkResult<AdminGraphResponse>>
        get() = repository.adminGraphResponse

    val homeFiguresLiveData: LiveData<NetworkResult<HomeFiltersModel>>
        get() = repository.homeFiguresResponse

    val computedData = MutableLiveData<MutableList<AdminGraphResponse.Data>>()

    private val _notificationLiveData =
        MutableLiveData<PagingData<NotificationResponse.Data.Partner>>()

    val readAllNotificationLiveData: LiveData<NetworkResult<GeneralResponse>>
        get() = repository.readAllNotificationResponse

    val deleteAllNotificationLiveData: LiveData<NetworkResult<GeneralResponse>>
        get() = repository.deleteAllNotificationResponse

    fun fetchUnapprovedVendors(pageNo: Long) {
        viewModelScope.launch {
            repository.fetchUnapprovedVendors(pageNo)
        }
    }

    fun isLastPage(): Boolean {
        return unapprovedVendorsLiveData.value?.data?.data?.nextPage==null
    }


    fun setGraphYear(year : Int){
        _graphYearLiveData.postValue(year)
    }

    fun getAdminGraphData(yearPayload : JsonObject){
        viewModelScope.launch {
            repository.fetchAdminGraphData(yearPayload)
        }
    }

    fun setComputedData(computedList : MutableList<AdminGraphResponse.Data>){
        computedData.value = computedList
    }

    fun getAdminHomeFigures(yearPayload : JsonObject){
        viewModelScope.launch {
            repository.getHomeFigures(yearPayload)
        }
    }

    suspend fun getNotificationListing(filter: String): LiveData<PagingData<NotificationResponse.Data.Partner>> {
        val response = repository.getNotifications(filter).cachedIn(viewModelScope)
        _notificationLiveData.value = response.value
        return response
    }

    fun readAllNotification(filter : String){
        viewModelScope.launch {
            repository.readAllNotifications(filter)
        }
    }

    fun deleteAllNotification(filter : String){
        viewModelScope.launch {
            repository.deleteAllNotifications(filter)
        }
    }

}