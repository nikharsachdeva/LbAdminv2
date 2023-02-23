package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class VendorRevenueModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: String?
) {
    data class Data(
        @SerializedName("profit")
        val incentive: List<Incentive?>?,
        @SerializedName("revenue")
        val revenue: List<Revenue?>?
    ) {
        data class Incentive(
            @SerializedName("xAxis")
            val xAxis: String?,
            @SerializedName("yAxis")
            val yAxis: String?
        )

        data class Revenue(
            @SerializedName("xAxis")
            val xAxis: String?,
            @SerializedName("yAxis")
            val yAxis: String?
        )
    }
}