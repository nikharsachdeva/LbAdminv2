package com.laundrybuoy.admin.ui.vendor

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.vendor.SelectServicesAdapter
import com.laundrybuoy.admin.databinding.FragmentEditProfileBinding
import com.laundrybuoy.admin.databinding.FragmentVendorUpdateBinding
import com.laundrybuoy.admin.model.filter.ServicesModel
import com.laundrybuoy.admin.model.vendor.ServicesOfferedPayload
import com.laundrybuoy.admin.model.vendor.WorkingPincodePayload
import com.laundrybuoy.admin.utils.*
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable


const val SCREEN_TYPE = "screenType"
const val VENDOR_ID = "VENDOR_ID"
const val ADDRESS_TYPE = "ADDRESS_TYPE"
const val ALTERNATE_NUMBER = "alternateNumber"
const val VENDOR_DOCS = "vendor_docs"
const val RIDER_DOCS = "rider_docs"
const val NAME = "name"
const val PRIMARY_NUMBER = "primaryNumber"
const val WORKING_PINCODES = "workingPincodes"
const val SERVICES_OFFERED = "servicesOffered"
const val SCREEN_OBJ = "screenObj"
const val USER_TYPE = "USER_TYPE"

@AndroidEntryPoint
class VendorUpdateFragment : BaseBottomSheet() {

    private var _binding: FragmentVendorUpdateBinding? = null
    private val binding get() = _binding!!
    val compositeDisposable = CompositeDisposable()
    private var screenTypeReceived: String? = null
    private var screenDataReceived: ArrayList<String>? = null
    private val vendorViewModel by viewModels<VendorViewModel>()
    private val serviceList = arrayListOf<ServicesModel.Data>()
    var selectedServiceList: ArrayList<ServicesModel.Data> = ArrayList()
    private lateinit var servicesAdapter: SelectServicesAdapter
    var vendorIdReceived: String? = null


    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screenTypeReceived = arguments?.getString(SCREEN_TYPE)
        screenDataReceived = arguments?.getStringArrayList(SCREEN_OBJ)
        vendorIdReceived = arguments?.getString(VENDOR_ID)

