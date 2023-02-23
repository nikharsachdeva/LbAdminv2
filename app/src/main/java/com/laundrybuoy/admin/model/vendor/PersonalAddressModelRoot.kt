package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class PersonalAddressModelRoot(
    @SerializedName("address")
    val workAddress: VendorProfileModel.Data.Address?
)