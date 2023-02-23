package com.laundrybuoy.admin.ui.vendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.databinding.FragmentVendorTransactionFigBinding
import com.laundrybuoy.admin.model.transaction.TransactionResponse
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VendorTransactionFigFragment : BaseBottomSheet() {

    private var _binding: FragmentVendorTransactionFigBinding? = null
    private val binding get() = _binding!!
    private val vendorViewModel by viewModels<VendorViewModel>()
    private var partnerId: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        partnerId = "63c718054b87ecd1aa0893ed"
        initObserver()
        fetchMetadata()
        onClick()

    }

    private fun fetchMetadata() {
        val partnerPayload = JsonObject()
        partnerPayload.addProperty("partnerId", partnerId)
        vendorViewModel.getVendorTransactionMeta("all",partnerPayload)
    }

    private fun onClick() {
        binding.closeFilterFigIv.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun initObserver() {

        vendorViewModel.vendorTransactionMetaLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true && it.data?.data?.metaData != null) {
                        setUI(it.data?.data?.metaData)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

    }

    companion object {
        private const val TRANSACTION_METADATA = "TRANSACTION_METADATA"
        fun newInstance(
            metaDataReceived: TransactionResponse.Data.MetaData?,
        ): VendorTransactionFigFragment {
            val figFragment = VendorTransactionFigFragment()
            val args = Bundle()
            args.putParcelable(TRANSACTION_METADATA, metaDataReceived)
            figFragment.arguments = args
            return figFragment
        }
    }

    private fun setUI(metaDataReceived: TransactionResponse.Data.MetaData) {
        binding.totalAmountValueTv.text = "₹" + metaDataReceived.totalAmount.toString()
        binding.totalOrderTv.text = metaDataReceived.totalOrders.toString()

        binding.settledAmountValueTv.text = "₹" + metaDataReceived.settledAmount.toString()
        binding.settledOrderTv.text = metaDataReceived.settledOrders.toString()

        binding.unsettledAmountValueTv.text = "₹" + metaDataReceived.unSettledAmount.toString()
        binding.unsettledOrderTv.text = metaDataReceived.unSettledOrders.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentVendorTransactionFigBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }



}