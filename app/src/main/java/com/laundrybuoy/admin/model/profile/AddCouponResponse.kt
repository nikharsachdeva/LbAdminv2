package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class AddCouponResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("createdOn")
        val createdOn: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("discountPercentage")
        val discountPercentage: Double?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("maxDiscount")
        val maxDiscount: Double?,
        @SerializedName("minQuantity")
        val minQuantity: Double?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("quantityType")
        val quantityType: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("userId")
        val userId: List<String?>?,
        @SerializedName("__v")
        val v: Int?
    )
}