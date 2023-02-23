package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class VendorRatingModel(
    @SerializedName("data")
    val `data`: List<VendorRatingModelItem?>?
) {
    data class VendorRatingModelItem(
        @SerializedName("count")
        val count: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("total")
        val total: String?
    )
}