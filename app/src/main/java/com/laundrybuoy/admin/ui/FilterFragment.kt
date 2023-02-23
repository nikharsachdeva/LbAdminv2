package com.laundrybuoy.admin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.filter.FilterPlatformAdapter
import com.laundrybuoy.admin.adapter.filter.FilterServiceAdapter
import com.laundrybuoy.admin.adapter.filter.FilterStatusAdapter
import com.laundrybuoy.admin.databinding.FragmentFilterBinding
import com.laundrybuoy.admin.model.vendor.VendorServices
import com.laundrybuoy.admin.utils.SimpleItemDecorator
import com.laundrybuoy.admin.viewmodel.FilterViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class FilterFragment : BaseBottomSheet() {


    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val filterViewModel by viewModels<FilterViewModel>()

    var filterPayload = JSONObject()
    private lateinit var filterStatusAdapter: FilterStatusAdapter
    private lateinit var filterServiceAdapter: FilterServiceAdapter
    private lateinit var filterPlatformAdapter: FilterPlatformAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behaviour = BottomSheetBehavior.from(bottomSheet)
                setupFullHeight(bottomSheet)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        arguments?.getString("FILTER_PAYLOAD_AS_STRING")
            ?.let {
                init(it)
            }

    }

    private fun init(payloadAsString: String) {


        filterPayload = JSONObject(payloadAsString)
        binding.filterStartDateEt.setText(filterPayload.opt("startDate")?.toString() ?: "")
        binding.filterEndDateEt.setText(filterPayload.opt("endDate")?.toString() ?: "")


        binding.closeFilterIv.setOnClickListener {
            dialog?.dismiss()
        }


        binding.applyFilterBtn.setOnClickListener {
        }


        filterServiceAdapter = FilterServiceAdapter(
            requireContext(), mutableListOf()
        )
        binding.servicesChooseFilterRv.adapter = filterServiceAdapter
        val layoutManagerServices = FlexboxLayoutManager(requireContext())
        layoutManagerServices.flexDirection = FlexDirection.ROW
        layoutManagerServices.justifyContent = (JustifyContent.FLEX_START)
        binding.servicesChooseFilterRv.addItemDecoration(SimpleItemDecorator(10, 10))
        binding.servicesChooseFilterRv.layoutManager = layoutManagerServices



        filterStatusAdapter = FilterStatusAdapter(
            requireContext(), mutableListOf()
        )
        binding.orderStatusFilterRv.adapter = filterStatusAdapter
        val layoutManagerStatus = FlexboxLayoutManager(requireContext())
        layoutManagerStatus.flexDirection = FlexDirection.ROW
        layoutManagerStatus.justifyContent = (JustifyContent.FLEX_START)
        binding.orderStatusFilterRv.addItemDecoration(SimpleItemDecorator(10, 10))
        binding.orderStatusFilterRv.layoutManager = layoutManagerStatus


        filterPlatformAdapter = FilterPlatformAdapter(
            requireContext(), mutableListOf()
        )
        binding.orderPlatformFilterRv.adapter = filterPlatformAdapter
        val layoutManagerPlatform = FlexboxLayoutManager(requireContext())
        layoutManagerPlatform.flexDirection = FlexDirection.ROW
        layoutManagerPlatform.justifyContent = (JustifyContent.FLEX_START)
        binding.orderPlatformFilterRv.addItemDecoration(SimpleItemDecorator(10, 10))
        binding.orderPlatformFilterRv.layoutManager = layoutManagerPlatform

        setDataToServiceFilter(getDummyServiceList())
        setDataToStatusFilter(getDummyStatusList())
        setDataToPlatformFilter(getDummyPlatformList())

    }

    private fun setDataToPlatformFilter(dummyPlatformList: ArrayList<VendorServices.VendorServicesItem>) {
        filterPlatformAdapter.setEmployees(dummyPlatformList)

    }

    private fun setDataToServiceFilter(serviceList: ArrayList<VendorServices.VendorServicesItem>) {

        filterServiceAdapter.setEmployees(serviceList)

    }


    private fun setDataToStatusFilter(dummyStatusList: ArrayList<VendorServices.VendorServicesItem>) {


        filterStatusAdapter.setEmployees(dummyStatusList)

    }

    private fun getDummyServiceList(): ArrayList<VendorServices.VendorServicesItem> {
        val list: ArrayList<VendorServices.VendorServicesItem> = arrayListOf()
        list.add(
            VendorServices.VendorServicesItem(
                id = "323",
                serviceName = "Laundry"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "3sds23",
                serviceName = "Dry Clean"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "fdsf",
                serviceName = "Wash & Iron"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "fdsfff",
                serviceName = "Shoe Cleaning"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "5675",
                serviceName = "House Cleaning"
            )
        )


        return list
    }

    private fun getDummyPlatformList(): ArrayList<VendorServices.VendorServicesItem> {
        val list: ArrayList<VendorServices.VendorServicesItem> = arrayListOf()
        list.add(
            VendorServices.VendorServicesItem(
                id = "32333",
                serviceName = "Android"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "633547",
                serviceName = "iOS"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "1234",
                serviceName = "Whatsapp"
            )
        )


        list.add(
            VendorServices.VendorServicesItem(
                id = "5678",
                serviceName = "Admin"
            )
        )


        return list
    }

    private fun getDummyStatusList(): ArrayList<VendorServices.VendorServicesItem> {
        val list: ArrayList<VendorServices.VendorServicesItem> = arrayListOf()
        list.add(
            VendorServices.VendorServicesItem(
                id = "32333",
                serviceName = "Un-Accepted"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "633547",
                serviceName = "Cancelled"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "1234",
                serviceName = "Pending Pickup"
            )
        )


        list.add(
            VendorServices.VendorServicesItem(
                id = "5678",
                serviceName = "Processing"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "9101112",
                serviceName = "Pending Delivery"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "jfj433534",
                serviceName = "Delivered"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "nbvv33435",
                serviceName = "Express Orders"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "nf433543",
                serviceName = "Requires Attention"
            )
        )

        list.add(
            VendorServices.VendorServicesItem(
                id = "nb43354",
                serviceName = "Problematic Orders"
            )
        )



        return list
    }

    private fun setupFullHeight(bottomSheet: View) {

        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }


    internal companion object {
        fun newInstance(filterPayload: JsonObject): FilterFragment {
            val frg = FilterFragment()
            val bundle = Bundle()
            bundle.putString("FILTER_PAYLOAD_AS_STRING", filterPayload.toString())
            frg.arguments = bundle
            return frg
        }
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