package com.laundrybuoy.admin.model.map


import com.google.gson.annotations.SerializedName

data class MapCoordinatesModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("address")
        val address: Address,
        @SerializedName("_id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("role")
        val role: String
    ) {
        data class Address(
            @SerializedName("latitude")
            val latitude: String,
            @SerializedName("longitude")
            val longitude: String
        )
    }
}