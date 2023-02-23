package com.laundrybuoy.admin.ui.vendor

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
import com.laundrybuoy.admin.adapter.transaction.VendorTransactionLoaderAdapter
import com.laundrybuoy.admin.adapter.transaction.VendorTransactionPagingAdapter
import com.laundrybuoy.admin.databinding.FragmentVendorSettlementBinding
import com.laundrybuoy.admin.model.transaction.TransactionResponse
import com.laundrybuoy.admin.model.vendor.VendorSettlePayload
import com.laundrybuoy.admin.utils.*
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VendorSettlementFragment : BaseFragment() {

    private var _binding: FragmentVendorSettlementBinding? = null
    private val binding get() = _binding!!
    private val vendorViewModel by viewModels<VendorViewModel>()
    lateinit var vendorTransactionAdapter: VendorTransactionPagingAdapter
    private var partnerId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        private const val VENDOR_ID = "VENDOR_ID"

        fun newInstance(
            vendorId: String,
        ): VendorSettlementFragment {
            val viewFragment = VendorSettlementFragment()
            val args = Bundle()
            args.putString(VENDOR_ID, vendorId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        partnerId = arguments?.getString(VENDOR_ID)
        init()
        initObserver()
        fetchTransactions("all")
        onClick()
        vendorViewModel.setSelectedTransactionList(arrayListOf())
        initSelectorCb()


    }

    private fun initSelectorCb() {
        binding.selectorTv.text="Select all"

        binding.selectorCb.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                vendorTransactionAdapter.selectAll()
                binding.selectorTv.text="${vendorViewModel._selectedTransactionLiveData.value?.size} unsettled items selected"
            }else{
                vendorTransactionAdapter.clearSelected()
                binding.selectorTv.text="Select all"
            }
        }

    }

    private fun init() {
    }

    private fun onClick() {

        binding.settleAllBtn.setOnClickListener {

            val listOfOrderId: MutableList<String> = arrayListOf()
            listOfOrderId.clear()
            vendorViewModel._selectedTransactionLiveData.value?.forEach { item ->
                listOfOrderId.add(item.id ?: "")
            }
            val settlePayload = VendorSettlePayload(
                orderId = listOfOrderId
            )
            vendorViewModel.settleVendor(settlePayload)
        }

        binding.transactionFiguresRl.setOnClickListener {
            val figFrag = VendorTransactionFigFragment()
            figFrag.isCancelable = true
            figFrag.show(childFragmentManager, "filter_home")
        }

        binding.transactionFilteredRl.setOnClickListener {
            val filterFrag = VendorTransactionFilFragment()
            filterFrag.setCallback { selectedFilter ->
                fetchTransactions(selectedFilter)
            }
            filterFrag.isCancelable = true
            filterFrag.show(childFragmentManager, "filter_home")
        }
    }

    private fun initObserver() {

        vendorTransactionAdapter = VendorTransactionPagingAdapter(object :
            VendorTransactionPagingAdapter.OnClickInterface {
            override fun onSelected(selectedList: MutableList<TransactionResponse.Data.Result.Partner>) {
                vendorViewModel.setSelectedTransactionList(selectedList)
            }
        })

        binding.transactionsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.transactionsRv.setHasFixedSize(true)
        binding.transactionsRv.adapter = vendorTransactionAdapter.withLoadStateHeaderAndFooter(
            header = VendorTransactionLoaderAdapter(),
            footer = VendorTransactionLoaderAdapter()
        )
        vendorTransactionAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && vendorTransactionAdapter.itemCount < 1) {
                binding.transactionUnAvailableLl.makeViewVisible()
                binding.transactionsRv.makeViewGone()
            } else {
                binding.transactionUnAvailableLl.makeViewGone()
                binding.transactionsRv.makeViewVisible()
            }
        }

        vendorViewModel._selectedTransactionLiveData.observe(viewLifecycleOwner,
            Observer { selectedList ->
                if (selectedList.size > 0) {
                    binding.settleAllBtn.makeButtonEnabled()
                } else {
                    binding.settleAllBtn.makeButtonDisabled()
                }
            })

        vendorViewModel.settleVendorLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        fetchTransactions("unSettled")
                    }
                    getMainActivity()?.showSnackBar(it.data?.message)
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

    }

    private fun fetchTransactions(filterValue: String) {
        val partnerPayload = JsonObject()
        partnerPayload.addProperty("partnerId", partnerId)

        viewLifecycleOwner.lifecycleScope.launch {
            vendorViewModel.getVendorTransaction(filterValue, partnerPayload)
                .observe(viewLifecycleOwner) {
                    vendorTransactionAdapter.submitData(lifecycle, it)
                }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentVendorSettlementBinding.inflate(inflater, container, false)
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