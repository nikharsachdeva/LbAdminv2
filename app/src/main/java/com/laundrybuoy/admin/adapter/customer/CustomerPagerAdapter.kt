package com.laundrybuoy.admin.adapter.customer

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.laundrybuoy.admin.ui.customer.*
import com.laundrybuoy.admin.ui.vendor.*

class CustomerPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int,
    var customerIdReceived: String?

):
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                CustomerProfileFragment.newInstance(customerIdReceived?:"")
            }
            1 -> {
                CustomerOrdersFragment.newInstance(customerIdReceived?:"")
            }
            2 -> {
                CustomerSubscriptionFragment.newInstance(customerIdReceived?:"")
            }
            3 -> {
                CustomerPackageFragment.newInstance(customerIdReceived?:"")
            }
            4 -> {
                CustomerCoinsFragment.newInstance(customerIdReceived?:"")
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}