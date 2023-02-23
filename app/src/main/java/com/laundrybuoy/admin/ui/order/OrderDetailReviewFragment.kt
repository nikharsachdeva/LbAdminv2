package com.laundrybuoy.admin.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.order.OrderRatingAdapter
import com.laundrybuoy.admin.databinding.FragmentOrderDetailReviewBinding
import com.laundrybuoy.admin.model.order.OrderDetailResponse
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.OrdersViewModel

class OrderDetailReviewFragment : BaseFragment() {


    private var _binding: FragmentOrderDetailReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderRatingAdapter: OrderRatingAdapter
    lateinit var ordersViewModel1: OrdersViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRatingRv()
        initObserver()
    }

    private fun initObserver() {
        ordersViewModel1.orderDetailLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.data != null) {
                        setUI(it.data?.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })
    }

    private fun setUI(data: OrderDetailResponse.Data) {
        if(!data.ratings.isNullOrEmpty()){
            orderRatingAdapter.submitList(data.ratings)
        }
    }

    private fun initRatingRv() {

        ordersViewModel1 = ViewModelProvider(requireActivity()).get(OrdersViewModel::class.java)

        orderRatingAdapter = OrderRatingAdapter()
        binding.orderReviewsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.orderReviewsRv.adapter = orderRatingAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOrderDetailReviewBinding.inflate(inflater, container, false)
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