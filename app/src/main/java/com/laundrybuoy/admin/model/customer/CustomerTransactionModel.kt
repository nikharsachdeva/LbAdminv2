package com.laundrybuoy.admin.model.customer


import com.google.gson.annotations.SerializedName

data class CustomerTransactionModel(
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
            @SerializedName("balanceBefore")
            val balanceBefore: Double?,
            @SerializedName("balanceCurrent")
            val balanceCurrent: Double?,
            @SerializedName("balanceUsed")
            val balanceUsed: Double?,
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("orderId")
            val orderId: OrderId?,
            @SerializedName("packageId")
            val packageId: PackageId?,
            @SerializedName("role")
            val role: String?,
            @SerializedName("subscriptionId")
            val subscriptionId: SubscriptionId?,
            @SerializedName("transactionDate")
            val transactionDate: String?,
            @SerializedName("transactionType")
            val transactionType: String?,
            @SerializedName("updatedAt")
            val updatedAt: String?,
            @SerializedName("userId")
            val userId: String?,
            @SerializedName("__v")
            val v: Int?
        ) {
            data class OrderId(
                @SerializedName("_id")
                val id: String?,
                @SerializedName("ordNum")
                val ordNum: String?
            )

            data class PackageId(
                @SerializedName("createdAt")
                val createdAt: String?,
                @SerializedName("description")
                val description: String?,
                @SerializedName("endDate")
                val endDate: String?,
                @SerializedName("_id")
                val id: String?,
                @SerializedName("isActive")
                val isActive: Boolean?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("packageId")
                val packageId: String?,
                @SerializedName("price")
                val price: Double?,
                @SerializedName("requestedOn")
                val requestedOn: String?,
                @SerializedName("riderId")
                val riderId: String?,
                @SerializedName("role")
                val role: String?,
                @SerializedName("startDate")
                val startDate: String?,
                @SerializedName("updatedAt")
                val updatedAt: String?,
                @SerializedName("userId")
                val userId: String?,
                @SerializedName("__v")
                val v: Int?,
                @SerializedName("validity")
                val validity: Int?
            )

            data class SubscriptionId(
                @SerializedName("actualPrice")
                val actualPrice: Double?,
                @SerializedName("createdAt")
                val createdAt: String?,
                @SerializedName("description")
                val description: String?,
                @SerializedName("details")
                val details: List<String?>?,
                @SerializedName("discountedPrice")
                val discountedPrice: Double?,
                @SerializedName("_id")
                val id: String?,
                @SerializedName("isActive")
                val isActive: Boolean?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("quantity")
                val quantity: Double?,
                @SerializedName("quantityType")
                val quantityType: String?,
                @SerializedName("role")
                val role: String?,
                @SerializedName("updatedAt")
                val updatedAt: String?,
                @SerializedName("__v")
                val v: Int?,
                @SerializedName("validity")
                val validity: Double?
            )
        }
    }
}