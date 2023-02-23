package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class AddSubscriptionResponse(
    @SerializedName("data")
    val `data`: Data?,
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
        @SerializedName("discountedPrice")
        val discountedPrice: Double?,
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
        val validity: Double?
    )
}