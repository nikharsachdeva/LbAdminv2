package com.laundrybuoy.admin.ui.vendor


import com.google.gson.annotations.SerializedName

data class VendorAddressModel(
    @SerializedName("city")
    var city: String?=null,
    @SerializedName("completeAddress")
    var completeAddress: String?=null,
    @SerializedName("country")
    var country: String?=null,
    @SerializedName("fullAddress")
    var fullAddress: String?=null,
    @SerializedName("knownName")
    var knownName: String?=null,
    @SerializedName("postalCode")
    var postalCode: String?=null,
    @SerializedName("state")
    var state: String?=null
)