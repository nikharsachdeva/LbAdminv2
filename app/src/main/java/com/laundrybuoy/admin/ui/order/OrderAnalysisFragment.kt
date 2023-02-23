package com.laundrybuoy.admin.ui.order

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentOrderAnalysisBinding
import com.laundrybuoy.admin.model.order.OrderChartAnalysis
import com.laundrybuoy.admin.ui.FilterFragment
import com.laundrybuoy.admin.utils.chart.ChartMarkerView

class OrderAnalysisFragment : BaseFragment(), OnChartValueSelectedListener {

    private var _binding: FragmentOrderAnalysisBinding? = null
    private val binding get() = _binding!!
    private var orderAnalysisLineChart: LineChart? = null
    val filterPayload = JsonObject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initOrderCharts() {

        orderAnalysisLineChart = binding.orderAnalysisLc

        val xAxis: XAxis = orderAnalysisLineChart?.xAxis!!
        xAxis.setDrawAxisLine(false) // to remove bottom x axis line
        xAxis.textColor = Color.parseColor("#31394C")

        // set listeners
        orderAnalysisLineChart!!.setOnChartValueSelectedListener(this)

        orderAnalysisLineChart?.axisRight?.isEnabled=false

        orderAnalysisLineChart!!.xAxis.textSize = 12f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            orderAnalysisLineChart!!.xAxis.typeface = resources.getFont(R.font.lexend_light)
        }
//        orderAnalysisLineChart!!.setExtraOffsets(20f, 10f, 10f, 20f)

        //customization
        orderAnalysisLineChart?.setTouchEnabled(true)
        orderAnalysisLineChart?.isDragEnabled = true
        orderAnalysisLineChart?.setScaleEnabled(false)
        orderAnalysisLineChart?.setPinchZoom(false)
        orderAnalysisLineChart?.setDrawGridBackground(false)

        //to hide background lines
        orderAnalysisLineChart?.xAxis?.setDrawGridLines(false)
        orderAnalysisLineChart?.axisLeft?.setDrawGridLines(false)
        orderAnalysisLineChart?.axisRight?.setDrawGridLines(false)

        // create marker to display box when values are selected
        val mv = ChartMarkerView(
            requireContext(),
            R.layout.custom_marker_revenue
        )

