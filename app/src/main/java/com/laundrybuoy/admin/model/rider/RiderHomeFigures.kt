package com.laundrybuoy.admin.model.rider


import com.google.gson.annotations.SerializedName

data class RiderHomeFigures(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("response")
        val response: List<Response>
    ) {
        data class Response(
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
}