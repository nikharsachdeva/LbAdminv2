package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class ServicesOfferedPayload(
    @SerializedName("servicesOffered")
    val servicesOffered: List<String?>?
)