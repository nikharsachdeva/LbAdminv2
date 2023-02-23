package com.laundrybuoy.admin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.notification.NotificationPagerAdapter
import com.laundrybuoy.admin.adapter.order.OrderPagerAdapter
import com.laundrybuoy.admin.databinding.FragmentNotificationBinding
import com.laundrybuoy.admin.databinding.FragmentProfileBinding
import com.laundrybuoy.admin.viewmodel.HomePageViewModel
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabLayout()

    }

    private fun initTabLayout() {
        val orderTabLayout = binding.notificationRootTabLayout
        val orderViewPager = binding.notificationRootViewPager

        orderTabLayout.addTab(orderTabLayout.newTab().setText("All"))
        orderTabLayout.addTab(orderTabLayout.newTab().setText("Order"))
        orderTabLayout.addTab(orderTabLayout.newTab().setText("Partner"))
        orderTabLayout.addTab(orderTabLayout.newTab().setText("Rider"))
        orderTabLayout.addTab(orderTabLayout.newTab().setText("User"))

        orderTabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = NotificationPagerAdapter(
            requireContext(), childFragmentManager,
            orderTabLayout.tabCount
        )
        orderViewPager.adapter = adapter
        orderViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                orderTabLayout
            )
        )
        orderViewPager.offscreenPageLimit = 5
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
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(true)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setBottomNav()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}