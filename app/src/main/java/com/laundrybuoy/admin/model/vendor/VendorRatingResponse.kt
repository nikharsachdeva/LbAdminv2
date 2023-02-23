package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class VendorRatingResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("badges")
        val badges: List<Badge?>?,
        @SerializedName("ratings")
        val ratings: List<Rating?>?
    ) {
        data class Badge(
            @SerializedName("badge")
            val badge: Badge?,
            @SerializedName("count")
            val count: Int?
        ) {
            data class Badge(
                @SerializedName("badgeFor")
                val badgeFor: String?,
                @SerializedName("createdAt")
                val createdAt: String?,
                @SerializedName("_id")
                val id: String?,
                @SerializedName("image")
                val image: String?,
                @SerializedName("updatedAt")
                val updatedAt: String?,
                @SerializedName("__v")
                val v: Int?,
                @SerializedName("name")
                val name: String?,
            )
        }

        data class Rating(
            @SerializedName("figure")
            val figure: Double?,
            @SerializedName("name")
            val name: String?
        )
    }
}