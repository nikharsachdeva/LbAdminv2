package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class GetQrResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("qrImage")
        val qrImage: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?
    )
}