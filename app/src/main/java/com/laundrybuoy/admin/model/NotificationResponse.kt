package com.laundrybuoy.admin.model


import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("endIndex")
        val endIndex: Int?,
        @SerializedName("len")
        val len: Int?,
        @SerializedName("nextPage")
        val nextPage: Int?,
        @SerializedName("partners")
        val partners: List<Partner>,
        @SerializedName("prevPage")
        val prevPage: Int?,
        @SerializedName("startIndex")
        val startIndex: Int?,
        @SerializedName("totalItems")
        val totalItems: Int?,
        @SerializedName("totalPage")
        val totalPage: Int?
    ) {
        data class Partner(
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("isOpened")
            val isOpened: Boolean?,
            @SerializedName("notificationContent")
            val notificationContent: String?,
            @SerializedName("notificationHeading")
            val notificationHeading: String?,
            @SerializedName("notificationIcon")
            val notificationIcon: String?,
            @SerializedName("orderId")
            val orderId: String?,
            @SerializedName("partnerId")
            val partnerId: String?,
            @SerializedName("riderId")
            val riderId: String?,
            @SerializedName("screenType")
            val screenType: String?,
            @SerializedName("screenTypeId")
            val screenTypeId: String?,
            @SerializedName("sender")
            val sender: String?,
            @SerializedName("userId")
            val userId: String?
        )
    }
}