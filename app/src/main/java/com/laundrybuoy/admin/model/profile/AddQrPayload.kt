package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class AddQrPayload(
    @SerializedName("description")
    val description: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("qrImage")
    val qrImage: String?
)