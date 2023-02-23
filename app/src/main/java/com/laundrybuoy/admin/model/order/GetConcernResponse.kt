package com.laundrybuoy.admin.model.order


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetConcernResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
):Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("concernDescription")
        val concernDescription: String?,
        @SerializedName("concernType")
        val concernType: String?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("date")
        val date: String?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("orderId")
        val orderId: String?,
        @SerializedName("profile")
        val profile: MutableList<String>,
        @SerializedName("riderId")
        val riderId: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?
    ):Parcelable
}