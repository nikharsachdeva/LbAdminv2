package com.laundrybuoy.admin.model.rider


import com.google.gson.annotations.SerializedName

data class RiderProfileModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("address")
        val address: Address?,
        @SerializedName("altMobile")
        val altMobile: String?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("isBlocked")
        val isBlocked: Boolean?,
        @SerializedName("isVerified")
        val isVerified: Boolean?,
        @SerializedName("lastOrder")
        val lastOrder: String?,
        @SerializedName("mobile")
        val mobile: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("photoId")
        val photoId: List<String>,
        @SerializedName("profile")
        val profile: String?,
        @SerializedName("rcImage")
        val rcImage: List<String?>?,
        @SerializedName("role")
        val role: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?,
        @SerializedName("vehicleNumber")
        val vehicleNumber: String?,
        @SerializedName("verificationLevel")
        val verificationLevel: Int?
    ) {
        data class Address(
            @SerializedName("city")
            val city: String?,
            @SerializedName("landmark")
            val landmark: String?,
            @SerializedName("latitude")
            val latitude: String?,
            @SerializedName("line1")
            val line1: String?,
            @SerializedName("longitude")
            val longitude: String?,
            @SerializedName("pin")
            val pin: String?,
            @SerializedName("state")
            val state: String?
        )
    }
}