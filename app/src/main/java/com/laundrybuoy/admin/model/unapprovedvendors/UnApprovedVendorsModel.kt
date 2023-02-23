package com.laundrybuoy.admin.model.unapprovedvendors


import com.google.gson.annotations.SerializedName

data class UnApprovedVendorsModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("endIndex")
        val endIndex: Int?,
        @SerializedName("len")
        val len: Int?,
        @SerializedName("nextPage")
        val nextPage: Any?,
        @SerializedName("partners")
        val partners: List<Partner?>?,
        @SerializedName("prevPage")
        val prevPage: Any?,
        @SerializedName("startIndex")
        val startIndex: Int?,
        @SerializedName("totalPage")
        val totalPage: Int?
    ) {
        data class Partner(
            @SerializedName("address")
            val address: Address?,
            @SerializedName("altMobile")
            val altMobile: Long?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("isActive")
            val isActive: Boolean?,
            @SerializedName("isBlocked")
            val isBlocked: Boolean?,
            @SerializedName("isVerified")
            val isVerified: Boolean?,
            @SerializedName("mobile")
            val mobile: Long?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("photoId")
            val photoId: List<String?>?,
            @SerializedName("profile")
            val profile: String?,
            @SerializedName("role")
            val role: String?,
            @SerializedName("servicesOffered")
            val servicesOffered: List<String?>?,
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
}