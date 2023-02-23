package com.laundrybuoy.admin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.JsonObject
import com.laundrybuoy.admin.adapter.customer.CustomerCoinsPagingSource
import com.laundrybuoy.admin.adapter.order.paging.*
import com.laundrybuoy.admin.model.customer.CustomerTransactionModel
import com.laundrybuoy.admin.model.order.*
import com.laundrybuoy.admin.retrofit.AdminAPI
import com.laundrybuoy.admin.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class CustomerRepository @Inject constructor(private val adminAPI: AdminAPI) {

    suspend fun getCustomerCoins(userPayload : JsonObject): LiveData<PagingData<CustomerTransactionModel.Data.Partner>> {
            return Pager(
                config = PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true
                ),
                pagingSourceFactory = {
                    CustomerCoinsPagingSource(adminAPI,userPayload)
                }
            ).liveData

    }

}