package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

class VendorAttendanceModel : ArrayList<VendorAttendanceModel.VendorAttendanceModelItem>(){
    data class VendorAttendanceModelItem(
        @SerializedName("date")
        val date: String?,
        @SerializedName("present")
        val present: Boolean?
    )
}