package com.laundrybuoy.admin.adapter.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.laundrybuoy.admin.ui.home.ChartOrderRevenueFragment
import com.laundrybuoy.admin.ui.home.ChartPackageRevenueFragment
import com.laundrybuoy.admin.ui.home.ChartSubscriptionRevenueFragment
import com.laundrybuoy.admin.ui.home.ChartTotalOrderFragment
import com.laundrybuoy.admin.ui.rider.*
import com.laundrybuoy.admin.ui.vendor.*

class AdminChartPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int):
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ChartTotalOrderFragment()
            }
            1 -> {
                ChartOrderRevenueFragment()
            }
            2 ->{
                ChartPackageRevenueFragment()
            }
            3 ->{
                ChartSubscriptionRevenueFragment()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}