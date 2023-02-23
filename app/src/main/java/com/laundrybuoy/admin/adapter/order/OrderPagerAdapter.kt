package com.laundrybuoy.admin.adapter.order

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.laundrybuoy.admin.ui.order.OrderAnalysisFragment
import com.laundrybuoy.admin.ui.order.OrdersFragment

class OrderPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
):
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                OrdersFragment()
            }

            1 -> {
                OrderAnalysisFragment()
            }

            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}