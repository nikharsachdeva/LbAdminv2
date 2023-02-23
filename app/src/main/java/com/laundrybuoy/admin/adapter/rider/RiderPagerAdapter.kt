package com.laundrybuoy.admin.adapter.rider

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.laundrybuoy.admin.ui.rider.*
import com.laundrybuoy.admin.ui.vendor.*

class RiderPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int,
    var riderIdReceived: String?
):
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                RiderProfileFragment.newInstance(riderIdReceived?:"")
            }
            1 -> {
                RiderChartFragment.newInstance(riderIdReceived?:"")
            }
            2 ->{
                RiderSettlementFragment.newInstance(riderIdReceived?:"")
            }
            3 ->{
                RiderReviewFragment.newInstance(riderIdReceived?:"")
            }
            4 ->{
                RiderEditProfileFragment.newInstance(riderIdReceived?:"")
            }


            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}