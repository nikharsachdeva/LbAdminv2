package com.laundrybuoy.admin.ui.order

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.home.AdminChartPagerAdapter
import com.laundrybuoy.admin.adapter.home.HomeFilterAdapter
import com.laundrybuoy.admin.databinding.FragmentOrdersBinding
import com.laundrybuoy.admin.model.home.AdminGraphResponse
import com.laundrybuoy.admin.model.home.HomeFiltersModel
import com.laundrybuoy.admin.ui.NotificationFragment
import com.laundrybuoy.admin.ui.SearchBottomSheetFragment
import com.laundrybuoy.admin.ui.notification.NotificationRootFragment
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.chart.ChartMarkerView
import com.laundrybuoy.admin.viewmodel.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class OrdersFragment : BaseFragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomePageViewModel>()
    lateinit var homeSharedVM: HomePageViewModel

    private lateinit var homeOrderFilterAdapter: HomeFilterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNav()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
        onClick()
        initObserver()

        initAdminChartTabs()
        val year = Calendar.getInstance().get(Calendar.YEAR);
        homeViewModel.setGraphYear(year)
    }

    private fun init() {
        homeSharedVM = ViewModelProvider(requireActivity()).get(HomePageViewModel::class.java)
        initOrderFiltersRv()

    }

    private fun initAdminChartTabs() {
        val chartTabLayout = binding.adminChartTabLayout
        val chartViewPager = binding.adminChartViewPager

        chartTabLayout.addTab(chartTabLayout.newTab().setText("Orders"))
        chartTabLayout.addTab(chartTabLayout.newTab().setText("Revenue"))
        chartTabLayout.addTab(chartTabLayout.newTab().setText("Package"))
        chartTabLayout.addTab(chartTabLayout.newTab().setText("Subscriptions"))
        chartTabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = AdminChartPagerAdapter(
            requireContext(), childFragmentManager,
            chartTabLayout.tabCount
        )
        chartViewPager.adapter = adapter
        chartViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                chartTabLayout
            )
        )
        chartViewPager.offscreenPageLimit = 4
        chartTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                chartViewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun initObserver() {
        homeViewModel._graphYearLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { year ->
//            binding.chartYearTv.text = "${year}"
            val yearPayload = JsonObject()
            yearPayload.addProperty("year", year)
            fetchAdminGraphData(yearPayload)
            fetchAdminHomeFigures(year)
        })

        homeViewModel.homeFiguresLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data!=null && !it.data?.data.isNullOrEmpty()) {
                        showFiguresUi(it.data?.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

        homeSharedVM.adminGraphLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data!=null) {
                        prepareDataForCharts(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })
    }

    private fun showFiguresUi(data: List<HomeFiltersModel.Data>?) {
        homeOrderFilterAdapter.submitList(data)

    }

    private fun prepareDataForCharts(monthsListFromBackend: List<AdminGraphResponse.Data>) {
        val emptyMonthList = getMainActivity()?.getAllMonthForAdminGraph()
        monthsListFromBackend.forEachIndexed { indexOfBackend, backendData ->
            emptyMonthList?.forEachIndexed { indexOfDummy, dummyData ->
                if (backendData?.month == dummyData.month) {
                    dummyData.orderRevenue = backendData?.orderRevenue
                    dummyData.ordersDelivered = backendData?.ordersDelivered
                    dummyData.ordersReceived = backendData?.ordersReceived
                    dummyData.packageRevenue = backendData?.packageRevenue
                    dummyData.subscriptionRevenue = backendData?.subscriptionRevenue
                    dummyData.totalOrders = backendData?.totalOrders
                }
            }
        }

        homeSharedVM.setComputedData(emptyMonthList!!)
    }

    private fun fetchAdminGraphData(yearPayload: JsonObject) {
        homeSharedVM.getAdminGraphData(yearPayload)
    }

    private fun fetchAdminHomeFigures(year: Int) {
        val datePayload = JsonObject()
        datePayload.addProperty("from", "${year}-01-01T00:00:00Z")
        datePayload.addProperty("to", "${year}-12-31T23:59:59Z")
        homeViewModel.getAdminHomeFigures(datePayload)
    }

    private fun onClick() {
        binding.orderSearchRl.setOnClickListener {
            getMainActivity()?.addFragment(true, getMainActivity()?.getVisibleFrame()!!,
                SearchBottomSheetFragment()
            )
        }

        binding.orderSearchEt.setOnClickListener {
            getMainActivity()?.addFragment(true, getMainActivity()?.getVisibleFrame()!!,
                SearchBottomSheetFragment()
            )

        }

        binding.adminNotificationIconIv.setOnClickListener {
            getMainActivity()?.addFragment(true,getMainActivity()?.getVisibleFrame()!!,NotificationRootFragment())
        }
    }


    private fun initOrderFiltersRv() {
        homeOrderFilterAdapter =
            HomeFilterAdapter(object : HomeFilterAdapter.OnClickInterface {
                override fun onFilterClicked(filter: HomeFiltersModel.Data) {
                    goToListingPage(filter)

                }

            })

        val layoutManagerFilter = GridLayoutManager(requireContext(), 2)
        binding.ordersFiltersRv.layoutManager = layoutManagerFilter
        binding.ordersFiltersRv.adapter = homeOrderFilterAdapter
    }

    private fun goToListingPage(filter: HomeFiltersModel.Data) {
        val metaData = JsonObject()
        metaData.addProperty("screenType", "adminHome")
        metaData.addProperty("from", "${homeViewModel._graphYearLiveData.value}-01-01T00:00:00Z")
        metaData.addProperty("to", "${homeViewModel._graphYearLiveData.value}-12-31T23:59:59Z")
        metaData.addProperty("url", filter.baseUrl)

        val listingFrag = OrderListingFragment.newInstance(metaData.toString())
        getMainActivity()?.addFragment(true,getMainActivity()?.getVisibleFrame()!!,listingFrag)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
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