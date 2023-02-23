package com.laundrybuoy.admin.ui.rider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.databinding.FragmentRiderEditProfileBinding
import com.laundrybuoy.admin.model.rider.RiderProfileModel
import com.laundrybuoy.admin.ui.vendor.*
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.RiderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RiderEditProfileFragment : BaseFragment(), CompoundButton.OnCheckedChangeListener {

    private var _binding: FragmentRiderEditProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var riderViewModel: RiderViewModel
    var riderIdReceived: String? = null
    var profileData: RiderProfileModel.Data? = null

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

        binding.addressLl.setOnClickListener {
            val updateFrag = UpdateAddressFragment.newInstance("address", riderIdReceived,
                arrayListOf(binding.addressTv.text.toString()),"rider"
            )
            updateFrag.setCallback {
                val riderIdPayload = JsonObject()
                riderIdPayload.addProperty("role", "rider")
                riderIdPayload.addProperty("userId", riderIdReceived)
                riderViewModel.getRiderProfile(riderIdPayload)
            }
            getMainActivity()?.addFragment(true,
                getMainActivity()?.getVisibleFrame()!!,
                updateFrag)
        }

        binding.riderDocRl.setOnClickListener {
            val updateFrag = VendorDocsFragment.newInstance(riderIdReceived,
                RIDER_DOCS, ArrayList(profileData?.photoId!!))
            updateFrag.setCallback {
                val riderIdPayload = JsonObject()
                riderIdPayload.addProperty("role", "rider")
                riderIdPayload.addProperty("userId", riderIdReceived)
                riderViewModel.getRiderProfile(riderIdPayload)
            }
            updateFrag.isCancelable = true
            updateFrag.show(childFragmentManager, "update_doc")
        }

        binding.nameLl.setOnClickListener {
            val updateFrag = RiderUpdateFragment.newInstance(riderIdReceived?:"",
                NAME, binding.userNameProfileTv.text.toString()
            )
            updateFrag.setCallback {
                val riderIdPayload = JsonObject()
                riderIdPayload.addProperty("role", "rider")
                riderIdPayload.addProperty("userId", riderIdReceived)
                riderViewModel.getRiderProfile(riderIdPayload)
            }
            updateFrag.isCancelable = true
            updateFrag.show(childFragmentManager, "update_alt")
        }

        binding.altNumberLl.setOnClickListener {
            val updateFrag = RiderUpdateFragment.newInstance(riderIdReceived?:"",
                ALTERNATE_NUMBER, binding.altNumberTv.text.toString()
            )
            updateFrag.setCallback {
                val riderIdPayload = JsonObject()
                riderIdPayload.addProperty("role", "rider")
                riderIdPayload.addProperty("userId", riderIdReceived)
                riderViewModel.getRiderProfile(riderIdPayload)
            }
            updateFrag.isCancelable = true
            updateFrag.show(childFragmentManager, "update_alt")
        }

        binding.primaryNumberLl.setOnClickListener {
            val updateFrag = RiderUpdateFragment.newInstance(riderIdReceived?:"",
                PRIMARY_NUMBER, binding.primaryNumberTv.text.toString()
            )
            updateFrag.setCallback {
                val riderIdPayload = JsonObject()
                riderIdPayload.addProperty("role", "rider")
                riderIdPayload.addProperty("userId", riderIdReceived)
                riderViewModel.getRiderProfile(riderIdPayload)
            }
            updateFrag.isCancelable = true
            updateFrag.show(childFragmentManager, "update_primary")
        }
    }

    private fun init() {
        riderIdReceived = arguments?.getString(RIDER_ID)
        riderViewModel = ViewModelProvider(requireActivity()).get(RiderViewModel::class.java)
    }

    private fun initObserver() {
        riderViewModel.riderProfileLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Success -> {
                        if (it.data?.success == true && it.data?.data != null) {
                            setRiderProfileUI(it.data.data)
                        }
                    }
                    is NetworkResult.Error -> {
                        getMainActivity()?.showSnackBar(it.message)
                    }
                }
            })

        riderViewModel.updateRiderLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        val vendorIdPayload = JsonObject()
                        vendorIdPayload.addProperty("role", "rider")
                        vendorIdPayload.addProperty("userId", riderIdReceived)
                        riderViewModel.getRiderProfile(vendorIdPayload)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })

    }

    private fun setRiderProfileUI(data: RiderProfileModel.Data) {
        profileData = data
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
        val blockPayload = JsonObject()
        blockPayload.addProperty(
            "isBlocked",
            checked
        )
        riderViewModel.updateRider(riderIdReceived ?: "", blockPayload)
    }

    private fun toggleActiveStatus(checked: Boolean) {
        val activePayload = JsonObject()
        activePayload.addProperty(
            "isActive",
            checked
        )
        riderViewModel.updateRider(riderIdReceived ?: "", activePayload)

    }


    companion object {
        private const val RIDER_ID = "RIDER_ID"

        fun newInstance(
            riderId: String,
        ): RiderEditProfileFragment {
            val viewFragment = RiderEditProfileFragment()
            val args = Bundle()
            args.putString(RIDER_ID, riderId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRiderEditProfileBinding.inflate(inflater, container, false)
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