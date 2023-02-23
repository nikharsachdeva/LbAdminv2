package com.laundrybuoy.admin.ui.order

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.order.paging.AllOrderListPagingAdapter
import com.laundrybuoy.admin.adapter.order.paging.PartnerOrderLoaderAdapter
import com.laundrybuoy.admin.adapter.order.paging.PartnerOrderPagingAdapter
import com.laundrybuoy.admin.databinding.FragmentHomeBinding
import com.laundrybuoy.admin.databinding.FragmentOrderListingBinding
import com.laundrybuoy.admin.model.order.AllOrderListModel
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.ui.rider.RiderProfileFragment
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.OrdersViewModel
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

@AndroidEntryPoint
class OrderListingFragment : BaseFragment() {

    private var _binding: FragmentOrderListingBinding? = null
    private val binding get() = _binding!!
    private val ordersViewModel by viewModels<OrdersViewModel>()
    lateinit var orderListAdapter: AllOrderListPagingAdapter
    private var payloadMetaData: JsonObject? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initOrdersRv()
        onClick()
    }

    private fun init() {
        if (isAdded) {
            try {
                payloadMetaData = JsonParser().parse(requireArguments().getString(META_DATA)) as JsonObject?
                callRespectiveApi()
            }catch (e:Exception){
                getMainActivity()?.showSnackBar(e.localizedMessage)
            }

        }
    }

    private fun callRespectiveApi() {
        val screenType = payloadMetaData?.get("screenType")?.asString
        val riderId = payloadMetaData?.get("riderId")?.asString
        val partnerId = payloadMetaData?.get("partnerId")?.asString
        val from = payloadMetaData?.get("from")?.asString
        val to = payloadMetaData?.get("to")?.asString
        val url = payloadMetaData?.get("url")?.asString

        when (screenType) {
            "riderProfile" -> {
                val metaData = JsonObject()
                metaData.addProperty("riderId", riderId)
                metaData.addProperty("from", from)
                metaData.addProperty("to", to)
                metaData.addProperty("url", url)

                viewLifecycleOwner.lifecycleScope.launch {
                    ordersViewModel.getRiderOrderListing(metaData).observe(viewLifecycleOwner) {
                        orderListAdapter.submitData(lifecycle, it)
                    }
                }
            }
            "partnerProfile" -> {
                val metaData = JsonObject()
                metaData.addProperty("partnerId", partnerId)
                metaData.addProperty("from", from)
                metaData.addProperty("to", to)
                metaData.addProperty("url", url)

                viewLifecycleOwner.lifecycleScope.launch {
                    ordersViewModel.getPartnerOrderListing(metaData).observe(viewLifecycleOwner) {
                        orderListAdapter.submitData(lifecycle, it)
                    }
                }
            }
            "adminHome" -> {
                val metaData = JsonObject()
                metaData.addProperty("from", from)
                metaData.addProperty("to", to)
                metaData.addProperty("url", url)

                viewLifecycleOwner.lifecycleScope.launch {
                    ordersViewModel.getHomeOrderListing(metaData).observe(viewLifecycleOwner) {
                        orderListAdapter.submitData(lifecycle, it)
                    }
                }
            }
        }
    }

    private fun onClick() {
        binding.backFromOrderListIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }
    }


    private fun initOrdersRv() {
        orderListAdapter =
            AllOrderListPagingAdapter(object : AllOrderListPagingAdapter.OnClickInterface {
                override fun onSelected(
                    order: AllOrderListModel.Data.Partner,
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

        binding.orderListingRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.orderListingRv.setHasFixedSize(true)
        binding.orderListingRv.adapter = orderListAdapter.withLoadStateHeaderAndFooter(
            header = PartnerOrderLoaderAdapter(),
            footer = PartnerOrderLoaderAdapter()
        )
        orderListAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && orderListAdapter.itemCount < 1) {
                binding.ordersUnAvailableLl.makeViewVisible()
                binding.orderListingRv.makeViewGone()
            } else {
                binding.ordersUnAvailableLl.makeViewGone()
                binding.orderListingRv.makeViewVisible()
            }
        }

    }


    companion object {
        private const val META_DATA = "META_DATA"

        fun newInstance(
            metaDataString: String,
        ): OrderListingFragment {
            val viewFragment = OrderListingFragment()
            val args = Bundle()
            args.putString(META_DATA, metaDataString)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOrderListingBinding.inflate(inflater, container, false)
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