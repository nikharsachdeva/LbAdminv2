package com.laundrybuoy.admin.model.order


import com.google.gson.annotations.SerializedName

data class OrderChartAnalysis(
    @SerializedName("data")
    val `data`: List<OrderChartAnalysisItem?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class OrderChartAnalysisItem(
        @SerializedName("dataSet")
        val dataSet: List<DataSet?>?,
        @SerializedName("datasetName")
        val datasetName: String?,
        @SerializedName("datasetHex")
        val datasetHex: String?
    ) {
        data class DataSet(
            @SerializedName("xAxis")
            val xAxis: String?,
            @SerializedName("yAxis")
            val yAxis: String?
        )
    }
}