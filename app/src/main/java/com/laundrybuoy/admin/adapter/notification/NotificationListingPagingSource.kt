package com.laundrybuoy.admin.adapter.notification

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.NotificationResponse
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.retrofit.AdminAPI

class NotificationListingPagingSource(private val adminApi: AdminAPI, private val filter: String) : PagingSource<Int, NotificationResponse.Data.Partner>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationResponse.Data.Partner> {
        return try {
            val position = params.key ?: 1
            val response = adminApi.getNotifications(filter,position)
            return LoadResult.Page(
                data = response.body()?.data?.partners!!,
                prevKey = if (position == 1) null else position - 1,
//                nextKey = if (position == response.body()?.data?.totalPage) null else position + 1
                nextKey = if (response.body()?.data?.partners!!.isEmpty()) null else position + 1
            )


        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NotificationResponse.Data.Partner>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}