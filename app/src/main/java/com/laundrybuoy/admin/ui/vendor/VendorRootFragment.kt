package com.laundrybuoy.admin.ui.vendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.vendor.VendorPagerAdapter
import com.laundrybuoy.admin.databinding.FragmentVendorRootBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VendorRootFragment : BaseFragment() {

    private var _binding: FragmentVendorRootBinding? = null
    private val binding get() = _binding!!
    var vendorIdReceived: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        private const val VENDOR_ID = "VENDOR_ID"

        fun newInstance(
            vendorId: String,
        ): VendorRootFragment {
            val viewFragment = VendorRootFragment()
            val args = Bundle()
            args.putString(VENDOR_ID, vendorId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabLayout()


    }

    private fun initTabLayout() {
        val vendorTabLayout = binding.vendorRootTabLayout
        val vendorViewPager = binding.vendorRootViewPager
        vendorIdReceived = arguments?.getString(VENDOR_ID)

        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Basic"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Orders"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Settlement"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Revenue"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Reviews"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Edit Profile"))
        vendorTabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = VendorPagerAdapter(
            requireContext(), childFragmentManager,
            vendorTabLayout.tabCount,
            vendorIdReceived
        )
        vendorViewPager.adapter = adapter
        vendorViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                vendorTabLayout
            )
        )
        vendorViewPager.offscreenPageLimit = 6
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
        _binding = FragmentVendorRootBinding.inflate(inflater, container, false)
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