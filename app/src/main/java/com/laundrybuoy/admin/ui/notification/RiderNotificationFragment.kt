package com.laundrybuoy.admin.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.notification.NotificationLoaderAdapter
import com.laundrybuoy.admin.adapter.notification.NotificationPagingAdapter
import com.laundrybuoy.admin.databinding.FragmentPartnerNotificationBinding
import com.laundrybuoy.admin.databinding.FragmentRiderNotificationBinding
import com.laundrybuoy.admin.model.NotificationResponse
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RiderNotificationFragment : BaseFragment() {

    private var _binding: FragmentRiderNotificationBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomePageViewModel>()
    lateinit var notificationListAdapter: NotificationPagingAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        initObserver()

    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.getNotificationListing("rider").observe(viewLifecycleOwner) {
                notificationListAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun initRv() {
        notificationListAdapter =
            NotificationPagingAdapter(object : NotificationPagingAdapter.OnClickInterface {
                override fun onSelected(order: NotificationResponse.Data.Partner, position: Int) {
                    getMainActivity()?.showSnackBar(order.notificationHeading)
                }

            })

        binding.orderNotificationRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.orderNotificationRv.setHasFixedSize(true)
        binding.orderNotificationRv.adapter = notificationListAdapter.withLoadStateHeaderAndFooter(
            header = NotificationLoaderAdapter(),
            footer = NotificationLoaderAdapter()
        )
        notificationListAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && notificationListAdapter.itemCount < 1) {
                binding.ordersUnAvailableLl.makeViewVisible()
                binding.orderNotificationRv.makeViewGone()
            } else {
                binding.ordersUnAvailableLl.makeViewGone()
                binding.orderNotificationRv.makeViewVisible()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRiderNotificationBinding.inflate(inflater, container, false)
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