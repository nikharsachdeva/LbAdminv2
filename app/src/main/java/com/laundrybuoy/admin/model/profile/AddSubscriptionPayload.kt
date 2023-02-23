package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class AddSubscriptionPayload(
    @SerializedName("actualPrice")
    val actualPrice: Double?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("discountedPrice")
    val discountedPrice: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("quantity")
    val quantity: Double?,
    @SerializedName("quantityType")
    val quantityType: String?,
    @SerializedName("validity")
    val validity: Int?,
    @SerializedName("details")
    val details: List<String?>?,
)