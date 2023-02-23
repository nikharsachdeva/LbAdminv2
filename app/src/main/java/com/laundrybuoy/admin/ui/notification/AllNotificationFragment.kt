package com.laundrybuoy.admin.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.notification.NotificationLoaderAdapter
import com.laundrybuoy.admin.adapter.notification.NotificationPagingAdapter
import com.laundrybuoy.admin.databinding.FragmentAllNotificationBinding
import com.laundrybuoy.admin.model.NotificationResponse
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllNotificationFragment : BaseFragment() {

    private var _binding: FragmentAllNotificationBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomePageViewModel>()
    lateinit var notificationListAdapter: NotificationPagingAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        initObserver()
        onClick()
    }

    private fun onClick() {
        binding.readAllNotificationIv.setOnClickListener {
            homeViewModel.readAllNotification("all")
        }

        binding.deleteAllNotificationIv.setOnClickListener {

        }
    }

    private fun initObserver() {
        getNotifications()

        homeViewModel.readAllNotificationLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Success -> {
                        if (it.data?.success == true && it.data?.message?.contains("notifications marked as read")==true) {
                            getNotifications()
                        }
                    }
                    is NetworkResult.Error -> {
                        getMainActivity()?.showSnackBar(it.message)
                    }
                }
            })
    }

    private fun getNotifications() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.getNotificationListing("all").observe(viewLifecycleOwner) {
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

        binding.allNotificationRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.allNotificationRv.setHasFixedSize(true)
        binding.allNotificationRv.adapter = notificationListAdapter.withLoadStateHeaderAndFooter(
            header = NotificationLoaderAdapter(),
            footer = NotificationLoaderAdapter()
        )
        notificationListAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && notificationListAdapter.itemCount < 1) {
                binding.ordersUnAvailableLl.makeViewVisible()
                binding.allNotificationRv.makeViewGone()
            } else {
                binding.ordersUnAvailableLl.makeViewGone()
                binding.allNotificationRv.makeViewVisible()
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
        _binding = FragmentAllNotificationBinding.inflate(inflater, container, false)
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