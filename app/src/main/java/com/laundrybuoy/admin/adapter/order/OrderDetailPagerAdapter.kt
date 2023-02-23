package com.laundrybuoy.admin.adapter.order

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.laundrybuoy.admin.ui.order.*
import com.laundrybuoy.admin.ui.order.OrderDetailRootFragment.Companion.newInstance

class OrderDetailPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int,
    var orderIdReceived: String?
):
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                OrderDetailBasicFragment.newInstance(orderIdReceived!!)
            }
            1 -> {
                OrderDetailBillingFragment()
            }
            2 -> {
                OrderDetailReviewFragment()
            }
            3 -> {
                OrderDetailActionFragment()
            }
            4 -> {
                OrderDetailTimelineFragment()
            }

            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}