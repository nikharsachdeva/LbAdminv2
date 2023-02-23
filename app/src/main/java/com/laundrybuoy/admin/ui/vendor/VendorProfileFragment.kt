package com.laundrybuoy.admin.ui.vendor

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.vendor.VendorAttendanceAdapter
import com.laundrybuoy.admin.adapter.vendor.VendorFilterAdapter
import com.laundrybuoy.admin.adapter.vendor.VendorServicesAdapter
import com.laundrybuoy.admin.databinding.FragmentVendorProfileBinding
import com.laundrybuoy.admin.model.vendor.VendorAttendanceModel
import com.laundrybuoy.admin.model.vendor.VendorFiguresModel
import com.laundrybuoy.admin.model.vendor.VendorProfileModel
import com.laundrybuoy.admin.model.vendor.VendorServices
import com.laundrybuoy.admin.ui.order.OrderListingFragment
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.SimpleItemDecorator
import com.laundrybuoy.admin.utils.loadImageWithGlide
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class VendorProfileFragment : BaseFragment() {

    private var _binding: FragmentVendorProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var vendorServicesAdapter: VendorServicesAdapter
    private lateinit var vendorPinCodesAdapter: VendorServicesAdapter
    private lateinit var vendorAttendanceAdapter: VendorAttendanceAdapter
    private lateinit var vendorFilterAdapter: VendorFilterAdapter

    lateinit var vendorViewModel: VendorViewModel
    var vendorIdReceived: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentVendorProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val VENDOR_ID = "VENDOR_ID"

        fun newInstance(
            vendorId: String,
        ): VendorProfileFragment {
            val viewFragment = VendorProfileFragment()
            val args = Bundle()
            args.putString(VENDOR_ID, vendorId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initServicesRv()
        initPinCodeRv()
        initFiltersRv()

        initObservers()

        initAttendanceRv()
        vendorAttendanceAdapter.submitList(getDummyAttendanceList())


        fetchVendorProfile()
        val year = Calendar.getInstance().get(Calendar.YEAR);
        vendorViewModel.setChartYear(year.toString())
    }

    private fun fetchVendorFigures(vendorIdPayload: JsonObject) {
        vendorViewModel.getVendorFigures(vendorIdPayload)
    }

    private fun initObservers() {

        vendorViewModel._chartYearLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { year ->

            binding.chartYearTv.text = "${year}"

            val vendorIdPayload = JsonObject()
            vendorIdPayload.addProperty("from", "${year}-01-01T00:00Z")
            vendorIdPayload.addProperty("to", "${year}-12-31T23:59Z")
            vendorIdPayload.addProperty("partnerId", vendorIdReceived)


            fetchVendorFigures(vendorIdPayload)
        })


        vendorViewModel.vendorProfileLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Success -> {
                        if (it.data?.success == true && it.data?.data != null) {
                            setVendorProfileUI(it.data?.data)
                        }
                    }
                    is NetworkResult.Error -> {
                        getMainActivity()?.showSnackBar(it.message)
                    }
                }
            })

        vendorViewModel.vendorFiguresLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Success -> {
                        if (it.data?.success == true && it.data?.data != null) {
                            setVendorFiguresRv(it.data?.data)
                        }
                    }
                    is NetworkResult.Error -> {
                        getMainActivity()?.showSnackBar(it.message)
                    }
                }
            })
    }

    private fun setVendorFiguresRv(data: List<VendorFiguresModel.VendorFiltersModelItem>) {
        vendorFilterAdapter.submitList(data)
    }

    private fun setVendorProfileUI(vendorData: VendorProfileModel.Data) {

        binding.vendorNameTv.text = vendorData.name ?: ""
        binding.vendorProfilePicIv.loadImageWithGlide(vendorData.profile ?: "")
        when (vendorData.isActive) {
            true -> {
                binding.vendorActiveStsTv.text = "Checked-In"
                binding.vendorActiveStsTv.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#30b856"
                        )
                    )
                )
                binding.vendorActiveStsTv.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#1030b856"))
            }
            false -> {
                binding.vendorActiveStsTv.text = "Checked-Out"
                binding.vendorActiveStsTv.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#fc2254"
                        )
                    )
                )
                binding.vendorActiveStsTv.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#10fc2254"))
            }
            else -> {
                binding.vendorActiveStsTv.text = ""
            }
        }
        when (vendorData.isBlocked) {
            true -> {
                binding.vendorBlockStsTv.text = "Blocked"
                binding.vendorBlockStsTv.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#fc2254"
                        )
                    )
                )
                binding.vendorBlockStsTv.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#10fc2254"))
            }
            false -> {
                binding.vendorBlockStsTv.text = "Active"
                binding.vendorBlockStsTv.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#30b856"
                        )
                    )
                )
                binding.vendorBlockStsTv.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#1030b856"))
            }
            else -> {
                binding.vendorBlockStsTv.text = ""
            }
        }
        binding.vendorPrimaryNoTv.text = vendorData?.mobile ?: ""
        binding.vendorSecNoTv.text = vendorData?.altMobile ?: ""

        val addressString = vendorData.workAddress?.line1 + " " +
                vendorData.workAddress?.landmark + " " +
                vendorData.workAddress?.city + " " +
                vendorData.workAddress?.pin
        binding.vendorWorkAddressTv.text = addressString ?: ""

        setPinCodeRv(vendorData)
        setServicesRv(vendorData)

    }

    private fun setServicesRv(vendorData: VendorProfileModel.Data) {
        val listOfServices: MutableList<VendorServices.VendorServicesItem> = arrayListOf()
        vendorData.servicesOffered?.forEach {
            listOfServices.add(
                VendorServices.VendorServicesItem(
                    id = it?.id,
                    serviceName = it?.serviceName
                )
            )
        }
        vendorServicesAdapter.submitList(listOfServices)
    }

    private fun setPinCodeRv(vendorData: VendorProfileModel.Data) {
        val listOfPinCodes: MutableList<VendorServices.VendorServicesItem> = arrayListOf()
        vendorData.workingPincode?.forEach {
            listOfPinCodes.add(
                VendorServices.VendorServicesItem(
                    id = it,
                    serviceName = it
                )
            )
        }
        vendorPinCodesAdapter.submitList(listOfPinCodes)
    }


    private fun fetchVendorProfile() {
        vendorIdReceived = arguments?.getString(VENDOR_ID)
        val vendorIdPayload = JsonObject()
        vendorIdPayload.addProperty("role", "partner")
        vendorIdPayload.addProperty("userId", vendorIdReceived)
        vendorViewModel.getVendorProfile(vendorIdPayload)
    }

    private fun init() {
        vendorViewModel = ViewModelProvider(requireActivity()).get(VendorViewModel::class.java)

        binding.yearPickerRl.setOnClickListener {
            val yearFrag = YearPickerFragment()
            yearFrag.setCallback { year ->
                vendorViewModel.setChartYear(year)
            }
            yearFrag.isCancelable = true
            yearFrag.show(childFragmentManager, "year_fragment")
        }

    }

    private fun initFiltersRv() {
        vendorFilterAdapter =
            VendorFilterAdapter(object : VendorFilterAdapter.OnClickInterface {
                override fun onFilterClicked(filter: VendorFiguresModel.VendorFiltersModelItem) {
                    goToListingPage(filter)

                }

            })
        binding.vendorFiguresRv.addItemDecoration(SimpleItemDecorator(15, 15))

        val layoutManagerFilter =
            GridLayoutManager(requireContext(), 3)
        binding.vendorFiguresRv.layoutManager = layoutManagerFilter
        binding.vendorFiguresRv.adapter = vendorFilterAdapter
    }

    private fun goToListingPage(filter: VendorFiguresModel.VendorFiltersModelItem) {

        val metaData = JsonObject()
        metaData.addProperty("screenType", "partnerProfile")
        metaData.addProperty("partnerId", vendorIdReceived)
        metaData.addProperty("from", "${vendorViewModel._chartYearLiveData.value}-01-01T00:00:00Z")
        metaData.addProperty("to", "${vendorViewModel._chartYearLiveData.value}-12-31T23:59:59Z")
        metaData.addProperty("url", filter.baseUrl)

        val listingFrag = OrderListingFragment.newInstance(metaData.toString())
        getMainActivity()?.addFragment(true,getMainActivity()?.getVisibleFrame()!!,listingFrag)

    }

    private fun initAttendanceRv() {
        vendorAttendanceAdapter =
            VendorAttendanceAdapter()

        val layoutManagerAttendance =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.vendorAttendanceRv.layoutManager = layoutManagerAttendance
        binding.vendorAttendanceRv.adapter = vendorAttendanceAdapter
    }

    private fun getCalculatedDate(dateFormat: String?, days: Int): String? {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }


    private fun initPinCodeRv() {
        vendorPinCodesAdapter =
            VendorServicesAdapter()

        val layoutManagerServices = FlexboxLayoutManager(requireContext())
        layoutManagerServices.flexDirection = FlexDirection.ROW
        layoutManagerServices.justifyContent = (JustifyContent.FLEX_START)
        binding.vendorPinCodesRv.addItemDecoration(SimpleItemDecorator(10, 10))
        binding.vendorPinCodesRv.layoutManager = layoutManagerServices
        binding.vendorPinCodesRv.adapter = vendorPinCodesAdapter
    }

    private fun getDummyAttendanceList(): MutableList<VendorAttendanceModel.VendorAttendanceModelItem> {
        val list: MutableList<VendorAttendanceModel.VendorAttendanceModelItem> = arrayListOf()

        list.add(
            VendorAttendanceModel.VendorAttendanceModelItem(
                date = "2022-09-04T06:40:32",
                present = true
            )

        )

        list.add(
            VendorAttendanceModel.VendorAttendanceModelItem(
                date = "2022-09-05T06:40:32",
                present = false
            )

        )

        list.add(
            VendorAttendanceModel.VendorAttendanceModelItem(
                date = "2022-09-06T06:40:32",
                present = false
            )

        )

        list.add(
            VendorAttendanceModel.VendorAttendanceModelItem(
                date = "2022-09-07T06:40:32",
                present = false
            )

        )


        list.add(
            VendorAttendanceModel.VendorAttendanceModelItem(
                date = "2022-09-08T06:40:32",
                present = true
            )

        )


        list.add(
            VendorAttendanceModel.VendorAttendanceModelItem(
                date = "2022-09-09T06:40:32",
                present = true
            )

        )

        list.add(
            VendorAttendanceModel.VendorAttendanceModelItem(
                date = "2022-09-10T06:40:32",
                present = true
            )

        )

        return list
    }

    private fun initServicesRv() {
        vendorServicesAdapter =
            VendorServicesAdapter()

        val layoutManagerServices = FlexboxLayoutManager(requireContext())
        layoutManagerServices.flexDirection = FlexDirection.ROW
        layoutManagerServices.justifyContent = (JustifyContent.FLEX_START)
        binding.vendorServicesLv.addItemDecoration(SimpleItemDecorator(10, 10))
        binding.vendorServicesLv.layoutManager = layoutManagerServices
        binding.vendorServicesLv.adapter = vendorServicesAdapter
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