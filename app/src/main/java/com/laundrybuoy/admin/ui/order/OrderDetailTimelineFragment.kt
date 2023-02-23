package com.laundrybuoy.admin.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.order.OrderHistoryAdapter
import com.laundrybuoy.admin.databinding.FragmentOrderDetailTimelineBinding
import com.laundrybuoy.admin.model.order.OrderDetailResponse
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.OrdersViewModel


class OrderDetailTimelineFragment : BaseFragment() {

    private var _binding: FragmentOrderDetailTimelineBinding? = null
    private val binding get() = _binding!!
    lateinit var ordersViewModel: OrdersViewModel
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initRv()
        initObservers()
    }

    private fun initObservers() {
        ordersViewModel.orderDetailLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data?.data != null) {
                        setHistoryUI(it.data?.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })
    }

    private fun setHistoryUI(data: OrderDetailResponse.Data) {
        if(!data.orderHistory.isNullOrEmpty()){
            binding.historyUnAvailableLl.makeViewGone()
            binding.orderHistoryRv.makeViewVisible()
            orderHistoryAdapter.submitList(data.orderHistory)
        }else{
            binding.historyUnAvailableLl.makeViewVisible()
            binding.orderHistoryRv.makeViewGone()
        }
    }


    private fun initRv() {
        orderHistoryAdapter = OrderHistoryAdapter()
        binding.orderHistoryRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.orderHistoryRv.adapter = orderHistoryAdapter
    }

    private fun init() {
        ordersViewModel = ViewModelProvider(requireActivity()).get(OrdersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOrderDetailTimelineBinding.inflate(inflater, container, false)
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