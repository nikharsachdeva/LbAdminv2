package com.laundrybuoy.admin.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.order.OrderDetailPagerAdapter
import com.laundrybuoy.admin.adapter.order.OrderPagerAdapter
import com.laundrybuoy.admin.databinding.FragmentOrderDetailRootBinding
import com.laundrybuoy.admin.databinding.FragmentOrdersBinding
import com.laundrybuoy.admin.viewmodel.OrdersViewModel

class OrderDetailRootFragment : BaseFragment() {

    private var _binding: FragmentOrderDetailRootBinding? = null
    private val binding get() = _binding!!
    private var orderIdReceived: String? = null
    lateinit var ordersViewModel: OrdersViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderIdReceived = arguments?.getString(ORDER_ID)
        ordersViewModel = ViewModelProvider(requireActivity()).get(OrdersViewModel::class.java)

        initTabLayout()
        if (!orderIdReceived.isNullOrEmpty()) {
            ordersViewModel.getDetailedOrder(orderIdReceived!!)
        }

    }

    private fun initTabLayout() {
        val vendorTabLayout = binding.orderRootTabLayout
        val vendorViewPager = binding.orderRootViewPager

        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Summary"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Billing"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Review"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Action"))
        vendorTabLayout.addTab(vendorTabLayout.newTab().setText("Timeline"))
        vendorTabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = OrderDetailPagerAdapter(
            requireContext(), childFragmentManager,
            vendorTabLayout.tabCount, orderIdReceived
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

    companion object {
        private const val ORDER_ID = "ORDER_ID"
        fun newInstance(
            orderId: String,
        ): OrderDetailRootFragment {
            val orderRootFragment = OrderDetailRootFragment()
            val args = Bundle()
            args.putString(ORDER_ID, orderId)
            orderRootFragment.arguments = args
            return orderRootFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOrderDetailRootBinding.inflate(inflater, container, false)
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