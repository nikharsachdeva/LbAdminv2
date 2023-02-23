package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

class VendorChartModel : ArrayList<VendorChartModel.VendorChartModelItem>(){
    data class VendorChartModelItem(
        @SerializedName("xAxis")
        val xAxis: String?,
        @SerializedName("yAxis")
        val yAxis: String?
    )
}