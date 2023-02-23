package com.laundrybuoy.admin.ui.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.customer.CustomerPagerAdapter
import com.laundrybuoy.admin.adapter.rider.RiderPagerAdapter
import com.laundrybuoy.admin.databinding.FragmentCustomerRootBinding
import com.laundrybuoy.admin.databinding.FragmentOrdersBinding
import com.laundrybuoy.admin.databinding.FragmentOrdersRootBinding
import com.laundrybuoy.admin.ui.rider.RiderRootFragment

class CustomerRootFragment : BaseFragment() {

    private var _binding: FragmentCustomerRootBinding? = null
    private val binding get() = _binding!!
    var customerIdReceived: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        private const val CUSTOMER_ID = "CUSTOMER_ID"

        fun newInstance(
            customerId: String,
        ): CustomerRootFragment {
            val viewFragment = CustomerRootFragment()
            val args = Bundle()
            args.putString(CUSTOMER_ID, customerId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerIdReceived = arguments?.getString(CUSTOMER_ID)
        initTabLayout()
    }


    private fun initTabLayout() {
        val customerTabLayout = binding.customerRootTabLayout
        val customerViewPager = binding.customerRootViewPager

        customerTabLayout.addTab(customerTabLayout.newTab().setText("Basic"))
        customerTabLayout.addTab(customerTabLayout.newTab().setText("Orders"))
        customerTabLayout.addTab(customerTabLayout.newTab().setText("Subscription"))
        customerTabLayout.addTab(customerTabLayout.newTab().setText("Packages"))
        customerTabLayout.addTab(customerTabLayout.newTab().setText("Coins"))
        customerTabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = CustomerPagerAdapter(
            requireContext(), childFragmentManager,
            customerTabLayout.tabCount,
            customerIdReceived
        )
        customerViewPager.adapter = adapter
        customerViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                customerTabLayout
            )
        )
        customerViewPager.offscreenPageLimit = 5
        customerTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                customerViewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCustomerRootBinding.inflate(inflater, container, false)
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