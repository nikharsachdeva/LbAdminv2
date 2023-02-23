package com.laundrybuoy.admin.model.splash


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class SplashDataModel(
    @SerializedName("data")
    val `data`: SplashData?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    @Entity
    data class SplashData(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        @SerializedName("order")
        val order: List<FilterObj>,
        @SerializedName("partner")
        val partner: List<FilterObj>,
        @SerializedName("rider")
        val rider: List<FilterObj>,
        @SerializedName("user")
        val user: List<FilterObj>
    ) {
        data class FilterObj(
            @SerializedName("filterName")
            val filterName: String?,
            @SerializedName("filterQuery")
            val filterQuery: String?
        )

    }


}