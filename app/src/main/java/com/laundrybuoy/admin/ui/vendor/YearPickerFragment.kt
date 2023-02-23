package com.laundrybuoy.admin.ui.vendor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.order.PhoneTagsAdapter
import com.laundrybuoy.admin.adapter.vendor.YearSelectorAdapter
import com.laundrybuoy.admin.databinding.FragmentVendorChartBinding
import com.laundrybuoy.admin.databinding.FragmentYearPickerBinding
import com.laundrybuoy.admin.model.order.OrderTagsModel
import com.laundrybuoy.admin.utils.getVendorPhoneNumbers

class YearPickerFragment : BaseBottomSheet() {

    private var _binding: FragmentYearPickerBinding? = null
    private val binding get() = _binding!!
    private lateinit var yearSelectorAdapter: YearSelectorAdapter
    private lateinit var callback: (String) -> Unit
    fun setCallback(callback: (String) -> Unit) {
        this.callback = callback
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        setYearsData()
    }

    private fun setYearsData() {
        yearSelectorAdapter.submitList(getYearList())

    }

    private fun getYearList(): MutableList<OrderTagsModel.OrderTagsItem>? {
        val listOfYear: MutableList<OrderTagsModel.OrderTagsItem> = arrayListOf()
        listOfYear.add(OrderTagsModel.OrderTagsItem(
            tagName = "2022",
            tagHex = null
        ))

        listOfYear.add(OrderTagsModel.OrderTagsItem(
            tagName = "2023",
            tagHex = null
        ))

        listOfYear.add(OrderTagsModel.OrderTagsItem(
            tagName = "2024",
            tagHex = null
        ))
        listOfYear.add(OrderTagsModel.OrderTagsItem(
            tagName = "2025",
            tagHex = null
        ))
        listOfYear.add(OrderTagsModel.OrderTagsItem(
            tagName = "2026",
            tagHex = null
        ))
        listOfYear.add(OrderTagsModel.OrderTagsItem(
            tagName = "2027",
            tagHex = null
        ))
        listOfYear.add(OrderTagsModel.OrderTagsItem(
            tagName = "2028",
            tagHex = null
        ))
        listOfYear.add(OrderTagsModel.OrderTagsItem(
            tagName = "2029",
            tagHex = null
        ))
        listOfYear.add(OrderTagsModel.OrderTagsItem(
            tagName = "2030",
            tagHex = null
        ))

        return listOfYear
    }

    private fun initRv() {
        yearSelectorAdapter = YearSelectorAdapter(object : YearSelectorAdapter.YearClickListener {
            override fun onYearClick(year: String) {
                if (::callback.isInitialized) {
                    dialog?.dismiss()
                    callback.invoke(year)
                }
            }


        })
        binding.yearsRv.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.yearsRv.adapter = yearSelectorAdapter

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentYearPickerBinding.inflate(inflater, container, false)
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