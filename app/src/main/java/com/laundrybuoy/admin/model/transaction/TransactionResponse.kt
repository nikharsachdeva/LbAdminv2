package com.laundrybuoy.admin.model.transaction


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?,
) : Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("metaData")
        val metaData: MetaData?,
        @SerializedName("result")
        val result: Result?,
    ) : Parcelable {
        @Parcelize
        data class MetaData(
            @SerializedName("settledAmount")
            val settledAmount: Double?,
            @SerializedName("settledOrders")
            val settledOrders: Int?,
            @SerializedName("totalAmount")
            val totalAmount: Double?,
            @SerializedName("totalOrders")
            val totalOrders: Int?,
            @SerializedName("unSettledAmount")
            val unSettledAmount: Double?,
            @SerializedName("unSettledOrders")
            val unSettledOrders: Int?,
        ) : Parcelable

        @Parcelize
        data class Result(
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
            val totalPage: Int?,
        ) : Parcelable {
            @Parcelize
            data class Partner(
                @SerializedName("amount")
                val amount: Double?,
                @SerializedName("ordNum")
                val ordNum: String? = null,
                @SerializedName("createdAt")
                val createdAt: String?,
                @SerializedName("cur_bal")
                val curBal: Int?,
                @SerializedName("date")
                val date: String?,
                @SerializedName("_id")
                val id: String?,
                @SerializedName("isApproved")
                val isApproved: Boolean?,
                @SerializedName("isSetteled")
                val isSetteled: Boolean?,
                @SerializedName("isSettled")
                val isSettled: Boolean?,
                @SerializedName("itemId")
                val itemId: String?,
                @SerializedName("items")
                val items: List<Item?>?,
                @SerializedName("partnerId")
                val partnerId: String?,
                @SerializedName("profile")
                val profile: String?,
                @SerializedName("serviceCost")
                val serviceCost: Int?,
                @SerializedName("settlementDate")
                val settlementDate: String?,
                @SerializedName("totalCloths")
                val totalCloths: Double?,
                @SerializedName("totalWeight")
                val totalWeight: Double?,
                @SerializedName("txn_for")
                val txnFor: String?,
                @SerializedName("txn_type")
                val txnType: String?,
                @SerializedName("updatedAt")
                val updatedAt: String?,
                @SerializedName("__v")
                val v: Int?,
                @SerializedName("isSelected")
                val isSelected: Boolean?=false,
            ) : Parcelable {
                @Parcelize
                data class Item(
                    @SerializedName("eqCloths")
                    val eqCloths: Double?,
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("itemId")
                    val itemId: String?,
                    @SerializedName("itemName")
                    val itemName: String?,
                    @SerializedName("quantity")
                    val quantity: Int?,
                ) : Parcelable
            }
        }
    }
}