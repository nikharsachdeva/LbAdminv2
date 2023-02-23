package com.laundrybuoy.admin.adapter.transaction

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.transaction.TransactionResponse.Data.Result.Partner
import com.laundrybuoy.admin.retrofit.AdminAPI

class VendorTransactionPagingSource(
    private val adminApi: AdminAPI,
    private val pathVar: String,
    private val partnerPayload: JsonObject
) : PagingSource<Int, Partner>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,Partner> {
        return try {
            val position = params.key ?: 1
            val response = adminApi.getVendorTransaction(pathVar,position,partnerPayload)
            return LoadResult.Page(
                data = response.body()?.data?.result?.partners!!,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.body()?.data?.result?.partners!!.isEmpty()) null else position + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int,Partner>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}