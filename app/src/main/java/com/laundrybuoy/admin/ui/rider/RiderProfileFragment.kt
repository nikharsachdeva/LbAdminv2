package com.laundrybuoy.admin.ui.rider

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.rider.RiderAttendanceAdapter
import com.laundrybuoy.admin.adapter.rider.RiderFilterAdapter
import com.laundrybuoy.admin.adapter.vendor.VendorFilterAdapter
import com.laundrybuoy.admin.databinding.FragmentRiderProfileBinding
import com.laundrybuoy.admin.model.rider.RiderAttendanceModel
import com.laundrybuoy.admin.model.rider.RiderHomeFigures
import com.laundrybuoy.admin.model.rider.RiderProfileModel
import com.laundrybuoy.admin.model.vendor.VendorFiguresModel
import com.laundrybuoy.admin.ui.order.OrderListingFragment
import com.laundrybuoy.admin.ui.rider.attendance.AttendanceRootFragment
import com.laundrybuoy.admin.ui.vendor.YearPickerFragment
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.SimpleItemDecorator
import com.laundrybuoy.admin.utils.loadImageWithGlide
import com.laundrybuoy.admin.viewmodel.RiderViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RiderProfileFragment : BaseFragment() {

    private var _binding: FragmentRiderProfileBinding? = null
    private val binding get() = _binding!!
    lateinit var riderViewModel: RiderViewModel
    var riderIdReceived: String? = null
    private lateinit var riderAttendanceAdapter: RiderAttendanceAdapter
    private lateinit var riderFilterAdapter: RiderFilterAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        onClick()
        initObserver()

        fetchRiderProfile()

        initRiderAttendance()
        fetchRiderAttendance(getMainActivity()?.getCurrentMonth(),
            getMainActivity()?.getCurrentYear())

        initFiltersRv()
        val year = Calendar.getInstance().get(Calendar.YEAR);
        riderViewModel.setHomeFiguresDuration(year.toString())

    }

    private fun initFiltersRv() {
        riderFilterAdapter =
            RiderFilterAdapter(object : RiderFilterAdapter.OnClickInterface {
                override fun onFilterClicked(filter: RiderHomeFigures.Data.Response) {
                    goToListingPage(filter)

                }

            })
        binding.riderFiguresRv.addItemDecoration(SimpleItemDecorator(15, 15))

        val layoutManagerFilter =
            GridLayoutManager(requireContext(), 3)
        binding.riderFiguresRv.layoutManager = layoutManagerFilter
        binding.riderFiguresRv.adapter = riderFilterAdapter
    }

    private fun goToListingPage(filter: RiderHomeFigures.Data.Response) {

        val metaData = JsonObject()
        metaData.addProperty("screenType", "riderProfile")
        metaData.addProperty("riderId", riderIdReceived)
        metaData.addProperty("from", "${riderViewModel._homeFigYearLiveData.value}-01-01T00:00:00Z")
        metaData.addProperty("to", "${riderViewModel._homeFigYearLiveData.value}-12-31T23:59:59Z")
        metaData.addProperty("url", filter.baseUrl)

        val listingFrag = OrderListingFragment.newInstance(metaData.toString())
        getMainActivity()?.addFragment(true, getMainActivity()?.getVisibleFrame()!!, listingFrag)
    }

    private fun onClick() {
        binding.attendanceDatePickerRl.setOnClickListener {
            val frag = RiderAttendanceFragment.newInstance(riderIdReceived ?: "")
            getMainActivity()?.addFragment(true, getMainActivity()?.getVisibleFrame()!!, frag)
        }

        binding.yearPickerRl.setOnClickListener {
            val yearFrag = YearPickerFragment()
            yearFrag.setCallback { year ->
                riderViewModel.setHomeFiguresDuration(year)
            }
            yearFrag.isCancelable = true
            yearFrag.show(childFragmentManager, "year_fragment")
        }

    }

    private fun initRiderAttendance() {
        riderAttendanceAdapter =
            RiderAttendanceAdapter()

        val layoutManagerAttendance =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.riderAttendanceRv.layoutManager = layoutManagerAttendance
        binding.riderAttendanceRv.adapter = riderAttendanceAdapter
    }

    private fun fetchRiderAttendance(currentMonth: Int?, currentYear: Int?) {
        binding.attendanceMonthTv.text =
            "${getMainActivity()?.getMonthString(currentMonth!!.minus(1))} $currentYear"
        val riderIdPayload = JsonObject()
        riderIdPayload.addProperty("month", currentMonth)
        riderIdPayload.addProperty("year", currentYear)
        riderIdPayload.addProperty("riderId", riderIdReceived)
        riderViewModel.getRiderAttendance(riderIdPayload)
    }

    private fun initObserver() {
        riderViewModel.riderAttendanceLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Success -> {
                        if (it.data?.success == true && it.data?.data != null) {
                            setAttendanceRvData(it.data?.data)
                        }
                    }
                    is NetworkResult.Error -> {
                        getMainActivity()?.showSnackBar(it.message)
                    }
                }
            })

        riderViewModel.riderProfileLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Success -> {
                        if (it.data?.success == true && it.data?.data != null) {
                            setRiderProfileUI(it.data?.data)
                        }
                    }
                    is NetworkResult.Error -> {
                        getMainActivity()?.showSnackBar(it.message)
                    }
                }
            })

        riderViewModel.riderHomeFigLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Success -> {
                        if (it.data?.success == true && !it.data?.data?.response.isNullOrEmpty()) {
                            setRiderHomeFigures(it.data.data?.response!!)
                        }
                    }
                    is NetworkResult.Error -> {
                        getMainActivity()?.showSnackBar(it.message)
                    }
                }
            })

        riderViewModel._homeFigYearLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { year ->

                binding.chartYearTv.text = "${year}"

                val vendorIdPayload = JsonObject()
                vendorIdPayload.addProperty("from", "${year}-01-01T00:00:00Z")
                vendorIdPayload.addProperty("to", "${year}-12-31T23:59:59Z")
                vendorIdPayload.addProperty("riderId", riderIdReceived)

                fetchRiderFigures(vendorIdPayload)
            })

    }

    private fun fetchRiderFigures(figDurationPayload: JsonObject) {
        riderViewModel.getRiderHomeFigures(figDurationPayload)
    }

    private fun setRiderHomeFigures(response: List<RiderHomeFigures.Data.Response>) {
        riderFilterAdapter.submitList(response)
    }

    private fun setAttendanceRvData(data: List<RiderAttendanceModel.Data>) {
        riderAttendanceAdapter.submitList(data)
    }

    private fun setRiderProfileUI(riderData: RiderProfileModel.Data) {

        binding.vendorNameTv.text = riderData.name ?: ""
        binding.vendorProfilePicIv.loadImageWithGlide(riderData.profile ?: "")
        when (riderData.isActive) {
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
        when (riderData.isBlocked) {
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
        binding.vendorPrimaryNoTv.text = riderData?.mobile ?: ""
        binding.vendorSecNoTv.text = riderData?.altMobile ?: ""

        val addressString = riderData.address?.line1 + " " +
                riderData.address?.landmark + " " +
                riderData.address?.city + " " +
                riderData.address?.pin
        binding.vendorWorkAddressTv.text = addressString ?: ""


    }

    private fun fetchRiderProfile() {
        val riderIdPayload = JsonObject()
        riderIdPayload.addProperty("role", "rider")
        riderIdPayload.addProperty("userId", riderIdReceived)
        riderViewModel.getRiderProfile(riderIdPayload)
    }

    private fun init() {
        riderIdReceived = arguments?.getString(RIDER_ID)
        riderViewModel = ViewModelProvider(requireActivity()).get(RiderViewModel::class.java)
    }

    companion object {
        private const val RIDER_ID = "RIDER_ID"

        fun newInstance(
            riderId: String,
        ): RiderProfileFragment {
            val viewFragment = RiderProfileFragment()
            val args = Bundle()
            args.putString(RIDER_ID, riderId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRiderProfileBinding.inflate(inflater, container, false)
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