package com.laundrybuoy.admin.model.rider


import com.google.gson.annotations.SerializedName

data class RiderAttendanceModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?,
) {
    data class Data(
        @SerializedName("date")
        val date: Int?,
        @SerializedName("status")
        val status: String?,
        @SerializedName("value")
        val value: String?,
        var isSelected: Boolean = false,
    )
}