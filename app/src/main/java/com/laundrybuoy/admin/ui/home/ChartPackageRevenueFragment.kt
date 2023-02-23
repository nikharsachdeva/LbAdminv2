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
import com.laundrybuoy.admin.databinding.FragmentChartPackageRevenueBinding
import com.laundrybuoy.admin.model.home.AdminGraphResponse
import com.laundrybuoy.admin.utils.chart.ChartMarkerView
import com.laundrybuoy.admin.viewmodel.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChartPackageRevenueFragment : BaseFragment(), OnChartValueSelectedListener {

    private var _binding: FragmentChartPackageRevenueBinding? = null
    private val binding get() = _binding!!
    lateinit var homeSharedVM: HomePageViewModel
    private var packageLineChart: LineChart? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        init()
        initRevenueCharts()
        initObserver()
    }

    private fun initRevenueCharts() {

        packageLineChart = binding.packageLineChartLc

        val xAxis: XAxis = packageLineChart?.xAxis!!
        xAxis.setDrawAxisLine(false) // to remove bottom x axis line
        xAxis.textColor = Color.parseColor("#31394C")

        //to hide right Y and top X border
        val rightYAxis = packageLineChart?.axisRight
        rightYAxis?.isEnabled = false
        val leftYAxis = packageLineChart?.axisLeft
        leftYAxis?.isEnabled = true
        val topXAxis = packageLineChart?.xAxis
        topXAxis?.isEnabled = false

        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(false)
        xAxis.isEnabled = true
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // set listeners
        packageLineChart!!.setOnChartValueSelectedListener(this)

        packageLineChart!!.xAxis.textSize = 12f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            packageLineChart!!.xAxis.typeface = resources.getFont(R.font.lexend_light)
        }
//        packageLineChart!!.setExtraOffsets(20f, 10f, 10f, 20f)

        //customization
        packageLineChart?.setTouchEnabled(true)
        packageLineChart?.isDragEnabled = true
        packageLineChart?.setScaleEnabled(false)
        packageLineChart?.setPinchZoom(false)
        packageLineChart?.setDrawGridBackground(false)

        //to hide background lines
        packageLineChart?.xAxis?.setDrawGridLines(false)
        packageLineChart?.axisLeft?.setDrawGridLines(false)
        packageLineChart?.axisRight?.setDrawGridLines(false)

        // create marker to display box when values are selected
        val mv = ChartMarkerView(
            requireContext(),
            R.layout.custom_marker_revenue
        )

        // Set the marker to the chart
        mv.chartView = packageLineChart
        packageLineChart?.marker = mv

        packageLineChart?.animateXY(1000, 500)
        packageLineChart?.legend?.isEnabled = false
        packageLineChart?.description?.isEnabled = false
        packageLineChart?.extraBottomOffset = 10f

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {
    }

    private fun setDataToPackageChart(lineChartData: MutableList<AdminGraphResponse.Data>?) {

        var totalRevenue = 0.0
        lineChartData?.forEach {
            totalRevenue += it.packageRevenue ?: 0.0
        }
        binding.totalRevenueTv.text="₹${totalRevenue}"

        val xAxisLabels = lineChartData?.map {
            it.month?.substring(0..2)
        }
        packageLineChart?.xAxis?.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        packageLineChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        packageLineChart?.xAxis?.labelCount = (xAxisLabels?.size!!)

        val valuesSet1 = ArrayList<Entry>()
        val dataSets = ArrayList<ILineDataSet>()

        for (i in 0 until lineChartData?.size!!) {
            val value = lineChartData[i]?.packageRevenue?.toFloat()
            valuesSet1.add(Entry((i).toFloat(), value!!))
        }

        // create a dataset and give it a type
        var set1: LineDataSet = LineDataSet(valuesSet1, "Package Dataset")
        set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set1.lineWidth = 1.75f
        set1.circleRadius = 2.5f
        set1.circleHoleRadius = 2.5f
        set1.color = resources.getColor(R.color.colorBlueChart)
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
                R.drawable.bluee_gradient_steps_chart
            )
            set1.fillDrawable = drawable
        } else {
            set1.fillColor = resources.getColor(R.color.colorBlueChart)
        }
        dataSets.add(set1)

        //String setter in x-Axis
        val data = LineData(dataSets)
        packageLineChart?.data = data
        packageLineChart?.invalidate()
    }


    private fun initObserver() {
        homeSharedVM.computedData.observe(viewLifecycleOwner, Observer {
            setDataToPackageChart(it)
        })
    }


    private fun init() {
        homeSharedVM = ViewModelProvider(requireActivity()).get(HomePageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChartPackageRevenueBinding.inflate(inflater, container, false)
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