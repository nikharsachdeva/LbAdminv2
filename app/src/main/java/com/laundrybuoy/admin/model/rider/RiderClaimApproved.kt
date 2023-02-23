package com.laundrybuoy.admin.model.rider


import com.google.gson.annotations.SerializedName

data class RiderClaimApproved(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("amount")
        val amount: Double?,
        @SerializedName("category")
        val category: String?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("cur_bal")
        val curBal: Double?,
        @SerializedName("date")
        val date: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("isApproved")
        val isApproved: Boolean?,
        @SerializedName("itemId")
        val itemId: String?,
        @SerializedName("riderId")
        val riderId: String?,
        @SerializedName("txn_for")
        val txnFor: String?,
        @SerializedName("txn_type")
        val txnType: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?
    )
}