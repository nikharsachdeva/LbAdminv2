package com.laundrybuoy.admin.model.auth

import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("isBlocked")
        val isBlocked: Boolean?,
        @SerializedName("isRegistered")
        val isRegistered: Boolean?,
        @SerializedName("token")
        val token: String?,
        @SerializedName("verificationLevel")
        val verificationLevel: Int?
    )
}