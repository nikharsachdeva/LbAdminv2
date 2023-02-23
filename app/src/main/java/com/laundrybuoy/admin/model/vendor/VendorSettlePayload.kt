package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class VendorSettlePayload(
    @SerializedName("orderId")
    val orderId: List<String?>?
)