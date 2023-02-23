package com.laundrybuoy.admin.ui.rider

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.order.NotesAdapter
import com.laundrybuoy.admin.adapter.rider.RiderTransactionAdapter
import com.laundrybuoy.admin.databinding.FragmentRiderAttendanceBinding
import com.laundrybuoy.admin.databinding.FragmentRiderSettlementBinding
import com.laundrybuoy.admin.model.order.GetNotesResponse
import com.laundrybuoy.admin.model.rider.RiderTransactionModel
import com.laundrybuoy.admin.ui.order.ViewNoteConcernFragment
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.RiderViewModel
import com.whiteelephant.monthpicker.MonthPickerDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RiderSettlementFragment : BaseFragment() {

    private var _binding: FragmentRiderSettlementBinding? = null
    private val binding get() = _binding!!
    private val riderViewModel by viewModels<RiderViewModel>()
    var riderIdReceived: String? = null
    private lateinit var transactionAdapter: RiderTransactionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserver()
        clickListener()
        setDataToVm(getMainActivity()?.getCurrentMonth() ?: 0,
            getMainActivity()?.getCurrentYear() ?: 0)
    }

    private fun init() {
        riderIdReceived = arguments?.getString(RIDER_ID)

        transactionAdapter = RiderTransactionAdapter(object : RiderTransactionAdapter.OnClickInterface {
            override fun onTransactionClicked(transaction: RiderTransactionModel.Date.Txn) {

            }

            override fun onClaimApproveClicked(transaction: RiderTransactionModel.Date.Txn) {
                val settlementFragment =
                    RiderClaimSettlementFragment.newInstance(
                        ACTION_APPROVE,
                        transaction.id?:"",transaction.amount.toString()
                    )
                settlementFragment.setCallback {
                    setDataToVm(getMainActivity()?.getCurrentMonth() ?: 0,
                        getMainActivity()?.getCurrentYear() ?: 0)
                }
                settlementFragment.isCancelable = false
                settlementFragment.show(childFragmentManager, "settlement_frag")

            }

            override fun onClaimRejectClicked(transaction: RiderTransactionModel.Date.Txn) {
                val settlementFragment =
                    RiderClaimSettlementFragment.newInstance(
                        ACTION_REJECT,
                        transaction.id?:"",transaction.amount.toString()
                    )
                settlementFragment.isCancelable = false
                settlementFragment.show(childFragmentManager, "settlement_frag")
            }

            override fun onSearchClicked(transaction: RiderTransactionModel.Date.Txn) {
                val refId = transaction.itemId
                if(refId!=null){
                    val currentList = transactionAdapter.currentList
                    var refPos = -1
                    currentList
                        .forEachIndexed { index, txn ->
                            if(txn.id==refId){
                                refPos = index
                            }
                        }
                    if(refPos!=-1){
                        binding.transactionsRv.scrollToPosition(refPos)
                    }else{
                        getMainActivity()?.showSnackBar("No reference found!")
                    }
                }else{
                    getMainActivity()?.showSnackBar("No reference found!")
                }
            }


        })
        binding.transactionsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.transactionsRv.adapter = transactionAdapter
    }

    companion object {
        private const val RIDER_ID = "RIDER_ID"

        fun newInstance(
            riderId: String,
        ): RiderSettlementFragment {
            val viewFragment = RiderSettlementFragment()
            val args = Bundle()
            args.putString(RIDER_ID, riderId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    private fun initObserver() {

        riderViewModel._riderTransactionPeriodLD.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.transactionMonthTv.text =
                    "${(getMainActivity()?.getMonthString(it.get("month").asInt.minus(1)))} ${
                        it.get("year").asString
                    }"
                fetchTransactionForPeriod(it)
            }
        })

        riderViewModel.riderTransactionLiveData.observe(viewLifecycleOwner, Observer {
            showShimmer()
            when (it) {
                is NetworkResult.Loading -> {
                    showShimmer()
                }
                is NetworkResult.Success -> {
                    hideShimmer()
                    if (it.data?.success == true && (it.data.data != null)) {
                        if (it.data.data.txnList?.isNotEmpty()!!) {
                            setDataToRv(it.data.data.txnList)
                            binding.noFilesRl.makeViewGone()
                        }else{
                            binding.noFilesRl.makeViewVisible()
                            setDataToRv(emptyList())
                        }
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmer()
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

    }

    private fun setDataToRv(txnList: List<RiderTransactionModel.Date.Txn>) {
        transactionAdapter.submitList(txnList)
        binding.transactionsRv.scrollToPosition(txnList.size - 1)
    }

    private fun showShimmer(){
        binding.shimmerTransaction.pendingOrderShimmerSl.makeViewVisible()
        binding.transactionsRv.makeViewGone()
        binding.shimmerTransaction.pendingOrderShimmerSl.startShimmer()
    }

    private fun hideShimmer(){
        binding.transactionsRv.makeViewVisible()
        binding.shimmerTransaction.pendingOrderShimmerSl.makeViewGone()
        binding.shimmerTransaction.pendingOrderShimmerSl.stopShimmer()
    }


    private fun fetchTransactionForPeriod(period: JsonObject) {
        riderViewModel.getRiderTransaction(period)
    }

    private fun clickListener() {

        val builder = MonthPickerDialog.Builder(
            requireContext(), { selectedMonth, selectedYear ->
                setDataToVm((selectedMonth + 1), selectedYear)
            },
            getMainActivity()?.getCurrentYear()!!,
            getMainActivity()?.getCurrentMonth()!! - 1
        )

        builder
//            .setActivatedMonth(Calendar.APRIL)
            .setMinYear(2021)
//            .setActivatedYear(2021)
            .setMaxYear(2030)
            .setMinMonth(Calendar.FEBRUARY)
            .setTitle("Select a month")
            .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)

        binding.transactionDatePickerRl.setOnClickListener {
            builder.build().show()
        }

    }

    private fun setDataToVm(month: Int, year: Int) {
        val riderPayload = JsonObject()
        riderPayload.addProperty("month", month)
        riderPayload.addProperty("year", year)
        riderPayload.addProperty("riderId", riderIdReceived)
        riderViewModel.riderTransactionPeriod(riderPayload)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRiderSettlementBinding.inflate(inflater, container, false)
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