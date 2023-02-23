package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class GetSubscriptionResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("actualPrice")
        val actualPrice: Double?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("details")
        val details: List<String?>?,
        @SerializedName("discountedPrice")
        val discountedPrice: Double?,
        @SerializedName("hexCode")
        val hexCode: String?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("quantity")
        val quantity: Double?,
        @SerializedName("quantityType")
        val quantityType: String?,
        @SerializedName("role")
        val role: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?,
        @SerializedName("validity")
        val validity: Int?
    )
}