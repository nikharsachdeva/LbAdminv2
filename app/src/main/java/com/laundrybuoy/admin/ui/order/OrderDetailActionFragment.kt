package com.laundrybuoy.admin.ui.order

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.order.PhoneTagsAdapter
import com.laundrybuoy.admin.databinding.FragmentOrderDetailActionBinding
import com.laundrybuoy.admin.model.order.EligibleVendorsModel
import com.laundrybuoy.admin.model.order.OrderDetailResponse
import com.laundrybuoy.admin.model.profile.AllListingResponse
import com.laundrybuoy.admin.ui.profile.AllListFragment
import com.laundrybuoy.admin.utils.*
import com.laundrybuoy.admin.viewmodel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailActionFragment : BaseFragment() {

    private var _binding: FragmentOrderDetailActionBinding? = null
    private val binding get() = _binding!!
    lateinit var ordersViewModel: OrdersViewModel

    private lateinit var vendorNumbersAdapter: PhoneTagsAdapter
    private lateinit var riderNumbersAdapter: PhoneTagsAdapter
    private lateinit var customerNumbersAdapter: PhoneTagsAdapter
    private val ordersViewModel2 by viewModels<OrdersViewModel>()
    var phoneNumberToCall: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        onClick()
        initObservers()

    }

    private fun onClick() {
        binding.changeRiderTv.setOnClickListener {
            val frag = AllListFragment.newInstance("rider", "order_detail")
            frag.setCallback { rider ->
                createUpdatePayload("rider", rider, null)
            }
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                frag
            )
        }

        binding.changeVendorTv.setOnClickListener {
            val frag =
                GetEligibleVendorsFragment.newInstance(ordersViewModel.orderDetailLiveData.value?.data?.data?.orderDetails?.id
                    ?: "")
            frag.setCallback { partner ->
                createUpdatePayload("partner", null, partner)
            }
            frag.isCancelable = true
            frag.show(childFragmentManager, "eligible_vendor")
        }
    }

    private fun createUpdatePayload(
        userType: String,
        riderObj: AllListingResponse.Data.Partner? = null,
        partnerObj: EligibleVendorsModel.Data? = null,
    ) {
        val updatePayload = JsonObject()
        updatePayload.addProperty(
            "orderId", ordersViewModel.orderDetailLiveData.value?.data?.data?.orderDetails?.id)
        updatePayload.addProperty(
            "desc", "Action by admin")
        if (userType == "rider") {
            updatePayload.addProperty(
                "riderId", riderObj?.id)
            ordersViewModel2.updateRider(updatePayload)
        } else if (userType == "partner") {
            updatePayload.addProperty(
                "partnerId", partnerObj?.id)
            ordersViewModel2.updatePartner(updatePayload)
        }

    }

    private fun init() {
        ordersViewModel = ViewModelProvider(requireActivity()).get(OrdersViewModel::class.java)
    }

    private fun initObservers() {
        ordersViewModel.orderDetailLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data?.data != null) {
                        setUI(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

        ordersViewModel2.updateRiderLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        getMainActivity()?.showSnackBar(it.data.message ?: "")
                        ordersViewModel.getDetailedOrder(ordersViewModel.orderDetailLiveData.value?.data?.data?.orderDetails?.id
                            ?: "")
                    } else {
                        getMainActivity()?.showSnackBar(it.data?.message ?: "")
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

        ordersViewModel2.updatePartnerLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        getMainActivity()?.showSnackBar(it.data.message ?: "")
                        ordersViewModel.getDetailedOrder(ordersViewModel.orderDetailLiveData.value?.data?.data?.orderDetails?.id
                            ?: "")
                    } else {
                        getMainActivity()?.showSnackBar(it.data?.message ?: "")
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })
    }

    private fun setUI(data: OrderDetailResponse.Data) {
        if (data.orderDetails?.partnerId != null) {
            binding.vendorNameTv.makeViewVisible()
            binding.vendorNumberRv.makeViewVisible()
            binding.vendorUnavailableTv.makeViewGone()
            binding.vendorNameTv.text = data.orderDetails.partnerId.name
            vendorNumbersAdapter = PhoneTagsAdapter(object : PhoneTagsAdapter.PhoneClickListener {
                override fun onPhoneClick(number: String) {
                    if (number.isNotEmpty()) {
                        phoneNumberToCall = number
                        checkPermission()

                    }
                }

            })
            binding.vendorNumberRv.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.vendorNumberRv.adapter = vendorNumbersAdapter
            vendorNumbersAdapter.submitList(data.orderDetails.partnerId.getVendorPhoneNumbers())
        } else {
            binding.vendorNameTv.makeViewGone()
            binding.vendorNumberRv.makeViewGone()
            binding.vendorUnavailableTv.makeViewVisible()
        }

        if (data.orderDetails?.riderId != null) {
            binding.riderNameTv.makeViewVisible()
            binding.riderNumberRv.makeViewVisible()
            binding.riderUnavailableTv.makeViewGone()

            binding.riderNameTv.text = data.orderDetails.riderId.name
            riderNumbersAdapter = PhoneTagsAdapter(object : PhoneTagsAdapter.PhoneClickListener {
                override fun onPhoneClick(number: String) {
                    if (number.isNotEmpty()) {
                        phoneNumberToCall = number
                        checkPermission()
                    }
                }

            })
            binding.riderNumberRv.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.riderNumberRv.adapter = riderNumbersAdapter
            riderNumbersAdapter.submitList(data.orderDetails.riderId.getRiderPhoneNumbers())
        } else {
            binding.riderNameTv.makeViewGone()
            binding.riderNumberRv.makeViewGone()
            binding.riderUnavailableTv.makeViewVisible()
        }


        if (data.orderDetails?.userId != null) {
            binding.customerNameTv.makeViewVisible()
            binding.customerNumberRv.makeViewVisible()
            binding.customerUnavailableTv.makeViewGone()

            binding.customerNameTv.text = data.orderDetails.userId.name
            customerNumbersAdapter = PhoneTagsAdapter(object : PhoneTagsAdapter.PhoneClickListener {
                override fun onPhoneClick(number: String) {
                    if (number.isNotEmpty()) {
                        phoneNumberToCall = number
                        checkPermission()
                    }
                }

            })
            binding.customerNumberRv.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.customerNumberRv.adapter = customerNumbersAdapter
            customerNumbersAdapter.submitList(data.orderDetails.userId.getCustomerPhoneNumbers())
        } else {
            binding.customerNameTv.makeViewGone()
            binding.customerNumberRv.makeViewGone()
            binding.customerUnavailableTv.makeViewVisible()
        }


    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CALL_PHONE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    42
                )
            }
        } else {
            // Permission has already been granted
            callPhone()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray,
    ) {
        if (requestCode == 42) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                callPhone()
            } else {
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }

    private fun callPhone() {
        if (phoneNumberToCall.isNullOrEmpty()) {
            getMainActivity()?.showSnackBar("Can not call from this screen")
        } else {
            val intent = Intent(Intent.ACTION_CALL);
            intent.data = Uri.parse("tel:${phoneNumberToCall}")
            startActivity(intent)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOrderDetailActionBinding.inflate(inflater, container, false)
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