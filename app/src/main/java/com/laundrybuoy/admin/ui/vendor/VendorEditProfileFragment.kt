package com.laundrybuoy.admin.ui.vendor

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentEditProfileBinding
import com.laundrybuoy.admin.model.vendor.VendorProfileModel
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VendorEditProfileFragment : BaseFragment(), CompoundButton.OnCheckedChangeListener {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var vendorViewModel: VendorViewModel
    var vendorIdReceived: String? = null
    var profileData: VendorProfileModel.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserver()
        onClick()

    }

    private fun onClick() {

        binding.vendorDocRl.setOnClickListener {
            vendorIdReceived = arguments?.getString(VENDOR_ID)
            val updateFrag = VendorDocsFragment.newInstance(vendorIdReceived,
                VENDOR_DOCS, ArrayList(profileData?.photoId!!))
            updateFrag.setCallback {
                val vendorIdPayload = JsonObject()
                vendorIdPayload.addProperty("role", "partner")
                vendorIdPayload.addProperty("userId", vendorIdReceived)
                vendorViewModel.getVendorProfile(vendorIdPayload)
            }
            updateFrag.isCancelable = true
            updateFrag.show(childFragmentManager, "update_doc")
        }

        binding.nameLl.setOnClickListener {
            vendorIdReceived = arguments?.getString(VENDOR_ID)
            val updateFrag = VendorUpdateFragment.newInstance(vendorIdReceived,
                NAME, arrayListOf(binding.userNameProfileTv.text.toString())
            )
            updateFrag.setCallback {
                val vendorIdPayload = JsonObject()
                vendorIdPayload.addProperty("role", "partner")
                vendorIdPayload.addProperty("userId", vendorIdReceived)
                vendorViewModel.getVendorProfile(vendorIdPayload)
            }
            updateFrag.isCancelable = true
            updateFrag.show(childFragmentManager, "update_alt")
        }

        binding.altNumberLl.setOnClickListener {
            vendorIdReceived = arguments?.getString(VENDOR_ID)
            val updateFrag = VendorUpdateFragment.newInstance(vendorIdReceived,
                ALTERNATE_NUMBER, arrayListOf(binding.altNumberTv.text.toString())
            )
            updateFrag.setCallback {
                val vendorIdPayload = JsonObject()
                vendorIdPayload.addProperty("role", "partner")
                vendorIdPayload.addProperty("userId", vendorIdReceived)
                vendorViewModel.getVendorProfile(vendorIdPayload)
            }
            updateFrag.isCancelable = true
            updateFrag.show(childFragmentManager, "update_alt")
        }

        binding.primaryNumberLl.setOnClickListener {
            vendorIdReceived = arguments?.getString(VENDOR_ID)
            val updateFrag = VendorUpdateFragment.newInstance(vendorIdReceived,
                PRIMARY_NUMBER, arrayListOf(binding.primaryNumberTv.text.toString())
            )
            updateFrag.setCallback {
                val vendorIdPayload = JsonObject()
                vendorIdPayload.addProperty("role", "partner")
                vendorIdPayload.addProperty("userId", vendorIdReceived)
                vendorViewModel.getVendorProfile(vendorIdPayload)
            }
            updateFrag.isCancelable = true
            updateFrag.show(childFragmentManager, "update_primary")
        }

        binding.workingPincodeLl.setOnClickListener {
            vendorIdReceived = arguments?.getString(VENDOR_ID)
            var chipChildren: List<String> = arrayListOf()
            chipChildren = binding.pincodeChipGroup.children.map {
                (it as Chip).text.toString()
            }.toMutableList().filter {
                it.length == 6
            }
            val updateFrag = VendorUpdateFragment.newInstance(vendorIdReceived,
                WORKING_PINCODES, chipChildren as ArrayList<String>
            )
            updateFrag.setCallback {
                val vendorIdPayload = JsonObject()
                vendorIdPayload.addProperty("role", "partner")
                vendorIdPayload.addProperty("userId", vendorIdReceived)
                vendorViewModel.getVendorProfile(vendorIdPayload)
            }
            updateFrag.isCancelable = true
            updateFrag.show(childFragmentManager, "update_pincode")
        }

        binding.workingServiceLl.setOnClickListener {
            vendorIdReceived = arguments?.getString(VENDOR_ID)
            var chipChildren: List<String> = arrayListOf()
            chipChildren = binding.servicesChipGroup.children.map {
                (it as Chip).text.toString()
            }.toMutableList().filter {
                it.isNotEmpty()
            }
            val updateFrag = VendorUpdateFragment.newInstance(vendorIdReceived,
                SERVICES_OFFERED, chipChildren as ArrayList<String>
            )
            updateFrag.setCallback {
                val vendorIdPayload = JsonObject()
                vendorIdPayload.addProperty("role", "partner")
                vendorIdPayload.addProperty("userId", vendorIdReceived)
                vendorViewModel.getVendorProfile(vendorIdPayload)
            }
            updateFrag.isCancelable = true
            updateFrag.show(childFragmentManager, "update_service")
        }

        binding.workAddressLl.setOnClickListener {
            vendorIdReceived = arguments?.getString(VENDOR_ID)
            val updateFrag = UpdateAddressFragment.newInstance("workAddress", vendorIdReceived,
                arrayListOf(binding.workAddressTv.text.toString()),"vendor"
            )
            updateFrag.setCallback {
                val vendorIdPayload = JsonObject()
                vendorIdPayload.addProperty("role", "partner")
                vendorIdPayload.addProperty("userId", vendorIdReceived)
                vendorViewModel.getVendorProfile(vendorIdPayload)
            }
            getMainActivity()?.addFragment(true,
                getMainActivity()?.getVisibleFrame()!!,
                updateFrag)
        }

        binding.addressLl.setOnClickListener {
            vendorIdReceived = arguments?.getString(VENDOR_ID)
            val updateFrag = UpdateAddressFragment.newInstance("address", vendorIdReceived,
                arrayListOf(binding.addressTv.text.toString()),"vendor"
            )
            updateFrag.setCallback {
                val vendorIdPayload = JsonObject()
                vendorIdPayload.addProperty("role", "partner")
                vendorIdPayload.addProperty("userId", vendorIdReceived)
                vendorViewModel.getVendorProfile(vendorIdPayload)
            }
            getMainActivity()?.addFragment(true,
                getMainActivity()?.getVisibleFrame()!!,
                updateFrag)
        }


    }

    private fun initObserver() {
        vendorViewModel.vendorProfileLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Success -> {
                        if (it.data?.success == true && it.data?.data != null) {
                            setVendorProfileUI(it.data.data)
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
                        val vendorIdPayload = JsonObject()
                        vendorIdPayload.addProperty("role", "partner")
                        vendorIdPayload.addProperty("userId", vendorIdReceived)
                        vendorViewModel.getVendorProfile(vendorIdPayload)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })
    }

    private fun setVendorProfileUI(data: VendorProfileModel.Data) {

        profileData  = data
        binding.userNameProfileTv.text = data?.name ?: ""
        binding.primaryNumberTv.text = data?.mobile ?: ""
        binding.altNumberTv.text = data?.altMobile ?: ""

        binding.noOfDocTv.text = (data?.photoId?.size).toString()

        binding.activeStatusToggle.setOnCheckedChangeListener(null)
        binding.blockStatusToggle.setOnCheckedChangeListener(null)
        binding.activeStatusToggle.isChecked = data?.isActive == true
        binding.blockStatusToggle.isChecked = data?.isBlocked == true
        binding.activeStatusToggle.setOnCheckedChangeListener(this)
        binding.blockStatusToggle.setOnCheckedChangeListener(this)
        updateSwitchListener()

        binding.addressTv.text =
            data?.address?.line1 + "\n" + data?.address?.landmark +
                    data?.address?.city + "," + data?.address?.state + "," + data?.address?.pin

        binding.workAddressTv.text =
            data?.workAddress?.line1 + "\n" + data?.workAddress?.landmark +
                    data?.workAddress?.city + "," + data?.workAddress?.state + "," + data?.workAddress?.pin

        binding.pincodeChipGroup.removeAllViews()
        data?.workingPincode?.forEach { tagName ->
            binding.pincodeChipGroup.addView(createTagChip(requireContext(), tagName!!))
        }

        binding.servicesChipGroup.removeAllViews()
        data?.servicesOffered?.forEach { service ->
            binding.servicesChipGroup.addView(
                createTagChip(
                    requireContext(),
                    service?.serviceName!!
                )
            )
        }
    }

    private fun updateSwitchListener() {
        binding.activeStatusToggle.setOnCheckedChangeListener { compoundButton, isChecked ->
            toggleActiveStatus(isChecked)
        }
        binding.blockStatusToggle.setOnCheckedChangeListener { compoundButton, isChecked ->
            toggleBlockStatus(isChecked)
        }
    }

    private fun toggleBlockStatus(checked: Boolean) {
        vendorIdReceived = arguments?.getString(VENDOR_ID)
        val blockPayload = JsonObject()
        blockPayload.addProperty(
            "isBlocked",
            checked
        )
        vendorViewModel.updateVendor(vendorIdReceived ?: "", blockPayload)
    }

    private fun toggleActiveStatus(checked: Boolean) {
        vendorIdReceived = arguments?.getString(VENDOR_ID)
        val activePayload = JsonObject()
        activePayload.addProperty(
            "isActive",
            checked
        )
        vendorViewModel.updateVendor(vendorIdReceived ?: "", activePayload)

    }

    private fun createTagChip(context: Context, chipName: String): Chip {
        return Chip(context).apply {
            text = chipName
            setChipBackgroundColorResource(R.color.profileHeading)
            isCloseIconVisible = false
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setTextAppearance(R.style.ChipTextAppearance)
        }

    }

    companion object {
        private const val VENDOR_ID = "VENDOR_ID"

        fun newInstance(
            vendorId: String,
        ): VendorEditProfileFragment {
            val viewFragment = VendorEditProfileFragment()
            val args = Bundle()
            args.putString(VENDOR_ID, vendorId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    private fun init() {
        vendorViewModel = ViewModelProvider(requireActivity()).get(VendorViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
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

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

    }

}