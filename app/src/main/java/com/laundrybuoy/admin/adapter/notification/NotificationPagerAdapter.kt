package com.laundrybuoy.admin.adapter.notification

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.laundrybuoy.admin.ui.notification.*
import com.laundrybuoy.admin.ui.order.OrderAnalysisFragment
import com.laundrybuoy.admin.ui.order.OrdersFragment

class NotificationPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
):
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AllNotificationFragment()
            }

            1 -> {
                OrderNotificationFragment()
            }

            2 -> {
                PartnerNotificationFragment()
            }

            3 -> {
                RiderNotificationFragment()
            }

            4 -> {
                UserNotificationFragment()
            }

            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}