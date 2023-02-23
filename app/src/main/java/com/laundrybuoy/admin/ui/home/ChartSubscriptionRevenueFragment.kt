package com.laundrybuoy.admin.ui.home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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
import com.laundrybuoy.admin.databinding.FragmentChartSubscriptionRevenueBinding
import com.laundrybuoy.admin.databinding.FragmentChartTotalOrderBinding
import com.laundrybuoy.admin.model.home.AdminGraphResponse
import com.laundrybuoy.admin.utils.chart.ChartMarkerView
import com.laundrybuoy.admin.viewmodel.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChartSubscriptionRevenueFragment : BaseFragment(), OnChartValueSelectedListener {

    private var _binding: FragmentChartSubscriptionRevenueBinding? = null
    private val binding get() = _binding!!
    lateinit var homeSharedVM: HomePageViewModel
    private var subscriptionLineChart: LineChart? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initSubscriptionsCharts()
        initObserver()

    }

    private fun initSubscriptionsCharts() {
        subscriptionLineChart = binding.subscriptionLineChartLc

        val xAxis: XAxis = subscriptionLineChart?.xAxis!!
        xAxis.setDrawAxisLine(false) // to remove bottom x axis line
        xAxis.textColor = Color.parseColor("#31394C")

        //to hide right Y and top X border
        val rightYAxis = subscriptionLineChart?.axisRight
        rightYAxis?.isEnabled = false
        val leftYAxis = subscriptionLineChart?.axisLeft
        leftYAxis?.isEnabled = true
        val topXAxis = subscriptionLineChart?.xAxis
        topXAxis?.isEnabled = false

        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(false)
        xAxis.isEnabled = true
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // set listeners
        subscriptionLineChart!!.setOnChartValueSelectedListener(this)

        subscriptionLineChart!!.xAxis.textSize = 12f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            subscriptionLineChart!!.xAxis.typeface = resources.getFont(R.font.lexend_light)
        }
//        subscriptionLineChart!!.setExtraOffsets(20f, 10f, 10f, 20f)

        //customization
        subscriptionLineChart?.setTouchEnabled(true)
        subscriptionLineChart?.isDragEnabled = true
        subscriptionLineChart?.setScaleEnabled(false)
        subscriptionLineChart?.setPinchZoom(false)
        subscriptionLineChart?.setDrawGridBackground(false)

        //to hide background lines
        subscriptionLineChart?.xAxis?.setDrawGridLines(false)
        subscriptionLineChart?.axisLeft?.setDrawGridLines(false)
        subscriptionLineChart?.axisRight?.setDrawGridLines(false)

        // create marker to display box when values are selected
        val mv = ChartMarkerView(
            requireContext(),
            R.layout.custom_marker_revenue
        )

        // Set the marker to the chart
        mv.chartView = subscriptionLineChart
        subscriptionLineChart?.marker = mv

        subscriptionLineChart?.animateXY(1000, 500)
        subscriptionLineChart?.legend?.isEnabled = false
        subscriptionLineChart?.description?.isEnabled = false
        subscriptionLineChart?.extraBottomOffset = 10f
    }

    private fun initObserver() {
        homeSharedVM.computedData.observe(viewLifecycleOwner, Observer {
            setDataToSubscriptionChart(it)

        })
    }

    private fun setDataToSubscriptionChart(lineChartData: MutableList<AdminGraphResponse.Data>?) {
        var totalRevenue = 0.0
        lineChartData?.forEach {
            totalRevenue += it.subscriptionRevenue ?: 0.0
        }
        binding.totalRevenueTv.text="₹${totalRevenue}"

        val xAxisLabels = lineChartData?.map {
            it.month?.substring(0..2)
        }
        subscriptionLineChart?.xAxis?.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        subscriptionLineChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        subscriptionLineChart?.xAxis?.labelCount = (xAxisLabels?.size!!)

        val valuesSet1 = ArrayList<Entry>()
        val dataSets = ArrayList<ILineDataSet>()

        for (i in 0 until lineChartData?.size!!) {
            val value = lineChartData[i]?.subscriptionRevenue?.toFloat()
            valuesSet1.add(Entry((i).toFloat(), value!!))
        }

        // create a dataset and give it a type
        var set1: LineDataSet = LineDataSet(valuesSet1, "Subscription Dataset")
        set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
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
        dataSets.add(set1)

        //String setter in x-Axis
        val data = LineData(dataSets)
        subscriptionLineChart?.data = data
        subscriptionLineChart?.invalidate()
    }

    private fun init() {
        homeSharedVM = ViewModelProvider(requireActivity()).get(HomePageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChartSubscriptionRevenueBinding.inflate(inflater, container, false)
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

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {
    }


}