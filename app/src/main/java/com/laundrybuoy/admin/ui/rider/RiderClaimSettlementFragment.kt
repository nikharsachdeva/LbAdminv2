package com.laundrybuoy.admin.ui.rider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentRiderClaimSettlementBinding
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.makeButtonDisabled
import com.laundrybuoy.admin.utils.makeButtonEnabled
import com.laundrybuoy.admin.viewmodel.RiderViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable

private const val CLAIM_ID = "CLAIM_ID"
private const val AMOUNT = "AMOUNT"
private const val CLAIM_ACTION = "CLAIM_ACTION"
const val ACTION_APPROVE = "ACTION_APPROVE"
const val ACTION_REJECT = "ACTION_REJECT"

@AndroidEntryPoint
class RiderClaimSettlementFragment : BaseBottomSheet() {

    private var _binding: FragmentRiderClaimSettlementBinding? = null
    private val binding get() = _binding!!
    var claimActionReceived: String? = null
    var claimIdReceived: String? = null
    var claimAmountReceived: String? = null
    private val riderViewModel by viewModels<RiderViewModel>()
    val compositeDisposable = CompositeDisposable()


    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        fun newInstance(
            claimAction: String,
            claimId: String,
            amount: String,
        ): RiderClaimSettlementFragment {
            val settleFragment = RiderClaimSettlementFragment()
            val args = Bundle()
            args.putString(CLAIM_ACTION, claimAction)
            args.putString(AMOUNT, amount)
            args.putString(CLAIM_ID, claimId)
            settleFragment.arguments = args
            return settleFragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRiderClaimSettlementBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserver()
        onClick()
    }

    private fun initObserver() {
        riderViewModel.approveClaimLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Success -> {
                        if (it.data?.success == true && it.data.message?.contains("Approved") == true) {
                            if (::callback.isInitialized) {
                                dialog?.dismiss()
                                callback.invoke()
                            }
                            getMainActivity()?.showSnackBar(it.data.message)
                        }
                    }
                    is NetworkResult.Error -> {
                        getMainActivity()?.showSnackBar(it.message)
                    }
                }
            })
    }

    private fun init() {
        if (isAdded) {
            claimActionReceived = requireArguments().getString(CLAIM_ACTION) ?: ""
            claimIdReceived = requireArguments().getString(CLAIM_ID) ?: ""
            claimAmountReceived = requireArguments().getString(AMOUNT) ?: ""

            if (claimActionReceived == ACTION_APPROVE) {
                binding.settlementHeadingTv.text = "Are you sure you want to approve this claim?"
                binding.settlementDescEt.setText("Marking this transaction as settled.")
            } else if (claimActionReceived == ACTION_REJECT) {
                binding.settlementHeadingTv.text = "Are you sure you want to reject this claim?"
                binding.settlementDescEt.setText("Marking this transaction as rejected.")

            }

            binding.settlementAmtEt.setText(claimAmountReceived)
        }

        compositeDisposable.add(
            RxTextView.textChanges(binding.settlementDescEt)
                .subscribe {
                    if (it.trim().isNotEmpty()) {
                        binding.settlementSubmitBtn.makeButtonEnabled()
                    } else {
                        binding.settlementSubmitBtn.makeButtonDisabled()
                    }
                })


    }

    private fun onClick() {
        binding.closeSettlementIv.setOnClickListener {
            dialog?.dismiss()
        }

        binding.settlementSubmitBtn.setOnClickListener {

            val claimPayload = JsonObject()
            claimPayload.addProperty("claimId", claimIdReceived)
            claimPayload.addProperty("desc", binding.settlementDescEt.text.toString())

            if (claimActionReceived == ACTION_APPROVE) {
                riderViewModel.approveClaim(claimPayload)
            } else if (claimActionReceived == ACTION_REJECT) {
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.dispose()

    }


}