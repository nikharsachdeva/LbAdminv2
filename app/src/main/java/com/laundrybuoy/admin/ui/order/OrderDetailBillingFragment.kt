package com.laundrybuoy.admin.ui.order

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.order.OrderItemAdapter
import com.laundrybuoy.admin.databinding.FragmentOrderDetailBillingBinding
import com.laundrybuoy.admin.model.order.OrderDetailResponse
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.getFormattedDateWithTime
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailBillingFragment : BaseFragment() {

    private var _binding: FragmentOrderDetailBillingBinding? = null
    private val binding get() = _binding!!
    lateinit var ordersViewModel: OrdersViewModel
    private lateinit var orderItemAdapter: OrderItemAdapter
    var orderType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOrderDetailBillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObservers()

    }

    private fun initObservers() {
        ordersViewModel.orderDetailLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data?.data != null) {
                        setBillingUI(it.data?.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })
    }

    private fun setBillingUI(data: OrderDetailResponse.Data) {
        if (data.bill?.items.isNullOrEmpty()) {
            binding.billingAvailableRl.makeViewGone()
            binding.billingUnAvailableLl.makeViewVisible()
        } else {
            binding.billingAvailableRl.makeViewVisible()
            binding.billingUnAvailableLl.makeViewGone()
            orderItemAdapter.submitList(data.bill?.items)
            var total = 0
            data.bill?.items?.map {
                total += it?.quantity!!
            }
            binding.totalCountItem.text = "Total Items : $total"


            if (data.bill?.totalWeight == null) {
                binding.totalWeightRl.makeViewGone()
                binding.billedWeightRl.makeViewGone()
                binding.billTotalWeightTv.text = ""
                binding.billBilledWeightTv.text = ""
                orderType = "cloth"
            } else {
                binding.totalWeightRl.makeViewVisible()
                binding.billedWeightRl.makeViewVisible()
                binding.billTotalWeightTv.text = "${data.bill.totalWeight} Kg"
                binding.billBilledWeightTv.text = "${data.bill.billedWeight} Kg"
                orderType = "kg"
            }

            if (data.bill?.deliveryCost?.toInt()== 0) {
                binding.plusAvailableLl.makeViewVisible()
                binding.plusValueTv.text="₹"+data.bill?.actualDeliveryCost.toString()?:""
                binding.plusUnitNameTv.text="delivery discount"
                binding.plusNameTv.makeViewVisible()
                binding.plusNameTv.text="LB Plus Membership!"
                binding.plusUnavailableLl.makeViewGone()
            } else {
                binding.plusAvailableLl.makeViewGone()
                binding.plusNameTv.makeViewGone()
                binding.plusUnavailableLl.makeViewVisible()
                binding.plusUnavailableTv.text="Standard delivery charges applied!"
            }


            if (data.bill?.couponStatus?.id == null) {
                binding.couponDiscountRl.makeViewGone()

                binding.couponAvailableLl.makeViewGone()
                binding.couponNameTv.makeViewGone()
                binding.couponUnavailableLl.makeViewVisible()
                binding.couponUnavailableTv.text="No Coupon Applied!"

            } else {
                binding.couponDiscountRl.makeViewVisible()
                binding.billCouponValueTv.text = "₹${data.bill?.couponDiscount}/-"
                binding.billCouponKeyTv.text = "Coupon (${data.bill?.couponStatus.name})"

                binding.couponAvailableLl.makeViewVisible()
                binding.couponValueTv.text="₹"+data.bill?.couponDiscount.toString()?:""
                binding.couponUnitNameTv.text="coupon discount"
                binding.couponNameTv.makeViewVisible()
                binding.couponNameTv.text=data?.bill?.couponStatus?.name
                binding.couponUnavailableLl.makeViewGone()

            }

            if (data.bill?.subscriptionStatus?.id == null) {
                binding.subscriptionRl.makeViewGone()
                binding.billSubscriptionTv.text = ""

                binding.subscriptionAvailableLl.makeViewGone()
                binding.subscriptionNameTv.makeViewGone()
                binding.subscriptionUnavailableLl.makeViewVisible()
                binding.subscriptionUnavailableTv.text=data.bill?.subscriptionStatus?.message?:""

            } else {
                binding.subscriptionRl.makeViewVisible()
                binding.billSubscriptionTv.text =
                    "${data.bill?.usedSubscriptionBalance} ${orderType!!.capitalize()}"

                binding.subscriptionAvailableLl.makeViewVisible()
                binding.subscriptionValueTv.text=data.bill?.usedSubscriptionBalance.toString()?:""
                binding.subscriptionUnitNameTv.text=orderType!!.capitalize() +" discount"
                binding.subscriptionNameTv.makeViewVisible()
                binding.subscriptionNameTv.text=data.bill.subscriptionStatus.name +"(${data.bill.subscriptionStatus.subType})"
                binding.subscriptionUnavailableLl.makeViewGone()
            }


            binding.billCoinsTv.text =
                "${data.bill?.coinUsed} coins used , ₹${data.bill?.coinDiscount}/-"

            binding.billGrossTotalTv.text = "₹${data.bill?.grossTotal}/-"
            binding.billDeliveryTv.text = "₹${data.bill?.deliveryCost}/-"
            binding.billNetTotalTv.text = "₹${data.bill?.netTotal}/-"
            binding.billPaymentTypeTv.text = data.bill?.paymentType?.capitalize()



            //Partner's Cut
            binding.totalBillValue.text="₹"+data?.partnerPayment?.amount.toString()

            if (data.partnerPayment?.isSetteled == true) {
                binding.billingSettlementStatus.text = "Settled"
                binding.billingSettlementStatus.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#30b856"
                        )
                    )
                )
                binding.billingSettlementStatus.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#1030b856"))

                binding.billingSettlementDate.text =
                    data?.partnerPayment?.settlementDate?.getFormattedDateWithTime()
                binding.billingSettlementDate.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#10ff8935"))
                binding.billingSettlementDate.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#ff8935"
                        )
                    )
                )

                binding.billingSettlementDate.makeViewVisible()
            } else {
                binding.billingSettlementStatus.text = "Unsettled"
                binding.billingSettlementStatus.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#fc2254"
                        )
                    )
                )
                binding.billingSettlementStatus.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#10fc2254"))

                binding.billingSettlementDate.text=""
                binding.billingSettlementDate.makeViewGone()
            }

        }


    }

    private fun init() {
        ordersViewModel = ViewModelProvider(requireActivity()).get(OrdersViewModel::class.java)

        orderItemAdapter = OrderItemAdapter()
        binding.billingItemsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.billingItemsRv.adapter = orderItemAdapter

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