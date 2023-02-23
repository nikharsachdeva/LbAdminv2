package com.laundrybuoy.admin.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentHomeBinding
import com.laundrybuoy.admin.utils.chart.ChartMarkerView

import com.github.mikephil.charting.formatter.ValueFormatter
import com.laundrybuoy.admin.utils.MyFirebaseMessagingService
import com.laundrybuoy.admin.utils.SharedPreferenceManager


class HomeFragment : BaseFragment(), OnChartValueSelectedListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var ordersLineChart: LineChart? = null
    private var servicesPieChart: PieChart? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNav()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOrdersCharts()
        setDataToOrdersCharts()

        initServicesPieChart()
        setDataToServicesPieChart()

        Log.d("xxyyxx1", "onViewCreated:"+MyFirebaseMessagingService().getToken(requireContext()))
        Log.d("xxyyxx2", "onViewCreated:"+SharedPreferenceManager.getFCMToken())
    }

    private fun initServicesPieChart() {
        servicesPieChart = binding.servicesPieChartPc

    }

    private fun setDataToServicesPieChart() {
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(3F, "Pass"))
        listColors.add(resources.getColor(R.color.teal_700))
        listPie.add(PieEntry(6F, "Fail"))
        listColors.add(resources.getColor(R.color.purple_200))
        listPie.add(PieEntry(5F, "Unanswered"))
        listColors.add(resources.getColor(R.color.pinkMusicTabText))

        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String? {
                return "" + value.toInt()
            }
        }

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors
        pieDataSet.setDrawValues(true)
        pieDataSet.valueTextSize = 20f
        pieDataSet.valueTextColor = R.color.white

        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(formatter)

        servicesPieChart?.data = pieData

        val legend = servicesPieChart?.legend
        servicesPieChart?.legend?.isWordWrapEnabled = true
        servicesPieChart?.legend?.isEnabled = true
        legend?.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend?.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT // position
        legend?.formSize = 20F
        legend?.formToTextSpace = 10f
        legend?.form = Legend.LegendForm.LINE // form type : line, square, circle ..
        legend?.textSize = 10f
        legend?.orientation = Legend.LegendOrientation.HORIZONTAL // side by side or bottom to bottom
        legend?.isWordWrapEnabled = true


        servicesPieChart?.setUsePercentValues(false)
        servicesPieChart?.setDrawSliceText(false)
        servicesPieChart?.isDrawHoleEnabled = true
        servicesPieChart?.transparentCircleRadius = 95F
        servicesPieChart?.setHoleColor(R.color.transparent)
        servicesPieChart?.description?.isEnabled = false
        servicesPieChart?.setEntryLabelColor(R.color.white)
        servicesPieChart?.animateY(1400, Easing.EaseInOutQuad)
    }

    private fun initOrdersCharts() {

        ordersLineChart = binding.ordersLineChartLc

        val xAxisLabel = ArrayList<String>()
        xAxisLabel.add("Sun")
        xAxisLabel.add("Mon")
        xAxisLabel.add("Tue")
        xAxisLabel.add("Wed")
        xAxisLabel.add("Thu")
        xAxisLabel.add("Fri")
        xAxisLabel.add("Sat")

        val xAxis: XAxis = ordersLineChart?.xAxis!!
        xAxis.setDrawAxisLine(false) // to remove bottom x axis line
        xAxis.textColor = Color.WHITE

        ordersLineChart?.xAxis?.valueFormatter = IndexAxisValueFormatter(xAxisLabel)


        // set listeners
        ordersLineChart!!.setOnChartValueSelectedListener(this)

        ordersLineChart!!.xAxis.textSize = 12f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ordersLineChart!!.xAxis.typeface = resources.getFont(R.font.lexend_light)
        }
        ordersLineChart!!.setExtraOffsets(20f, 10f, 10f, 20f)

        //customization
        ordersLineChart?.setTouchEnabled(true)
        ordersLineChart?.isDragEnabled = true
        ordersLineChart?.setScaleEnabled(false)
        ordersLineChart?.setPinchZoom(false)
        ordersLineChart?.setDrawGridBackground(false)

        //to hide background lines
        ordersLineChart?.xAxis?.setDrawGridLines(false)
        ordersLineChart?.axisLeft?.setDrawGridLines(false)
        ordersLineChart?.axisRight?.setDrawGridLines(false)

        // create marker to display box when values are selected

        // create marker to display box when values are selected
        val mv = ChartMarkerView(
            requireContext(),
            R.layout.custom_marker_map
        )

        // Set the marker to the chart

        // Set the marker to the chart
        mv.setChartView(ordersLineChart)
        ordersLineChart?.marker = mv

    }

    private fun setDataToOrdersCharts() {

        val incomeEntries = getIncomeEntries()

        val dataSets: ArrayList<ILineDataSet?> = ArrayList()
        val set1 = LineDataSet(incomeEntries, "Orders")
        set1.mode=LineDataSet.Mode.HORIZONTAL_BEZIER
        dataSets.add(set1)


        //to hide right Y and top X border
        val rightYAxis = ordersLineChart?.axisRight
        rightYAxis?.isEnabled = false
        val leftYAxis = ordersLineChart?.axisLeft
        leftYAxis?.isEnabled = false
        val topXAxis = ordersLineChart?.xAxis
        topXAxis?.isEnabled = false

        val xAxis = ordersLineChart?.xAxis
        xAxis?.granularity = 1f
        xAxis?.setCenterAxisLabels(false)
        xAxis?.isEnabled = true
        xAxis?.setDrawGridLines(false)
        xAxis?.position = XAxis.XAxisPosition.BOTTOM

        set1.lineWidth = 1.75f
        set1.circleRadius = 2.5f
        set1.circleHoleRadius = 2.5f
        set1.color = resources.getColor(R.color.pinkMusicTabText)
        set1.setCircleColor(resources.getColor(R.color.pinkMusicTabText))
        set1.circleHoleColor = resources.getColor(R.color.pinkMusicTabText)
        set1.highLightColor = resources.getColor(R.color.pinkMusicTabText)
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
                R.drawable.pink_gradient_steps_chart
            )
            set1.fillDrawable = drawable
        } else {
            set1.fillColor = resources.getColor(R.color.pinkMusicTabImage)
        }

        //String setter in x-Axis

        val data = LineData(dataSets)
        ordersLineChart?.data = data
        ordersLineChart?.animateXY(1000, 500)
        ordersLineChart?.invalidate()
        ordersLineChart?.legend?.isEnabled = false
        ordersLineChart?.description?.isEnabled = false

    }

    private fun getIncomeEntries(): List<Entry>? {
        val incomeEntries = ArrayList<Entry>()
        incomeEntries.add(Entry(0f, 11300f))
        incomeEntries.add(Entry(1f, 5567f))
        incomeEntries.add(Entry(2f, 9881f))
        incomeEntries.add(Entry(3f, 7200f))
        incomeEntries.add(Entry(4f, 8450f))
        incomeEntries.add(Entry(5f, 6500f))
        incomeEntries.add(Entry(6f, 8000f))
        return incomeEntries
    }

    override fun onValueSelected(e: Entry, h: Highlight?) {
        Log.i("Entry selected", e.toString())
        Log.i(
            "LOW HIGH",
            "low: " + ordersLineChart?.lowestVisibleX + ", high: " + ordersLineChart?.highestVisibleX
        )
        Log.i(
            "MIN MAX",
            "xMin: " + ordersLineChart?.xChartMin + ", xMax: " + ordersLineChart?.xChartMax + ", yMin: " + ordersLineChart?.yChartMin + ", yMax: " + ordersLineChart?.yChartMax
        )
    }

    override fun onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.")
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