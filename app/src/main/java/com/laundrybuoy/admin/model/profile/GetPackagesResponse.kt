package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class GetPackagesResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("grossPrice")
        val grossPrice: Double?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("price")
        val price: Double?,
        @SerializedName("role")
        val role: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?,
        @SerializedName("validity")
        val validity: Int?,
        @SerializedName("hexCode")
        val hexCode: String?=null,
        @SerializedName("details")
        val details: MutableList<String>?=null
    )
}