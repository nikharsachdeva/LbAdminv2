package com.laundrybuoy.admin.ui.vendor

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentVendorRevenueBinding
import com.laundrybuoy.admin.model.vendor.VendorOrderGraphModel
import com.laundrybuoy.admin.model.vendor.VendorRevenueModel
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.chart.ChartMarkerView
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class VendorRevenueFragment : BaseFragment(), OnChartValueSelectedListener {

    private var _binding: FragmentVendorRevenueBinding? = null
    private val binding get() = _binding!!
    private var revenueLineChart: LineChart? = null
    private val vendorViewModel by viewModels<VendorViewModel>()
    private var partnerId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        private const val VENDOR_ID = "VENDOR_ID"

        fun newInstance(
            vendorId: String,
        ): VendorRevenueFragment {
            val viewFragment = VendorRevenueFragment()
            val args = Bundle()
            args.putString(VENDOR_ID, vendorId)
            viewFragment.arguments = args
            return viewFragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        init()
        onClicks()
    }

    private fun onClicks() {

        binding.yearPickerRl.setOnClickListener {
            val yearFrag = YearPickerFragment()
            yearFrag.setCallback { year ->
                vendorViewModel.setChartYear(year)
            }
            yearFrag.isCancelable = true
            yearFrag.show(childFragmentManager, "year_fragment")
        }


    }

    private fun init() {
        partnerId = arguments?.getString(VENDOR_ID)
        val year = Calendar.getInstance().get(Calendar.YEAR);
        vendorViewModel.setChartYear(year.toString())

        initRevenueCharts()
    }

    private fun initObserver() {
        vendorViewModel._chartYearLiveData.observe(viewLifecycleOwner, Observer { year ->

            binding.chartYearTv.text = "${year}"
            fetchGraphData(partnerId, year)

        })

        vendorViewModel.vendorOrderGraphLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true && it.data.data.isNullOrEmpty() == false) {
                        showChartData()
                        computeChartData(it.data.data)
                    } else {
                        hideChartData()
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

    }

    private fun computeChartData(monthsListFromBackend: List<VendorOrderGraphModel.Data?>) {
        val emptyMonthList = getMainActivity()?.getAllMonthForGraphPartner()
        monthsListFromBackend.forEachIndexed { indexOfBackend, backendData ->
            emptyMonthList?.forEachIndexed { indexOfDummy, dummyData ->
                if (backendData?.month == dummyData.month) {
                    dummyData.orders = backendData?.orders
                    dummyData.revenue = backendData?.revenue
                }
            }
        }

        setDataToRevenueCharts(emptyMonthList)
    }

    private fun showChartData() {
        binding.revenueChartLegend.makeViewVisible()
        binding.revenueLineChartLc.makeViewVisible()
        binding.dataUnAvailableLl.makeViewGone()
    }

    private fun hideChartData() {
        binding.revenueChartLegend.makeViewGone()
        binding.revenueLineChartLc.makeViewGone()
        binding.dataUnAvailableLl.makeViewVisible()
    }

    private fun fetchGraphData(partnerIdReceived: String?, year: String) {
        val partnerPayload = JsonObject()
        partnerPayload.addProperty("partnerId", partnerIdReceived)
        partnerPayload.addProperty("year", year)
        vendorViewModel.getVendorOrderGraph(partnerPayload)
    }

    private fun getDummyLineChartData(): VendorRevenueModel {


        val revenueList: MutableList<VendorRevenueModel.Data.Revenue> = arrayListOf()

        revenueList.add(
            VendorRevenueModel.Data.Revenue(
                xAxis = "Jan",
                yAxis = "4800"
            )
        )

        revenueList.add(
            VendorRevenueModel.Data.Revenue(
                xAxis = "Feb",
                yAxis = "3000"
            )
        )

        revenueList.add(
            VendorRevenueModel.Data.Revenue(
                xAxis = "Mar",
                yAxis = "2500"
            )
        )

        revenueList.add(
            VendorRevenueModel.Data.Revenue(
                xAxis = "Apr",
                yAxis = "1120"
            )
        )

        revenueList.add(
            VendorRevenueModel.Data.Revenue(
                xAxis = "May",
                yAxis = "2330"
            )
        )

        revenueList.add(
            VendorRevenueModel.Data.Revenue(
                xAxis = "Jun",
                yAxis = "5000"
            )
        )

        revenueList.add(
            VendorRevenueModel.Data.Revenue(
                xAxis = "Jul",
                yAxis = "4500"
            )
        )

        revenueList.add(
            VendorRevenueModel.Data.Revenue(
                xAxis = "Aug",
                yAxis = "2460"
            )
        )

        revenueList.add(
            VendorRevenueModel.Data.Revenue(
                xAxis = "Sep",
                yAxis = "3500"
            )
        )

        revenueList.add(
            VendorRevenueModel.Data.Revenue(
                xAxis = "Oct",
                yAxis = "8000"
            )
        )

        revenueList.add(
            VendorRevenueModel.Data.Revenue(
                xAxis = "Nov",
                yAxis = "3800"
            )
        )

        revenueList.add(
            VendorRevenueModel.Data.Revenue(
                xAxis = "Dec",
                yAxis = "2670"
            )
        )

        val incentiveList: MutableList<VendorRevenueModel.Data.Incentive> = arrayListOf()

        incentiveList.add(
            VendorRevenueModel.Data.Incentive(
                xAxis = "Jan",
                yAxis = "2500"
            )
        )

        incentiveList.add(
            VendorRevenueModel.Data.Incentive(
                xAxis = "Feb",
                yAxis = "1300"
            )
        )

        incentiveList.add(
            VendorRevenueModel.Data.Incentive(
                xAxis = "Mar",
                yAxis = "1200"
            )
        )

        incentiveList.add(
            VendorRevenueModel.Data.Incentive(
                xAxis = "Apr",
                yAxis = "110"
            )
        )

        incentiveList.add(
            VendorRevenueModel.Data.Incentive(
                xAxis = "May",
                yAxis = "230"
            )
        )

        incentiveList.add(
            VendorRevenueModel.Data.Incentive(
                xAxis = "Jun",
                yAxis = "2500"
            )
        )

        incentiveList.add(
            VendorRevenueModel.Data.Incentive(
                xAxis = "Jul",
                yAxis = "450"
            )
        )

        incentiveList.add(
            VendorRevenueModel.Data.Incentive(
                xAxis = "Aug",
                yAxis = "240"
            )
        )

        incentiveList.add(
            VendorRevenueModel.Data.Incentive(
                xAxis = "Sep",
                yAxis = "350"
            )
        )

        incentiveList.add(
            VendorRevenueModel.Data.Incentive(
                xAxis = "Oct",
                yAxis = "2800"
            )
        )

        incentiveList.add(
            VendorRevenueModel.Data.Incentive(
                xAxis = "Nov",
                yAxis = "380"
            )
        )

        incentiveList.add(
            VendorRevenueModel.Data.Incentive(
                xAxis = "Dec",
                yAxis = "260"
            )
        )

        return VendorRevenueModel(
            success = "true",
            message = "data is available",
            data = VendorRevenueModel.Data(
                incentive = incentiveList,
                revenue = revenueList
            )
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentVendorRevenueBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRevenueCharts() {

        revenueLineChart = binding.revenueLineChartLc

        val xAxis: XAxis = revenueLineChart?.xAxis!!
        xAxis.setDrawAxisLine(false) // to remove bottom x axis line
        xAxis.textColor = Color.parseColor("#31394C")

        //to hide right Y and top X border
        val rightYAxis = revenueLineChart?.axisRight
        rightYAxis?.isEnabled = false
        val leftYAxis = revenueLineChart?.axisLeft
        leftYAxis?.isEnabled = true
        val topXAxis = revenueLineChart?.xAxis
        topXAxis?.isEnabled = false

        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(false)
        xAxis.isEnabled = true
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // set listeners
        revenueLineChart!!.setOnChartValueSelectedListener(this)

        revenueLineChart!!.xAxis.textSize = 12f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            revenueLineChart!!.xAxis.typeface = resources.getFont(R.font.lexend_light)
        }
//        revenueLineChart!!.setExtraOffsets(20f, 10f, 10f, 20f)

        //customization
        revenueLineChart?.setTouchEnabled(true)
        revenueLineChart?.isDragEnabled = true
        revenueLineChart?.setScaleEnabled(false)
        revenueLineChart?.setPinchZoom(false)
        revenueLineChart?.setDrawGridBackground(false)

        //to hide background lines
        revenueLineChart?.xAxis?.setDrawGridLines(false)
        revenueLineChart?.axisLeft?.setDrawGridLines(false)
        revenueLineChart?.axisRight?.setDrawGridLines(false)

        // create marker to display box when values are selected
        val mv = ChartMarkerView(
            requireContext(),
            R.layout.custom_marker_revenue
        )

        // Set the marker to the chart
        mv.chartView = revenueLineChart
        revenueLineChart?.marker = mv

        revenueLineChart?.animateXY(1000, 500)
        revenueLineChart?.legend?.isEnabled = false
        revenueLineChart?.description?.isEnabled = false
        revenueLineChart?.extraBottomOffset = 10f

    }

    private fun setDataToRevenueCharts(lineChartData: MutableList<VendorOrderGraphModel.Data>?) {

//        val revenueList = dummyLineChartData.data?.revenue
        /*
        val incentiveList = dummyLineChartData.data?.incentive
         */

        val xAxisLabels = lineChartData?.map {
            it.month?.substring(0..2)
        }
        revenueLineChart?.xAxis?.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        revenueLineChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        revenueLineChart?.xAxis?.labelCount = (xAxisLabels?.size!!)

        val valuesSet1 = ArrayList<Entry>()
        /*
        val valuesSet2 = ArrayList<Entry>()
         */
        val dataSets = ArrayList<ILineDataSet>()

        for (i in 0 until lineChartData?.size!!) {
            val value = lineChartData[i]?.revenue?.toFloat()
            valuesSet1.add(Entry((i).toFloat(), value!!))
        }

        /*
        for (i in 0 until incentiveList?.size!!) {
            val value = incentiveList[i]?.yAxis?.toFloat()
            valuesSet2.add(Entry((i).toFloat(), value!!))
        }
         */

        // create a dataset and give it a type
        var set1: LineDataSet = LineDataSet(valuesSet1, "Revenue Dataset")
//        set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set1.lineWidth = 1.75f
        set1.circleRadius = 2.5f
        set1.circleHoleRadius = 2.5f
        set1.color = resources.getColor(R.color.purple80)
        set1.setCircleColor(resources.getColor(R.color.hintColor))
        set1.circleHoleColor = resources.getColor(R.color.hintColor)
        set1.highLightColor = resources.getColor(R.color.hintColor)
        set1.valueTextColor = Color.WHITE
        set1.valueTextSize = 12f
        set1.setDrawValues(false)
        set1.setDrawHighlightIndicators(false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            set1.valueTypeface = resources.getFont(R.font.lexend_light)
        }
        set1.setDrawFilled(true)
        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.purple_gradient_steps_chart
            )
            set1.fillDrawable = drawable
        } else {
            set1.fillColor = resources.getColor(R.color.purple20)
        }
        /*
        var set2: LineDataSet = LineDataSet(valuesSet2, "Incentive Dataset")
//        set2.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set2.lineWidth = 1.75f
        set2.circleRadius = 2.5f
        set2.circleHoleRadius = 2.5f
        set2.color = resources.getColor(R.color.blue100)
        set2.setCircleColor(resources.getColor(R.color.hintColor))
        set2.circleHoleColor = resources.getColor(R.color.hintColor)
        set2.highLightColor = resources.getColor(R.color.hintColor)
        set2.valueTextColor = Color.WHITE
        set2.valueTextSize = 12f
        set2.setDrawValues(false)
        set2.setDrawHighlightIndicators(false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            set2.valueTypeface = resources.getFont(R.font.lexend_light)
        }
        set2.setDrawFilled(true)
        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.blue_gradient_steps_chart
            )
            set2.fillDrawable = drawable
        } else {
            set2.fillColor = resources.getColor(R.color.blue20)
        }

        dataSets.add(set2)
         */

        dataSets.add(set1)

        //String setter in x-Axis
        val data = LineData(dataSets)
        revenueLineChart?.data = data
        revenueLineChart?.invalidate()
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

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {
    }

}