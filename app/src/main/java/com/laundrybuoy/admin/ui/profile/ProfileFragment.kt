package com.laundrybuoy.admin.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.UploadDocsAdapter
import com.laundrybuoy.admin.databinding.FragmentProfileBinding
import com.laundrybuoy.admin.model.UploadedDocsModel
import com.laundrybuoy.admin.ui.customer.CustomerRootFragment

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var docAdapter: UploadDocsAdapter
    var qrList: MutableList<UploadedDocsModel.UploadedDocsModelItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBottomNav()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        onClick()
    }

    private fun onClick() {

        binding.tempTv.setOnClickListener {
            val frag = CustomerRootFragment.newInstance("63e3effca8a7c4e2b5e3f849" ?: "")
            getMainActivity()?.addFragment(true,getMainActivity()?.getVisibleFrame()!!,frag)

        }

        binding.vendorLeaderboardLl.setOnClickListener {
            val frag = LeaderboardFragment.newInstance("PARTNER")
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                frag
            )
        }

        binding.riderLeaderboardLl.setOnClickListener {
            val frag = LeaderboardFragment.newInstance("RIDER")
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                frag
            )

        }

        binding.subscriptionLl.setOnClickListener {
            val frag = SubscriptionFragment()
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                frag
            )
        }

        binding.packageLl.setOnClickListener {
            val frag = PackageFragment()
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                frag
            )
        }


        binding.qrLl.setOnClickListener {
            val frag = QrFragment()
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                frag
            )
        }

        binding.couponLl.setOnClickListener {
            val frag = CouponFragment()
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                frag
            )
        }

        binding.vendorsLl.setOnClickListener {
            val frag = AllListFragment.newInstance("partner", "profile")
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                frag
            )
        }

        binding.ridersLl.setOnClickListener {
            val frag = AllListFragment.newInstance("rider", "profile")
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                frag
            )
        }

        binding.ordersLl.setOnClickListener {
            val frag = AllListFragment.newInstance("order", "profile")
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                frag
            )
        }

        binding.usersLl.setOnClickListener {
            val frag = AllListFragment.newInstance("user", "profile")
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                frag
            )
        }
    }

    private fun init() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
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