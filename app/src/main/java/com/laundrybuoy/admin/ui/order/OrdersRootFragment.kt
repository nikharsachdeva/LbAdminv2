package com.laundrybuoy.admin.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.order.OrderPagerAdapter
import com.laundrybuoy.admin.adapter.vendor.VendorPagerAdapter
import com.laundrybuoy.admin.databinding.FragmentOrdersRootBinding
import com.laundrybuoy.admin.databinding.FragmentSplashBinding


class OrdersRootFragment : BaseFragment() {

    private var _binding: FragmentOrdersRootBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabLayout()

    }

    private fun initTabLayout() {
        val orderTabLayout = binding.orderRootTabLayout
        val orderViewPager = binding.orderRootViewPager

        orderTabLayout.addTab(orderTabLayout.newTab().setText("Orders"))
        orderTabLayout.addTab(orderTabLayout.newTab().setText("Analysis"))

        orderTabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = OrderPagerAdapter(
            requireContext(), childFragmentManager,
            orderTabLayout.tabCount
        )
        orderViewPager.adapter = adapter
        orderViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                orderTabLayout
            )
        )
        orderViewPager.offscreenPageLimit = 2
        orderTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                orderViewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setBottomNav()
        }
    }

    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(true)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}