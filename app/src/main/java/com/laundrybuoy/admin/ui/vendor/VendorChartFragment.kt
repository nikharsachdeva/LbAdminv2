package com.laundrybuoy.admin.ui.vendor

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.order.paging.PartnerOrderLoaderAdapter
import com.laundrybuoy.admin.adapter.order.paging.PartnerOrderPagingAdapter
import com.laundrybuoy.admin.databinding.FragmentVendorChartBinding
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.model.vendor.VendorOrderGraphModel
import com.laundrybuoy.admin.ui.order.OrderDetailRootFragment
import com.laundrybuoy.admin.utils.*
import com.laundrybuoy.admin.utils.chart.ChartMarkerView
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class VendorChartFragment : BaseFragment(), OnChartValueSelectedListener {

    private var _binding: FragmentVendorChartBinding? = null
    private val binding get() = _binding!!

    private var completedOrderBarChart: BarChart? = null
    private val vendorViewModel by viewModels<VendorViewModel>()
    private var partnerId: String? = null
    lateinit var vendorOrderListAdapter: PartnerOrderPagingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        private const val VENDOR_ID = "VENDOR_ID"

        fun newInstance(
            vendorId: String,
        ): VendorChartFragment {
            val viewFragment = VendorChartFragment()
            val args = Bundle()
            args.putString(VENDOR_ID, vendorId)
            viewFragment.arguments = args
            return viewFragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentVendorChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        init()
        onClicks()
    }

    private fun init() {
        partnerId = arguments?.getString(VENDOR_ID)
        val year = Calendar.getInstance().get(Calendar.YEAR);
        vendorViewModel.setChartYear(year.toString())

        binding.vendorOrdersRv.makeViewGone()
        binding.showingDatesTv.makeViewGone()
        binding.showingDatesDivTv.makeViewGone()

        initOrderChart()
        initOrdersRv()
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

        setDataOrderChart(emptyMonthList)
    }

    private fun showChartData() {
        binding.dataAvailableLl.makeViewVisible()
        binding.dataUnAvailableLl.makeViewGone()
    }

    private fun hideChartData() {
        binding.dataAvailableLl.makeViewGone()
        binding.dataUnAvailableLl.makeViewVisible()
    }

    private fun fetchGraphData(partnerIdReceived: String?, year: String) {
        val partnerPayload = JsonObject()
        partnerPayload.addProperty("partnerId", partnerIdReceived)
        partnerPayload.addProperty("year", year)
        vendorViewModel.getVendorOrderGraph(partnerPayload)
    }

    private fun fetchOrderData(partnerIdReceived: String, from: String, to : String){
        val partnerPayload = JsonObject()
        partnerPayload.addProperty("partnerId", partnerIdReceived)
        partnerPayload.addProperty("from", from)
        partnerPayload.addProperty("to", to)

        viewLifecycleOwner.lifecycleScope.launch {
            vendorViewModel.getVendorOrders(partnerPayload).observe(viewLifecycleOwner) {
                vendorOrderListAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun initOrdersRv() {
        vendorOrderListAdapter =
            PartnerOrderPagingAdapter(object : PartnerOrderPagingAdapter.OnClickInterface {
                override fun onSelected(order: VendorOrderListingModel.Data.Partner.OrderId, position: Int) {
                    val frag = OrderDetailRootFragment.newInstance(orderId = order.id ?: "")
                    getMainActivity()?.addFragment(
                        true,
                        getMainActivity()?.getVisibleFrame()!!,
                        frag
                    )
                }

            })

        binding.vendorOrdersRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.vendorOrdersRv.setHasFixedSize(true)
        binding.vendorOrdersRv.adapter = vendorOrderListAdapter.withLoadStateHeaderAndFooter(
            header = PartnerOrderLoaderAdapter(),
            footer = PartnerOrderLoaderAdapter()
        )
        vendorOrderListAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && vendorOrderListAdapter.itemCount < 1) {
                binding.ordersUnAvailableLl.makeViewVisible()
                binding.vendorOrdersRv.makeViewGone()
            } else {
                binding.ordersUnAvailableLl.makeViewGone()
                binding.vendorOrdersRv.makeViewVisible()
            }
        }

    }

    private fun setDataOrderChart(orderChartList: MutableList<VendorOrderGraphModel.Data>?) {

        val xAxisLabels = orderChartList?.map {
            it.month?.substring(0..2)
        }
        completedOrderBarChart?.xAxis?.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        completedOrderBarChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        completedOrderBarChart?.xAxis?.labelCount = (xAxisLabels?.size ?: 0)
        val values = ArrayList<BarEntry>()

        for (i in 0 until orderChartList?.size!!) {
            val value = orderChartList[i].orders?.toFloat()
            values.add(BarEntry((i).toFloat(), value!!))
        }

        val set1: BarDataSet

        if (completedOrderBarChart?.data != null &&
            completedOrderBarChart?.data!!.dataSetCount > 0
        ) {
            set1 = completedOrderBarChart?.data!!.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            completedOrderBarChart?.data!!.notifyDataChanged()
            completedOrderBarChart?.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "Data Set")
            set1.color = Color.parseColor("#9030b856");
            set1.formSize = 15f;
            set1.valueTextSize = 12f;
            set1.valueTextColor = Color.parseColor("#30b856")
            set1.setDrawValues(false)
            val dataSets = java.util.ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.barWidth = 0.7f
            completedOrderBarChart?.data = data
            completedOrderBarChart?.setFitBars(true)
        }

        completedOrderBarChart?.invalidate()
    }

    private fun initOrderChart() {

        completedOrderBarChart = binding.vendorOrderCompleteBc

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
        xAxis.position = XAxisPosition.BOTTOM
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
//        getMainActivity()?.getFirstAndLastMonthWise(e.x.toInt())
        var datePair = requireContext().getFirstAndLastDate(e.x.toInt())
        fetchOrderData(partnerId?:"",datePair.first?:"",datePair.second?:"")

        binding.showingDatesTv.makeViewVisible()
        binding.showingDatesDivTv.makeViewVisible()
        binding.showingDatesTv.text="Showing for ${datePair.first?.getFormattedDateInString()} - ${datePair.second?.getFormattedDateInString()}"
        binding.vendorOrdersRv.makeViewVisible()
    }

    override fun onNothingSelected() {
        binding.vendorOrdersRv.makeViewGone()
        binding.showingDatesTv.makeViewGone()
        binding.showingDatesDivTv.makeViewGone()
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