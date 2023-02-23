package com.laundrybuoy.admin.model.order


import com.google.gson.annotations.SerializedName

data class RatingModel(
    @SerializedName("partner")
    val partner: Partner?,
    @SerializedName("rider")
    val rider: Rider?,
) {
    data class Partner(
        @SerializedName("badges")
        val badges: List<Rider.Badge>,
        @SerializedName("stars")
        val stars: Int?,
    )

    data class Rider(
        @SerializedName("badges")
        val badges: List<Badge>,
        @SerializedName("stars")
        val stars: Int?,
    ) {
        data class Badge(
            @SerializedName("badgeIcon")
            val badgeIcon: String?,
            @SerializedName("badgeName")
            val badgeName: String?,
            @SerializedName("isSelected")
            val isSelected: Boolean?,
        )
    }
}