package com.laundrybuoy.admin.model.home


import com.google.gson.annotations.SerializedName

data class AdminGraphResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("month")
        var month: String?,
        @SerializedName("OrderRevenue")
        var orderRevenue: Double?,
        @SerializedName("ordersDelivered")
        var ordersDelivered: Int?,
        @SerializedName("ordersRecieved")
        var ordersReceived: Int?,
        @SerializedName("packageRevenue")
        var packageRevenue: Double?,
        @SerializedName("subscriptionRevenue")
        var subscriptionRevenue: Double?,
        @SerializedName("totalOrders")
        var totalOrders: Int?
    )
}