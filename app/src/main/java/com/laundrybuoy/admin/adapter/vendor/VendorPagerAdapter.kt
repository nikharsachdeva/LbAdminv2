package com.laundrybuoy.admin.adapter.vendor

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.laundrybuoy.admin.ui.order.WasteFragment
import com.laundrybuoy.admin.ui.vendor.*

class VendorPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int,
    var vendorIdReceived: String?,
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> {
                VendorProfileFragment.newInstance(vendorIdReceived ?: "")
            }
            1 -> {
                VendorChartFragment.newInstance(vendorIdReceived ?: "")
            }
            2 -> {
                VendorSettlementFragment.newInstance(vendorIdReceived ?: "")
            }
            3 -> {
                VendorRevenueFragment.newInstance(vendorIdReceived ?: "")
            }
            4 -> {
                VendorReviewFragment.newInstance(vendorIdReceived ?: "")
            }
            5 -> {
                VendorEditProfileFragment.newInstance(vendorIdReceived ?: "")
            }

            else -> WasteFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}