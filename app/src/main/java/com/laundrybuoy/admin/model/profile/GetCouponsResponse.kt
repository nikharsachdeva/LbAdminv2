package com.laundrybuoy.admin.model.profile


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetCouponsResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
):Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("description")
        val description: String?,
        @SerializedName("discountPercentage")
        val discountPercentage: Double?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("maxDiscount")
        val maxDiscount: Double?,
        @SerializedName("minQuantity")
        val minQuantity: Double?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("quantityType")
        val quantityType: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("userId")
        val userId: List<String?>?,
        @SerializedName("__v")
        val v: Int?
    ):Parcelable
}