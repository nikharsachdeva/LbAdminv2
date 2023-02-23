package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class LeaderboardPayload(
    @SerializedName("month")
    val month: Int?,
    @SerializedName("reqType")
    val reqType: String?,
    @SerializedName("year")
    val year: Int?
)