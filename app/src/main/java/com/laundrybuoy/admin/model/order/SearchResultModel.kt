package com.laundrybuoy.admin.model.order


import com.google.gson.annotations.SerializedName

data class SearchResultModel(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("len")
    val len: Int?,
    @SerializedName("message")
    val message: String?
) {
    data class Data(
        @SerializedName("_id")
        val id: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("role")
        val role: String?
    )
}