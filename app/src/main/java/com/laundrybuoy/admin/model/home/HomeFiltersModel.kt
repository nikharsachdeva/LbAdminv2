package com.laundrybuoy.admin.model.home


import com.google.gson.annotations.SerializedName

data class HomeFiltersModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
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