        initObserver()
        onClicks()
        setUIAccordingly(screenTypeReceived, screenDataReceived)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            vendorIdReceived1: String?,
            screenType: String,
            screenObj: ArrayList<String>,
        ) =
            VendorUpdateFragment().apply {
                arguments = Bundle().apply {
                    putString(VENDOR_ID, vendorIdReceived1)
                    putString(SCREEN_TYPE, screenType)
                    putStringArrayList(SCREEN_OBJ, screenObj)
                }
            }
    }

    private fun onClicks() {
        binding.closeUpdateProfileIv.setOnClickListener {
            dialog?.dismiss()
        }

        binding.updateProfileBtn.setOnClickListener {

            when (screenTypeReceived) {

                ALTERNATE_NUMBER -> {
                    if (binding.enterNumberEt.text.trim().length == 10) {
                        val numberPayload = JsonObject()
                        numberPayload.addProperty(
                            "altMobile",
                            (binding.enterNumberEt.text.toString())
                        )
                        vendorViewModel.updateVendorNumber(vendorIdReceived
                            ?: "", numberPayload)
                    }
                }

                PRIMARY_NUMBER -> {
                    if (binding.enterNumberEt.text.trim().length == 10) {
                        val numberPayload = JsonObject()
                        numberPayload.addProperty(
                            "mobile",
                            (binding.enterNumberEt.text.toString())
                        )
                        vendorViewModel.updateVendorNumber(vendorIdReceived
                            ?: "", numberPayload)
                    }
                }

                NAME ->{
                    if (binding.enterNameEt.text.trim().isNotEmpty()) {
                        val namePayload = JsonObject()
                        namePayload.addProperty(
                            "name",
                            (binding.enterNameEt.text.toString())
                        )
                        vendorViewModel.updateVendor(vendorIdReceived
                            ?: "", namePayload)
                    }
                }

                WORKING_PINCODES -> {
                    if (vendorViewModel.pincodeArray.value?.size!! > 0) {
                        val listOfPinPayload = WorkingPincodePayload(
                            vendorViewModel.pincodeArray.value
                        )
                        vendorViewModel.updateVendor(vendorIdReceived?:"",Gson().fromJson(Gson().toJson(listOfPinPayload),JsonObject::class.java))
                    }
                }
                SERVICES_OFFERED -> {

                    if (vendorViewModel.serviceArraySize.value?.size!! > 0) {
                        val listOfServices = selectedServiceList.map {
                            it.id
                        }
                        val listOfServicePayload = ServicesOfferedPayload(
                            listOfServices
                        )
                        vendorViewModel.updateVendor(vendorIdReceived?:"",Gson().fromJson(Gson().toJson(listOfServicePayload),JsonObject::class.java))

                    }
                }
            }
        }
    }

    private fun setUIAccordingly(
        screenTypeReceived: String?,
        screenDataReceived: ArrayList<String>?,
    ) {
        when (screenTypeReceived) {

            ALTERNATE_NUMBER -> {
                binding.updateProfileHeading.text = "Update Alternate Number"
                binding.updateAltNumberRl.makeViewVisible()
                binding.updatePincodeLl.makeViewGone()
                binding.updateServicesRl.makeViewGone()
                binding.updateNameRl.makeViewGone()
                binding.enterNumberEt.setText(screenDataReceived?.get(0).toString())
            }

            NAME ->{
                binding.updateProfileHeading.text = "Update Name"
                binding.updateAltNumberRl.makeViewGone()
                binding.updatePincodeLl.makeViewGone()
                binding.updateServicesRl.makeViewGone()
                binding.updateNameRl.makeViewVisible()
                binding.enterNameEt.setText(screenDataReceived?.get(0).toString())
            }

            PRIMARY_NUMBER -> {
                binding.updateProfileHeading.text = "Update Primary Number"
                binding.updateAltNumberRl.makeViewVisible()
                binding.updatePincodeLl.makeViewGone()
                binding.updateServicesRl.makeViewGone()
                binding.updateNameRl.makeViewGone()
                binding.enterNumberEt.setText(screenDataReceived?.get(0).toString())
            }

            WORKING_PINCODES -> {
                binding.updateProfileHeading.text = "Update Working Pincodes"
                binding.updateAltNumberRl.makeViewGone()
                binding.updatePincodeLl.makeViewVisible()
                binding.updateServicesRl.makeViewGone()
                binding.updateNameRl.makeViewGone()
                vendorViewModel.pushUpdatedPincodeList(screenDataReceived?.toMutableList()!!)
            }

            SERVICES_OFFERED -> {
                binding.updateProfileHeading.text = "Update Services Offered"
                binding.updateAltNumberRl.makeViewGone()
                binding.updatePincodeLl.makeViewGone()
                binding.updateServicesRl.makeViewVisible()
                binding.updateNameRl.makeViewGone()
                vendorViewModel.pushUpdatedServiceList(serviceList)
                vendorViewModel.fetchServices()
            }

        }
    }

    private fun initObserver() {

        vendorViewModel.vendorNumberLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        if (::callback.isInitialized) {
                            dialog?.dismiss()
                            callback.invoke()
                        }
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })

        vendorViewModel.updateVendorLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        if (::callback.isInitialized) {
                            dialog?.dismiss()
                            callback.invoke()
                        }
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })

        vendorViewModel.pincodeArray.observe(viewLifecycleOwner, Observer {
            binding.chipGroupPincodeCg.removeAllViews()
            if (it.isEmpty()) {
                binding.updateProfileBtn.makeButtonDisabled()
            } else {
                it.map { tag->
                    binding.chipGroupPincodeCg.addView(createTagChip(requireContext(),tag))
                }
                binding.updateProfileBtn.makeButtonEnabled()
            }
        })

        when (screenTypeReceived) {

            ALTERNATE_NUMBER, PRIMARY_NUMBER -> {
                compositeDisposable.add(
                    RxTextView.textChanges(binding.enterNumberEt)
                        .subscribe {
                            if (it.trim().length == 10) {
                                binding.updateProfileBtn.makeButtonEnabled()
                            } else {
                                binding.updateProfileBtn.makeButtonDisabled()
                            }
                        })
            }

            WORKING_PINCODES -> {
                compositeDisposable.add(
                    RxTextView.textChanges(binding.enterPincodeEt)
                        .subscribe {
                            if (it.trim().length == 6) {
                                val currentPinList = vendorViewModel.pincodeArray.value
                                if (currentPinList?.contains(it) == true) {
                                    Toast.makeText(requireContext(),
                                        "Value already exist",
                                        Toast.LENGTH_SHORT).show()
                                } else {
                                    currentPinList?.add(it.toString())
                                }
                                binding.enterPincodeEt.text?.clear()
                                vendorViewModel.pushUpdatedPincodeList(currentPinList!!)

                            }
                        })
            }

        }

        vendorViewModel.servicesLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        Log.d("abcd-->", "servicesLiveData: "+Gson().toJson(it.data))
                        screenDataReceived?.forEach { received ->
                            it.data.data.map { model ->
                                if (model.serviceName?.equals(received) == true) {
                                    model.isSelected = true
                                }
                            }
                        }

                        initServicesRv(it.data.data)
                        vendorViewModel.pushUpdatedServiceList(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })

        vendorViewModel.serviceArraySize.observe(viewLifecycleOwner, Observer {
            val selected: ArrayList<ServicesModel.Data> = ArrayList()
            for (i in it.indices) {
                if (it[i].isSelected) {
                    selected.add(it[i])
                }
            }
            selectedServiceList = selected

            if (selected.size > 0) {
                binding.updateProfileBtn.makeButtonEnabled()
            } else {
                binding.updateProfileBtn.makeButtonDisabled()
            }
        })

    }

    private fun createTagChip(context: Context, chipName: String): Chip {

        return Chip(context).apply {
            text = chipName
            setChipBackgroundColorResource(R.color.profileHeading)
            isCloseIconVisible = true
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setTextAppearance(R.style.ChipTextAppearance)
            setOnCloseIconClickListener {
                binding.chipGroupPincodeCg.removeView(this as View)
                val currentPinList = vendorViewModel.pincodeArray.value
                currentPinList?.remove(chipName)
                vendorViewModel.pushUpdatedPincodeList(currentPinList?.toMutableList()!!)

            }

        }

    }

    private fun initServicesRv(data: ArrayList<ServicesModel.Data>) {
        Log.d("abcd-->", "initServicesRv: "+Gson().toJson(data))
        servicesAdapter = SelectServicesAdapter(
            requireContext(),
            data,
            object : SelectServicesAdapter.OnClickInterface {
                override fun onServiceClicked(serviceList: ArrayList<ServicesModel.Data>) {
                    vendorViewModel.pushUpdatedServiceList(serviceList)
                }

            })
        binding.updateServicesRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.updateServicesRv.adapter = servicesAdapter


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentVendorUpdateBinding.inflate(inflater, container, false)
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