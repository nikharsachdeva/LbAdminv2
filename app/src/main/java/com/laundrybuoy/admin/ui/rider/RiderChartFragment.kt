package com.laundrybuoy.admin.ui.rider

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.order.paging.PartnerOrderLoaderAdapter
import com.laundrybuoy.admin.adapter.order.paging.PartnerOrderPagingAdapter
import com.laundrybuoy.admin.databinding.FragmentRiderChartBinding
import com.laundrybuoy.admin.databinding.FragmentRiderProfileBinding
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.model.rider.RiderGraphResponse
import com.laundrybuoy.admin.model.vendor.VendorOrderGraphModel
import com.laundrybuoy.admin.ui.order.OrderDetailRootFragment
import com.laundrybuoy.admin.ui.vendor.YearPickerFragment
import com.laundrybuoy.admin.utils.*
import com.laundrybuoy.admin.utils.chart.ChartMarkerView
import com.laundrybuoy.admin.viewmodel.RiderViewModel
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class RiderChartFragment : BaseFragment(), OnChartValueSelectedListener {

    private var _binding: FragmentRiderChartBinding? = null
    private val binding get() = _binding!!

    private var completedOrderBarChart: BarChart? = null
    private val riderViewModel by viewModels<RiderViewModel>()
    private var riderId: String? = null
    lateinit var riderOrderListAdapter: PartnerOrderPagingAdapter
    private var PICKED_DATASET = 0
    private var DELIVERED_DATASET = 1
    private var SELECTED_DATASET = -1


    companion object {
        private const val RIDER_ID = "RIDER_ID"

        fun newInstance(
            riderId: String,
        ): RiderChartFragment {
            val viewFragment = RiderChartFragment()
            val args = Bundle()
            args.putString(RIDER_ID, riderId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun fetchGraphDataForMonth(riderId: String, from: String, to: String) {
        val riderPayload = JsonObject()
        riderPayload.addProperty("riderId", riderId)
        riderPayload.addProperty("from", from)
        riderPayload.addProperty("to", to)

        viewLifecycleOwner.lifecycleScope.launch {
            riderViewModel.getRiderOrdersByMonth(riderPayload).observe(viewLifecycleOwner) {
                if(SELECTED_DATASET==PICKED_DATASET){
                    val pickedList = it.filter {
                        it.orderId.orderStatus==4
                    }
                    riderOrderListAdapter.submitData(lifecycle, pickedList)
                }else if(SELECTED_DATASET==DELIVERED_DATASET){
                    val deliveredList = it.filter {
                        it.orderId.orderStatus==10
                    }
                    riderOrderListAdapter.submitData(lifecycle, deliveredList)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        onClicks()
        initObserver()

        val year = Calendar.getInstance().get(Calendar.YEAR);
        riderViewModel.setChartYear(year.toString())
    }

    private fun onClicks() {

        binding.yearPickerRl.setOnClickListener {
            val yearFrag = YearPickerFragment()
            yearFrag.setCallback { year ->
                riderViewModel.setChartYear(year)
            }
            yearFrag.isCancelable = true
            yearFrag.show(childFragmentManager, "year_fragment")
        }


    }

    private fun initObserver() {

        riderViewModel._chartYearLiveData.observe(viewLifecycleOwner, Observer { year ->

            binding.chartYearTv.text = "${year}"
            fetchRiderGraphData(riderId, year)

        })

        riderViewModel.riderGraphLiveData.observe(viewLifecycleOwner, Observer {
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

    private fun fetchRiderGraphData(riderId: String?, year: String) {
        val riderPayload = JsonObject()
        riderPayload.addProperty("riderId", riderId)
        riderPayload.addProperty("year", year)
        riderViewModel.getRiderGraph(riderPayload)
    }

    private fun computeChartData(monthsListFromBackend: List<RiderGraphResponse.Data?>) {
        val emptyMonthList = getMainActivity()?.getAllMonthForGraphRider()
        monthsListFromBackend.forEachIndexed { indexOfBackend, backendData ->
            emptyMonthList?.forEachIndexed { indexOfDummy, dummyData ->
                if (backendData?.month == dummyData.month) {
                    dummyData.picked = backendData?.picked
                    dummyData.delivered = backendData?.delivered
                }
            }
        }

        setDataOrderChart(emptyMonthList)
    }

    private fun setDataOrderChart(orderChartList: MutableList<RiderGraphResponse.Data>?) {
        val xAxisLabels = orderChartList?.map {
            it.month?.substring(0..2)
        }
        completedOrderBarChart?.xAxis?.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        completedOrderBarChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        completedOrderBarChart?.xAxis?.labelCount = (xAxisLabels?.size ?: 0)

        val valuesPendingOrder = ArrayList<BarEntry>()
        val valuesDeliveredOrder = ArrayList<BarEntry>()
        for (i in 0 until orderChartList?.size!!) {
            val value1 = orderChartList[i].picked?.toFloat()
            val value2 = orderChartList[i].delivered?.toFloat()
            valuesPendingOrder.add(BarEntry((i).toFloat(), value1!!))
            valuesDeliveredOrder.add(BarEntry((i).toFloat(), value2!!))
        }

        val set1: BarDataSet
        val set2: BarDataSet

        if (completedOrderBarChart?.data != null &&
            completedOrderBarChart?.data!!.dataSetCount > 0
        ) {
            set1 = completedOrderBarChart?.data!!.getDataSetByIndex(0) as BarDataSet
            set2 = completedOrderBarChart?.data!!.getDataSetByIndex(1) as BarDataSet
            set1.values = valuesPendingOrder
            set2.values = valuesDeliveredOrder
            completedOrderBarChart?.data!!.notifyDataChanged()
            completedOrderBarChart?.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(valuesPendingOrder, "Picked orders")
            set1.color = Color.parseColor("#9030b856");
            set1.formSize = 15f;
            set1.valueTextSize = 12f;
            set1.valueTextColor = Color.parseColor("#30b856")
            set1.setDrawValues(false)

            set2 = BarDataSet(valuesDeliveredOrder, "Delivered orders")
            set2.color = Color.parseColor("#00b7fd");
            set2.formSize = 15f;
            set2.valueTextSize = 12f;
            set2.valueTextColor = Color.parseColor("#00b7fd")
            set2.setDrawValues(false)
        }

        var data = BarData(set1, set2)
        data.barWidth = 0.35f
        completedOrderBarChart?.data = data
        completedOrderBarChart?.setFitBars(true)
        completedOrderBarChart?.legend?.isEnabled=true

        val barSpace = 0.08f
        val groupSpace = 0.1f

        completedOrderBarChart?.isDragEnabled = false
        completedOrderBarChart?.xAxis?.axisMinimum = 0f
        completedOrderBarChart?.xAxis?.setCenterAxisLabels(true);
        completedOrderBarChart?.groupBars(0f, groupSpace, barSpace)
        completedOrderBarChart?.invalidate()

    }

    private fun showChartData() {
        binding.dataAvailableLl.makeViewVisible()
        binding.dataUnAvailableLl.makeViewGone()
    }

    private fun hideChartData() {
        binding.dataAvailableLl.makeViewGone()
        binding.dataUnAvailableLl.makeViewVisible()
    }

    private fun init() {
        riderId = requireArguments()?.getString(RIDER_ID)

        initOrderChart()
        initOrdersRv()
    }

    private fun initOrdersRv() {
        riderOrderListAdapter =
            PartnerOrderPagingAdapter(object : PartnerOrderPagingAdapter.OnClickInterface {
                override fun onSelected(
                    order: VendorOrderListingModel.Data.Partner.OrderId,
                    position: Int,
                ) {
                    val frag = OrderDetailRootFragment.newInstance(orderId = order.id ?: "")
                    getMainActivity()?.addFragment(
                        true,
                        getMainActivity()?.getVisibleFrame()!!,
                        frag
                    )
                }

            })

        binding.riderOrdersRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.riderOrdersRv.setHasFixedSize(true)
        binding.riderOrdersRv.adapter = riderOrderListAdapter.withLoadStateHeaderAndFooter(
            header = PartnerOrderLoaderAdapter(),
            footer = PartnerOrderLoaderAdapter()
        )
        riderOrderListAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && riderOrderListAdapter.itemCount < 1) {
                binding.ordersUnAvailableLl.makeViewVisible()
                binding.riderOrdersRv.makeViewGone()
            } else {
                binding.ordersUnAvailableLl.makeViewGone()
                binding.riderOrdersRv.makeViewVisible()
            }
        }
    }

    private fun initOrderChart() {

        completedOrderBarChart = binding.riderOrderCompleteBc

        completedOrderBarChart?.description?.isEnabled = false

        completedOrderBarChart?.setOnChartValueSelectedListener(this)

        completedOrderBarChart!!.xAxis.textSize = 12f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            completedOrderBarChart!!.xAxis.typeface = resources.getFont(R.font.lexend_light)
        }


        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        completedOrderBarChart?.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        completedOrderBarChart?.setPinchZoom(false)

        completedOrderBarChart?.setDrawBarShadow(false)
        completedOrderBarChart?.setDrawGridBackground(false)
        completedOrderBarChart?.setDrawBorders(false);

        val xAxis: XAxis = completedOrderBarChart?.xAxis!!
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f;
        xAxis.setDrawAxisLine(false);

        val leftAxis: YAxis = completedOrderBarChart?.axisLeft!!
        leftAxis.setDrawAxisLine(false)
        leftAxis.setGranularity(1.0f);
        leftAxis.setGranularityEnabled(true);

        val rightAxis: YAxis = completedOrderBarChart?.axisRight!!
        rightAxis.setDrawAxisLine(false)
        rightAxis.setGranularity(1.0f);
        rightAxis.setGranularityEnabled(true);

        // add a nice and smooth animation
        completedOrderBarChart?.animateY(1500)

        val mv = ChartMarkerView(
            requireContext(),
            R.layout.custom_marker_map
        )

        // Set the marker to the chart
        mv.chartView = completedOrderBarChart
        completedOrderBarChart?.marker = mv

        completedOrderBarChart?.legend?.isEnabled = false
        completedOrderBarChart?.extraBottomOffset = 10f

    }

    override fun onValueSelected(e: Entry, h: Highlight?) {
        Log.d("xoxo1", "onValueSelected: "+Gson().toJson(e))
        Log.d("xoxo2", "onValueSelected: "+Gson().toJson(h))
        SELECTED_DATASET = h?.dataSetIndex?:-1
        var datePair = requireContext().getFirstAndLastDate(e.x.toInt())
        fetchGraphDataForMonth(riderId ?: "", datePair.first ?: "", datePair.second ?: "")

        binding.showingDatesTv.makeViewVisible()
        binding.showingDatesDivTv.makeViewVisible()
        binding.showingDatesTv.text =
            "Showing for ${datePair.first?.getFormattedDateInString()} - ${datePair.second?.getFormattedDateInString()}"
        binding.riderOrdersRv.makeViewVisible()
    }

    override fun onNothingSelected() {
        binding.riderOrdersRv.makeViewGone()
        binding.showingDatesTv.makeViewGone()
        binding.showingDatesDivTv.makeViewGone()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRiderChartBinding.inflate(inflater, container, false)
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