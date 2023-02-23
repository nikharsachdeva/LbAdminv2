package com.laundrybuoy.admin.adapter.all

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.profile.AllListingResponse
import com.laundrybuoy.admin.retrofit.AdminAPI
import com.laundrybuoy.admin.utils.Constants.BASE_URL
import java.lang.Exception

class AllItemPagingSource(private val adminApi: AdminAPI, private val filter: String, private val search : String?=null, private val filterSelected : String?=null) : PagingSource<Int, AllListingResponse.Data.Partner>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllListingResponse.Data.Partner> {
        return try {
            val position = params.key ?: 1
            val response = adminApi.getAllItems(filter,position,search,filterSelected)
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

    override fun getRefreshKey(state: PagingState<Int, AllListingResponse.Data.Partner>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)

        }
    }

}