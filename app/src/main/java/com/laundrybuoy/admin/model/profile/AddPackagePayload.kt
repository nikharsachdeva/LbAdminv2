package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class AddPackagePayload(
    @SerializedName("description")
    val description: String?,
    @SerializedName("details")
    val details: List<String?>?,
    @SerializedName("grossPrice")
    val grossPrice: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("validity")
    val validity: Int?
)