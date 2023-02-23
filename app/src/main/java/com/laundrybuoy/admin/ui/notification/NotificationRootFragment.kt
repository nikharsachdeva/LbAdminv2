package com.laundrybuoy.admin.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.notification.DynamicNotificationPagerAdapter
import com.laundrybuoy.admin.databinding.FragmentNotificationRootBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationRootFragment : BaseFragment() {

    private var _binding: FragmentNotificationRootBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfHeading : MutableList<String> = arrayListOf()
        listOfHeading.add("All")
        listOfHeading.add("Order")
        listOfHeading.add("Partner")
        listOfHeading.add("Rider")
        listOfHeading.add("User")
        setupTabLayoutUI(listOfHeading)

        binding.backFromNotificationIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }
    }

    internal companion object {
        private const val SCREEN_HEADING = "SCREEN_HEADING"

        @JvmStatic
        fun newInstance(
            screenHeading: String): NotificationRootFragment {
            val frg = NotificationRootFragment()
            val bundle = Bundle()
            bundle.putString(SCREEN_HEADING, screenHeading)
            frg.arguments = bundle
            return frg
        }
    }

    private fun setupTabLayoutUI(listOfHeading: MutableList<String>) {
        binding.notificationsDynamicViewPager.adapter =
            DynamicNotificationPagerAdapter(
                childFragmentManager,
                listOfHeading,
                DynamicNotificationPagerAdapter.TYPE_NOTIFICATION_FRAGMENT
            )

        binding.notificationsTabLayout.setupWithViewPager(binding.notificationsDynamicViewPager)
        binding.notificationsTabLayout.tabRippleColor = null
        binding.notificationsDynamicViewPager.offscreenPageLimit = listOfHeading.size

        binding.notificationsTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentNotificationRootBinding.inflate(inflater, container, false)
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