package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class VendorSettleResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("acknowledged")
        val acknowledged: Boolean?,
        @SerializedName("matchedCount")
        val matchedCount: Int?,
        @SerializedName("modifiedCount")
        val modifiedCount: Int?,
        @SerializedName("upsertedCount")
        val upsertedCount: Int?,
        @SerializedName("upsertedId")
        val upsertedId: Any?
    )
}