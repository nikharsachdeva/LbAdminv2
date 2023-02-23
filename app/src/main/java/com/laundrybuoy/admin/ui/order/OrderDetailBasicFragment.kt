package com.laundrybuoy.admin.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.order.ConcernAdapter
import com.laundrybuoy.admin.adapter.order.NotesAdapter
import com.laundrybuoy.admin.adapter.order.OrderTagsAdapter
import com.laundrybuoy.admin.databinding.FragmentOrderDetailBasicBinding
import com.laundrybuoy.admin.model.order.GetConcernResponse
import com.laundrybuoy.admin.model.order.GetNotesResponse
import com.laundrybuoy.admin.model.order.OrderDetailResponse
import com.laundrybuoy.admin.utils.*
import com.laundrybuoy.admin.viewmodel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderDetailBasicFragment : BaseFragment() {

    private var _binding: FragmentOrderDetailBasicBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteAdapter: NotesAdapter
    private lateinit var concernAdapter: ConcernAdapter
    private lateinit var tagAdapter: OrderTagsAdapter
    private var orderIdReceived: String? = null
    private val ordersViewModel2 by viewModels<OrdersViewModel>()
    lateinit var ordersViewModel1: OrdersViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObservers()

    }

    private fun initObservers() {
        ordersViewModel1.orderDetailLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.data != null) {
                        setUI(it.data?.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })


        ordersViewModel2.getConcernLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        setConcernRv(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })


        ordersViewModel2.getNotesLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        setNotesRv(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })
    }

    private fun setNotesRv(data: List<GetNotesResponse.Data?>?) {
        if (data.isNullOrEmpty()) {
            binding.noNotesTv.makeViewVisible()
            binding.notesOrderRv.makeViewGone()
        } else {
            binding.noNotesTv.makeViewGone()
            binding.notesOrderRv.makeViewVisible()
            noteAdapter.submitList(data)
        }
    }

    private fun setConcernRv(data: List<GetConcernResponse.Data?>?) {
        if (data.isNullOrEmpty()) {
            binding.noConcernTv.makeViewVisible()
            binding.concernOrderRv.makeViewGone()
        } else {
            binding.noConcernTv.makeViewGone()
            binding.concernOrderRv.makeViewVisible()
            concernAdapter.submitList(data)
        }
    }

    private fun setUI(data: OrderDetailResponse.Data) {
        val addressString = data.orderDetails?.deliveryAddress?.line1 + " " +
                data.orderDetails?.deliveryAddress?.landmark + "\n" +
                data.orderDetails?.deliveryAddress?.city + "," +
                data.orderDetails?.deliveryAddress?.pin

        binding.orderDateTv.text = data.orderDetails?.orderDate?.getFormattedDate()
        binding.orderNumberTv.text = data.orderDetails?.ordNum.toString()
        binding.orderAddressTv.text = addressString
        binding.orderCNameTv.text = data.orderDetails?.userName
        binding.orderServiceTv.text = data?.orderDetails?.serviceId?.serviceName

        val orderStatus = data.orderDetails?.orderStatus ?: 0
        binding.orderStageTv.text = orderStatus.getOrderStatus()

        if (data.orderDetails?.tagIds?.isEmpty() == true) {
            binding.orderTagsRv.makeViewGone()
            binding.tagsUnavailableTv.makeViewVisible()
        } else {
            binding.orderTagsRv.makeViewVisible()
            binding.tagsUnavailableTv.makeViewGone()
            tagAdapter.submitList(data.getClothesTags())
        }

    }

    private fun init() {
        orderIdReceived = arguments?.getString(ORDER_ID)
        ordersViewModel1 = ViewModelProvider(requireActivity()).get(OrdersViewModel::class.java)
        initNotesRv()
        initTagsRv()
        initConcernRv()

        val orderIdPayload = JsonObject()
        orderIdPayload.addProperty("orderId", orderIdReceived)
        ordersViewModel2.getConcernsOrder(orderIdPayload)
        ordersViewModel2.getNotesOrder(orderIdPayload)


    }

    private fun initTagsRv() {
        tagAdapter = OrderTagsAdapter()
        binding.orderTagsRv.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.orderTagsRv.adapter = tagAdapter
    }

    private fun initConcernRv() {

        concernAdapter = ConcernAdapter(object : ConcernAdapter.OnClickInterface {
            override fun onConcernClicked(doc: GetConcernResponse.Data) {
                val viewBtmSheet =
                    ViewNoteConcernFragment.newInstance("CONCERN", null, doc)
                viewBtmSheet.isCancelable = true
                viewBtmSheet.show(childFragmentManager, "view_concern")
            }

        })
        binding.concernOrderRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.concernOrderRv.adapter = concernAdapter

    }

    private fun initNotesRv() {
        noteAdapter = NotesAdapter(object : NotesAdapter.OnClickInterface {
            override fun onNoteClicked(doc: GetNotesResponse.Data) {
                val viewBtmSheet =
                    ViewNoteConcernFragment.newInstance("NOTE", doc, null)
                viewBtmSheet.isCancelable = true
                viewBtmSheet.show(childFragmentManager, "view_note")
            }

        })
        binding.notesOrderRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.notesOrderRv.adapter = noteAdapter
    }


    companion object {
        private const val ORDER_ID = "ORDER_ID"

        fun newInstance(
            orderId: String,
        ): OrderDetailBasicFragment {
            val viewFragment = OrderDetailBasicFragment()
            val args = Bundle()
            args.putString(ORDER_ID, orderId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOrderDetailBasicBinding.inflate(inflater, container, false)
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