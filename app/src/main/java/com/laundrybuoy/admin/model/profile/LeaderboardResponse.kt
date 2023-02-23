package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class LeaderboardResponse(
    @SerializedName("data")
    val `data`: MutableList<Data>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("name")
        val name: String?,
        @SerializedName("partnerId")
        val partnerId: String?,
        @SerializedName("points")
        val points: Int?,
        @SerializedName("profile")
        val profile: String?,
        @SerializedName("riderId")
        val riderId: String?
    )
}