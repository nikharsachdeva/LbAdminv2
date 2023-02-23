package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

class VendorActivityModel : ArrayList<VendorActivityModel.VendorActivityModelItem>(){
    data class VendorActivityModelItem(
        @SerializedName("activityDate")
        val activityDate: String?,
        @SerializedName("activityDesc")
        val activityDesc: String?,
        @SerializedName("activityId")
        val activityId: String?,
        @SerializedName("activityOperation")
        val activityOperation: String?
    )
}