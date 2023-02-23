package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

class VendorOrderBreakupModel : ArrayList<VendorOrderBreakupModel.VendorOrderBreakupModelItem>(){
    data class VendorOrderBreakupModelItem(
        @SerializedName("hex")
        val hex: String?,
        @SerializedName("score")
        val score: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("total")
        val total: String?
    )
}