package com.laundrybuoy.admin.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.customer.CustomerCoinsPagingAdapter
import com.laundrybuoy.admin.adapter.order.paging.PartnerOrderLoaderAdapter
import com.laundrybuoy.admin.databinding.FragmentCustomerCoinsBinding
import com.laundrybuoy.admin.model.customer.CustomerTransactionModel
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomerCoinsFragment : BaseFragment() {

    private var _binding: FragmentCustomerCoinsBinding? = null
    private val binding get() = _binding!!
    var customerIdReceived: String? = null
    private val customerViewModel by viewModels<CustomerViewModel>()
    lateinit var customerCoinsListAdapter: CustomerCoinsPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerCoinsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val CUSTOMER_ID = "CUSTOMER_ID"

        fun newInstance(
            customerId: String,
        ): CustomerCoinsFragment {
            val viewFragment = CustomerCoinsFragment()
            val args = Bundle()
            args.putString(CUSTOMER_ID, customerId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerIdReceived = arguments?.getString(CUSTOMER_ID)
        initCoinsRv()
        initObserver()

    }


    private fun initCoinsRv() {
        customerCoinsListAdapter =
            CustomerCoinsPagingAdapter(object : CustomerCoinsPagingAdapter.OnClickInterface {
                override fun onSelected(
                    order: CustomerTransactionModel.Data.Partner,
                    position: Int,
                ) {

                }

            })

        binding.customerCoinsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.customerCoinsRv.setHasFixedSize(true)
        binding.customerCoinsRv.adapter = customerCoinsListAdapter.withLoadStateHeaderAndFooter(
            header = PartnerOrderLoaderAdapter(),
            footer = PartnerOrderLoaderAdapter()
        )
        customerCoinsListAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && customerCoinsListAdapter.itemCount < 1) {
                binding.coinsUnAvailableLl.makeViewVisible()
                binding.coinsAvailableRl.makeViewGone()
            } else {
                binding.coinsUnAvailableLl.makeViewGone()
                binding.coinsAvailableRl.makeViewVisible()
                setUI()
            }
        }

    }


    private fun initObserver() {
        val customerIdPayload = JsonObject()
        customerIdPayload.addProperty("userId", customerIdReceived)

        viewLifecycleOwner.lifecycleScope.launch {
            customerViewModel.getCustomerCoins(customerIdPayload).observe(viewLifecycleOwner) {
                customerCoinsListAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun setUI() {
        val transactions = customerCoinsListAdapter.snapshot().items
        if(!transactions.isNullOrEmpty()){
            binding.currentBalTv.text = "${transactions[0].balanceCurrent}"
        }else{
            binding.currentBalTv.text = "Null"

        }
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