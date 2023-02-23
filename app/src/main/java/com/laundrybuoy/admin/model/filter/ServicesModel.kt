package com.laundrybuoy.admin.model.filter

import com.google.gson.annotations.SerializedName

data class ServicesModel(
    @SerializedName("data")
    val `data`: ArrayList<Data>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("_id")
        val id: String?,
        @SerializedName("serviceName")
        val serviceName: String?,
        @SerializedName("__v")
        val v: Int?,
        var isSelected: Boolean = false
    )
}