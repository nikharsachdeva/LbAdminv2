package com.laundrybuoy.admin.adapter.order.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.order.AllOrderListModel
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.retrofit.AdminAPI

class PartnerAllOrderListingPs(private val adminApi: AdminAPI, private val orderPayload: JsonObject) : PagingSource<Int, AllOrderListModel.Data.Partner>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllOrderListModel.Data.Partner> {
        return try {
            val position = params.key ?: 1
            val response = adminApi.getPartnerOrderListing(position,orderPayload)
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

    override fun getRefreshKey(state: PagingState<Int, AllOrderListModel.Data.Partner>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}