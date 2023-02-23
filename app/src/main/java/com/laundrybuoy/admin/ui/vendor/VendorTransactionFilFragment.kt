package com.laundrybuoy.admin.ui.vendor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentVendorTransactionFigBinding
import com.laundrybuoy.admin.databinding.FragmentVendorTransactionFilBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VendorTransactionFilFragment : BaseBottomSheet() {

    private var _binding: FragmentVendorTransactionFilBinding? = null
    private val binding get() = _binding!!
    var selectedFilter: String? = null
    private lateinit var callback: (String) -> Unit
    fun setCallback(callback: (String) -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListener()

    }

    private fun clickListener() {
        binding.submitDateBtn.setOnClickListener {
            if (selectedFilter.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please select a filter", Toast.LENGTH_SHORT).show()
            } else {
                if (::callback.isInitialized) {
                    dialog?.dismiss()
                    callback.invoke(selectedFilter!!)
                }
            }
        }

        binding.transactionAll.setOnClickListener {
            binding.isAllSelected = true
            binding.isSettledSelected = false
            binding.isUnSettledSelected = false
            selectedFilter = "all"
        }

        binding.transactionSettled.setOnClickListener {
            binding.isAllSelected = false
            binding.isSettledSelected = true
            binding.isUnSettledSelected = false
            selectedFilter = "settled"

        }

        binding.transactionUnsettled.setOnClickListener {
            binding.isAllSelected = false
            binding.isSettledSelected = false
            binding.isUnSettledSelected = true
            selectedFilter = "unSettled"

        }


        binding.closeTransFilterIv.setOnClickListener {
            dialog?.dismiss()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentVendorTransactionFilBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

}