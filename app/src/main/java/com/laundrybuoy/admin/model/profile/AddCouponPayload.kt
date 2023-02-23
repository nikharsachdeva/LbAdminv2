package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class AddCouponPayload(
    @SerializedName("description")
    val description: String?,
    @SerializedName("discountPercentage")
    val discountPercentage: Double?,
    @SerializedName("maxDiscount")
    val maxDiscount: Double?,
    @SerializedName("minQuantity")
    val minQuantity: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("quantityType")
    val quantityType: String?
)