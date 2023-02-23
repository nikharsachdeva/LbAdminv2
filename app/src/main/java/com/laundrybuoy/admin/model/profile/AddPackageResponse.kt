package com.laundrybuoy.admin.model.profile


import com.google.gson.annotations.SerializedName

data class AddPackageResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("details")
        val details: List<String?>?,
        @SerializedName("grossPrice")
        val grossPrice: Double?,
        @SerializedName("hexCode")
        val hexCode: String?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("price")
        val price: Double?,
        @SerializedName("role")
        val role: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?
    )
}