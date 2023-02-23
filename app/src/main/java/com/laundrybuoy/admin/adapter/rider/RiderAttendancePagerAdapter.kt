package com.laundrybuoy.admin.adapter.rider

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.laundrybuoy.admin.ui.rider.*
import com.laundrybuoy.admin.ui.rider.attendance.AttendanceApprovedFragment
import com.laundrybuoy.admin.ui.rider.attendance.AttendanceMissedFragment
import com.laundrybuoy.admin.ui.rider.attendance.AttendanceSubmittedFragment
import com.laundrybuoy.admin.ui.vendor.*

class RiderAttendancePagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int,
    var riderIdReceived: String?
):
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AttendanceMissedFragment.newInstance(riderIdReceived?:"")
            }
            1 -> {
                AttendanceSubmittedFragment.newInstance(riderIdReceived?:"")
            }
            2 ->{
                AttendanceApprovedFragment.newInstance(riderIdReceived?:"")
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}