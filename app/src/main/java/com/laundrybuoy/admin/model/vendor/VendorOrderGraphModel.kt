package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class VendorOrderGraphModel(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("month")
        var month: String?,
        @SerializedName("orders")
        var orders: Double?,
        @SerializedName("revenue")
        var revenue: Double?
    )
}