package com.laundrybuoy.admin.model

import com.laundrybuoy.admin.model.SearchModel.SearchModelItem

import com.google.gson.annotations.SerializedName

class SearchModel : ArrayList<SearchModelItem>(){
    data class SearchModelItem(
        @SerializedName("id")
        val id: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("type")
        val type: String?
    )
}