        // Set the marker to the chart
        mv.chartView = orderAnalysisLineChart
        orderAnalysisLineChart?.marker = mv

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initOrderCharts()
        setDataToChart(getDynamicChartData())
    }

    private fun init() {

        filterPayload.addProperty("startDate", "20/09/2022")
        filterPayload.addProperty("endDate", "21/09/2022")


        binding.orderAnalysisFilterIv.setOnClickListener {

            val filterFragment =
                FilterFragment.newInstance(filterPayload)
            filterFragment.show(childFragmentManager, "filter_fragment")

        }
    }

    private fun getDynamicChartData(): OrderChartAnalysis {

        val listOf: MutableList<OrderChartAnalysis.OrderChartAnalysisItem> = arrayListOf()

        val item1A: MutableList<OrderChartAnalysis.OrderChartAnalysisItem.DataSet> = arrayListOf()
        item1A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Sun",
                yAxis = "110"
            )
        )

        item1A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Mon",
                yAxis = "120"
            )
        )

        item1A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Tue",
                yAxis = "150"
            )
        )

        item1A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Wed",
                yAxis = "110"
            )
        )

        item1A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Thu",
                yAxis = "115"
            )
        )

        item1A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Fri",
                yAxis = "120"
            )
        )

        item1A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Sat",
                yAxis = "200"
            )
        )

        val item1 = OrderChartAnalysis.OrderChartAnalysisItem(
            datasetName = "Laundry",
            datasetHex = "FEC8D8",
            dataSet = item1A
        )

        val item2A: MutableList<OrderChartAnalysis.OrderChartAnalysisItem.DataSet> = arrayListOf()
        item2A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Sun",
                yAxis = "150"
            )
        )

        item2A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Mon",
                yAxis = "170"
            )
        )

        item2A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Tue",
                yAxis = "200"
            )
        )

        item2A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Wed",
                yAxis = "160"
            )
        )

        item2A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Thu",
                yAxis = "175"
            )
        )

        item2A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Fri",
                yAxis = "190"
            )
        )

        item2A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Sat",
                yAxis = "220"
            )
        )

        val item2 = OrderChartAnalysis.OrderChartAnalysisItem(
            datasetName = "Iron",
            datasetHex = "A7BED3",
            dataSet = item2A
        )

        val item3A: MutableList<OrderChartAnalysis.OrderChartAnalysisItem.DataSet> = arrayListOf()
        item3A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Sun",
                yAxis = "250"
            )
        )

        item3A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Mon",
                yAxis = "270"
            )
        )

        item3A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Tue",
                yAxis = "300"
            )
        )

        item3A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Wed",
                yAxis = "260"
            )
        )

        item3A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Thu",
                yAxis = "275"
            )
        )

        item3A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Fri",
                yAxis = "290"
            )
        )

        item3A.add(
            OrderChartAnalysis.OrderChartAnalysisItem.DataSet(
                xAxis = "Sat",
                yAxis = "320"
            )
        )


        val item3 = OrderChartAnalysis.OrderChartAnalysisItem(
            datasetName = "Dry Clean",
            datasetHex = "F6AA90",
            dataSet = item3A
        )


        listOf.add(item1)
        listOf.add(item2)
        listOf.add(item3)


        val mainData = OrderChartAnalysis(
            success = true,
            message = "Success",
            data = listOf
        )

        Log.d("piku-->", "getDynamicChartData: " + Gson().toJson(mainData))
        return mainData

    }

    private fun setDataToChart(data: OrderChartAnalysis) {

        val dataSets = ArrayList<ILineDataSet>()

        val xAxisLabels = data.data!![0]?.dataSet!!.map {
            it?.xAxis
        }
        Log.d("xxyyzz-->", "setDataToChart: " + Gson().toJson(xAxisLabels))
        orderAnalysisLineChart?.xAxis?.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        orderAnalysisLineChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        orderAnalysisLineChart?.xAxis?.labelCount = (xAxisLabels.size)

        orderAnalysisLineChart?.xAxis?.granularity = 1f

        orderAnalysisLineChart?.setTouchEnabled(true)
        orderAnalysisLineChart?.isDragEnabled = true
        orderAnalysisLineChart?.setScaleEnabled(false)
        orderAnalysisLineChart?.setPinchZoom(false)
        orderAnalysisLineChart?.setDrawGridBackground(false)

        orderAnalysisLineChart?.xAxis?.setDrawGridLines(false)
        orderAnalysisLineChart?.axisLeft?.setDrawGridLines(false)
        orderAnalysisLineChart?.axisRight?.setDrawGridLines(false)

        orderAnalysisLineChart?.legend?.isEnabled = false
        orderAnalysisLineChart?.description?.isEnabled = false

        orderAnalysisLineChart?.extraBottomOffset = 10f


        data.data?.forEachIndexed { index, element ->

            val values = ArrayList<Entry>()

            data.data[index]?.dataSet?.forEachIndexed { index, dataSet ->
                values.add(Entry(index.toFloat(), dataSet?.yAxis?.toFloat()!!))
            }

            val lineDataSet = LineDataSet(values, element?.datasetName)
            lineDataSet.lineWidth = 1.75f
            lineDataSet.circleRadius = 2.5f
            lineDataSet.circleHoleRadius = 2.5f
            if (element?.datasetHex?.length == 6) {
                lineDataSet.color = (Color.parseColor("#" + element?.datasetHex))
            } else {
                lineDataSet.color = resources.getColor(R.color.hintColor)
            }
            lineDataSet.setCircleColor(resources.getColor(R.color.hintColor))
            lineDataSet.circleHoleColor = resources.getColor(R.color.hintColor)
            lineDataSet.highLightColor = resources.getColor(R.color.hintColor)
            lineDataSet.valueTextColor = Color.WHITE
            lineDataSet.valueTextSize = 12f
            lineDataSet.setDrawValues(false)
            lineDataSet.setDrawHighlightIndicators(false)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                lineDataSet.valueTypeface = resources.getFont(R.font.lexend_light)
            }
            lineDataSet.setDrawFilled(true)
            lineDataSet.fillColor = (Color.parseColor("#10" + element?.datasetHex))

            dataSets.add(lineDataSet)

        }

        val mainData = LineData(dataSets)
        orderAnalysisLineChart?.data = mainData
        orderAnalysisLineChart?.invalidate()
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
        Log.d("xxyyzz--", "onValueSelected: " + Gson().toJson(e))
        Log.d("xxyyzz--", "onValueSelected: " + Gson().toJson(h))
    }

    override fun onNothingSelected() {
    }
}