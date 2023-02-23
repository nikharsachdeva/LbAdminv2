package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class WorkAddressModelRoot(
    @SerializedName("workAddress")
    val workAddress: VendorProfileModel.Data.WorkAddress?
)