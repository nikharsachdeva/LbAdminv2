package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class VendorAttendanceModelNew(
    @SerializedName("data")
    val `data`: List<Data?>?
) {
    data class Data(
        @SerializedName("date")
        val date: Int?,
        @SerializedName("value")
        val value: String?
    )
}