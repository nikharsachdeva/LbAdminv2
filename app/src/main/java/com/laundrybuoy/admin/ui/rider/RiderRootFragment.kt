package com.laundrybuoy.admin.ui.rider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.rider.RiderPagerAdapter
import com.laundrybuoy.admin.adapter.vendor.VendorPagerAdapter
import com.laundrybuoy.admin.databinding.FragmentRiderRootBinding
import com.laundrybuoy.admin.databinding.FragmentVendorRootBinding
import com.laundrybuoy.admin.ui.vendor.VendorRootFragment

class RiderRootFragment : BaseFragment() {

    private var _binding: FragmentRiderRootBinding? = null
    private val binding get() = _binding!!
    var riderIdReceived: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        private const val RIDER_ID = "RIDER_ID"

        fun newInstance(
            riderId: String,
        ): RiderRootFragment {
            val viewFragment = RiderRootFragment()
            val args = Bundle()
            args.putString(RIDER_ID, riderId)
            viewFragment.arguments = args
            return viewFragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        riderIdReceived = arguments?.getString(RIDER_ID)
        initTabLayout()
    }

    private fun initTabLayout() {
        val vendorTabLayout = binding.riderRootTabLayout
        val vendorViewPager = binding.riderRootViewPager

        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Basic"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Orders"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Settlement"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Reviews"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Edit Profile"))
        vendorTabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = RiderPagerAdapter(
            requireContext(), childFragmentManager,
            vendorTabLayout.tabCount,
            riderIdReceived
        )
        vendorViewPager.adapter = adapter
        vendorViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                vendorTabLayout
            )
        )
        vendorViewPager.offscreenPageLimit = 5
        vendorTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                vendorViewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRiderRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(false)
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