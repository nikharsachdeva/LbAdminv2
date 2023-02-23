package com.laundrybuoy.admin.adapter.rider

import com.laundrybuoy.admin.model.rider.RiderAttendanceModel


interface SelectionInterfaceAttendance {
    fun latestItemIsHere(attendance: RiderAttendanceModel.Data)
}