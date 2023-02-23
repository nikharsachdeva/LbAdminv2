package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class VendorFiguresModel(
    @SerializedName("data")
    val `data`: List<VendorFiltersModelItem>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class VendorFiltersModelItem(
        @SerializedName("baseUrl")
        val baseUrl: String?,
        @SerializedName("filterFigure")
        val filterFigure: Int?,
        @SerializedName("filterIcon")
        val filterIcon: String?,
        @SerializedName("filterName")
        val filterName: String?,
        @SerializedName("hexCode")
        val hexCode: String?,
        @SerializedName("id")
        val id: Int?
    )
}