package com.laundrybuoy.admin.ui.home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentChartOrderRevenueBinding
import com.laundrybuoy.admin.model.home.AdminGraphResponse
import com.laundrybuoy.admin.utils.chart.ChartMarkerView
import com.laundrybuoy.admin.viewmodel.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChartOrderRevenueFragment : BaseFragment(), OnChartValueSelectedListener {

    private var _binding: FragmentChartOrderRevenueBinding? = null
    private val binding get() = _binding!!
    lateinit var homeSharedVM: HomePageViewModel
    private var revenueLineChart: LineChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initRevenueCharts()
        initObserver()
    }

    private fun initObserver() {
        homeSharedVM.computedData.observe(viewLifecycleOwner, Observer {
            setDataToRevenueChart(it)
        })
    }

    private fun setDataToRevenueChart(lineChartData: MutableList<AdminGraphResponse.Data>?) {

        var totalRevenue = 0.0
        lineChartData?.forEach {
            totalRevenue += it.orderRevenue ?: 0.0
        }
        binding.totalRevenueTv.text="₹${totalRevenue}"

        val xAxisLabels = lineChartData?.map {
            it.month?.substring(0..2)
        }
        revenueLineChart?.xAxis?.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        revenueLineChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        revenueLineChart?.xAxis?.labelCount = (xAxisLabels?.size!!)

        val valuesSet1 = ArrayList<Entry>()
        val dataSets = ArrayList<ILineDataSet>()

        for (i in 0 until lineChartData?.size!!) {
            val value = lineChartData[i]?.orderRevenue?.toFloat()
            valuesSet1.add(Entry((i).toFloat(), value!!))
        }

        // create a dataset and give it a type
        var set1: LineDataSet = LineDataSet(valuesSet1, "Revenue Dataset")
        set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set1.lineWidth = 1.75f
        set1.circleRadius = 2.5f
        set1.circleHoleRadius = 2.5f
        set1.color = resources.getColor(R.color.colorYellowChart)
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
                R.drawable.yellow_gradient_steps_chart
            )
            set1.fillDrawable = drawable
        } else {
            set1.fillColor = resources.getColor(R.color.colorYellowChart)
        }
        dataSets.add(set1)

        //String setter in x-Axis
        val data = LineData(dataSets)
        revenueLineChart?.data = data
        revenueLineChart?.invalidate()
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

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {
    }

    private fun init() {
        homeSharedVM = ViewModelProvider(requireActivity()).get(HomePageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChartOrderRevenueBinding.inflate(inflater, container, false)
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