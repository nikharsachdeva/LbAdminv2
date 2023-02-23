package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class VendorProfileModel(
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
        @SerializedName("mobile")
        val mobile: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("photoId")
        val photoId: List<String>,
        @SerializedName("profile")
        val profile: String?,
        @SerializedName("role")
        val role: String?,
        @SerializedName("servicesOffered")
        val servicesOffered: List<ServicesOffered?>?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?,
        @SerializedName("verificationLevel")
        val verificationLevel: Int?,
        @SerializedName("workAddress")
        val workAddress: WorkAddress?,
        @SerializedName("workingPincode")
        val workingPincode: List<String?>?
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

        data class ServicesOffered(
            @SerializedName("costPerCloth")
            val costPerCloth: Double?,
            @SerializedName("costPerKg")
            val costPerKg: Double?,
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("deliveryCloth")
            val deliveryCloth: Double?,
            @SerializedName("deliveryKg")
            val deliveryKg: Double?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("serviceImage")
            val serviceImage: String?,
            @SerializedName("serviceName")
            val serviceName: String?,
            @SerializedName("updatedAt")
            val updatedAt: String?,
            @SerializedName("__v")
            val v: Int?
        )

        data class WorkAddress(
            @SerializedName("city")
            val city: String?,
            @SerializedName("landmark")
            val landmark: String?,
            @SerializedName("lattitude")
            val lattitude: String?,
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