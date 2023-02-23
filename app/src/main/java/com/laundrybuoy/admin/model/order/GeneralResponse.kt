package com.laundrybuoy.admin.model.order


import com.google.gson.annotations.SerializedName

data class GeneralResponse(
    @SerializedName("data")
    val `data`: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
)