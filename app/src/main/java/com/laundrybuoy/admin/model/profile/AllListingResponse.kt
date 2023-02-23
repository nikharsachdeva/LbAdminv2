package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class AllListingResponse(
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
            @SerializedName("address")
            val address: Address?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("ordNum")
            val ordNum: String?,
            @SerializedName("role")
            val role: String?
        ) {
            data class Address(
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