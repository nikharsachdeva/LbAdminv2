package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class DeleteCouponResponse(
    @SerializedName("data")
    val `data`: Any?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
)