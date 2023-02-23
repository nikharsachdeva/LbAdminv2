package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

class VendorServices : ArrayList<VendorServices.VendorServicesItem>(){
    data class VendorServicesItem(
        @SerializedName("id")
        val id: String?,
        @SerializedName("serviceName")
        val serviceName: String?,
        var isSelected: Boolean = false

    )
}