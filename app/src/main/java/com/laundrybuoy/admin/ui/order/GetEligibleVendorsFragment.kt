package com.laundrybuoy.admin.ui.order

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.adapter.order.EligibleVendorAdapter
import com.laundrybuoy.admin.adapter.order.OrderTagsAdapter
import com.laundrybuoy.admin.databinding.FragmentGetEligibleVendorsBinding
import com.laundrybuoy.admin.model.order.EligibleVendorsModel
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetEligibleVendorsFragment : BaseBottomSheet() {

    private val profileViewModel by viewModels<ProfileViewModel>()
    private var _binding: FragmentGetEligibleVendorsBinding? = null
    private val binding get() = _binding!!
    private var orderIdReceived: String? = null
    private lateinit var eligibleVendorAdapter: EligibleVendorAdapter
    private lateinit var callback: (EligibleVendorsModel.Data) -> Unit
    fun setCallback(callback: (EligibleVendorsModel.Data) -> Unit) {
        this.callback = callback
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initRv()
        initObserver()
    }

    private fun initRv() {
        eligibleVendorAdapter =
            EligibleVendorAdapter(object : EligibleVendorAdapter.OnClickInterface {
                override fun onVendorClicked(doc: EligibleVendorsModel.Data) {
                    if (::callback.isInitialized) {
                        dialog?.dismiss()
                        callback.invoke(doc)
                    }

                }

            })
        binding.eligibleVendorsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.eligibleVendorsRv.adapter = eligibleVendorAdapter

    }

    private fun initObserver() {
        profileViewModel.eligibleVendorsLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true && it.data.data?.isNotEmpty() == true) {
                        setDataRv(it.data.data)
                    } else {
                        hideDataRv()
                    }

                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }

        })

    }

    private fun hideDataRv() {
        binding.vendorUnAvailableLl.makeViewVisible()
        binding.eligibleVendorsRv.makeViewGone()
    }

    private fun setDataRv(data: List<EligibleVendorsModel.Data?>) {
        binding.vendorUnAvailableLl.makeViewGone()
        binding.eligibleVendorsRv.makeViewVisible()
        eligibleVendorAdapter.submitList(data)

    }

    private fun init() {
        orderIdReceived = arguments?.getString(ORDER_ID)

        binding.backFromEligibleListIv.setOnClickListener {
            dialog?.dismiss()
        }

        val orderPayload = JsonObject()
        orderPayload.addProperty("orderId", orderIdReceived)
        profileViewModel.getEligibleVendors(orderPayload)

    }

    companion object {
        private const val ORDER_ID = "ORDER_ID"
        fun newInstance(
            orderId: String,
        ): GetEligibleVendorsFragment {
            val eligibleVendorsFragment = GetEligibleVendorsFragment()
            val args = Bundle()
            args.putString(ORDER_ID, orderId)
            eligibleVendorsFragment.arguments = args
            return eligibleVendorsFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGetEligibleVendorsBinding.inflate(inflater, container, false)
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