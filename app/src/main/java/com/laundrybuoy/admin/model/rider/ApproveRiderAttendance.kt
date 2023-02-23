package com.laundrybuoy.admin.model.rider


import com.google.gson.annotations.SerializedName

data class ApproveRiderAttendance(
    @SerializedName("approvalStatus")
    val approvalStatus: String?,
    @SerializedName("dates")
    val dates: List<String?>?,
    @SerializedName("riderId")
    val riderId: String?
)