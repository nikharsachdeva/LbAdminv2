package com.laundrybuoy.admin.model.rider


import com.google.gson.annotations.SerializedName

data class RiderGraphResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("delivered")
        var delivered: Int?,
        @SerializedName("month")
        var month: String?,
        @SerializedName("picked")
        var picked: Int?
    )
}