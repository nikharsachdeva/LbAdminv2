package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class WorkingPincodePayload(
    @SerializedName("workingPincode")
    val servicesOffered: List<String?>?
)