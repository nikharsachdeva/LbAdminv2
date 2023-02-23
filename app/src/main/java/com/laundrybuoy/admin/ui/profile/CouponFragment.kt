package com.laundrybuoy.admin.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.CouponFilterAdapter
import com.laundrybuoy.admin.adapter.CouponsAdapter
import com.laundrybuoy.admin.databinding.FragmentCouponBinding
import com.laundrybuoy.admin.model.profile.GetCouponsResponse
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class CouponFragment : BaseFragment() {

    private var _binding: FragmentCouponBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    private lateinit var couponAdapter: CouponsAdapter
    lateinit var couponFilterAdapter: CouponFilterAdapter
    var currentSelectedFilter = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserver()

        try {
            binding.couponFiltersRv
                .viewTreeObserver
                .addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            // At this point the layout is complete and the
                            // dimensions of recyclerView and any child views
                            // are known.
                            binding.couponFiltersRv.findViewHolderForAdapterPosition(0)?.itemView?.performClick()

                            binding.couponFiltersRv
                                .viewTreeObserver
                                .removeOnGlobalLayoutListener(this)
                        }
                    })
        } catch (e: Exception) {
            Log.d("exec-->", "onViewCreated: " + e.localizedMessage)
        }


        onClick()

    }

    private fun getCoupons(filter: String) {
        profileViewModel.getAllCoupons(filter)
    }

    private fun initObserver() {
        profileViewModel._couponsLiveData.observe(viewLifecycleOwner, Observer {
            couponAdapter.submitList(it)
            couponAdapter.notifyDataSetChanged()
        })


        profileViewModel.getCouponsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    /*
                    if (it.data?.success == true && !it.data?.data.isNullOrEmpty()) {
                        setCouponsData(it.data.data)
                    }
                     */
                    setCouponsData(it.data?.data ?: emptyList())

                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })
    }

    private fun setCouponsData(data: List<GetCouponsResponse.Data>) {
        profileViewModel.setCouponsDocs(data.toMutableList())
    }

    private fun init() {

        initFilterRv()
        initCouponRv()
    }

    private fun initFilterRv() {
        couponFilterAdapter = CouponFilterAdapter(object : CouponFilterAdapter.OnClickInterface {
            override fun onFilterSelected(filter: String) {
                currentSelectedFilter = filter
                getCoupons(filter)
            }


        })
        binding.couponFiltersRv.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.couponFiltersRv.adapter = couponFilterAdapter

        couponFilterAdapter.submitList(arrayListOf("all", "active", "inactive"))
    }

    private fun onClick() {
        binding.closeCouponIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }

        binding.addCouponCv.setOnClickListener {
            val updateFrag = AddCouponFragment()
            updateFrag.setCallback {
               getCoupons(currentSelectedFilter)
            }
            updateFrag.isCancelable = true
            updateFrag.show(childFragmentManager, "add_coupon")

        }
    }

    private fun initCouponRv() {
        couponAdapter = CouponsAdapter(object : CouponsAdapter.OnClickInterface {
            override fun onCouponClicked(coupon: GetCouponsResponse.Data) {
                val viewFrag = ViewCouponDetailsFragment.newInstance(coupon)
                viewFrag.setCallback {

                    try {
                        binding.couponFiltersRv
                            .viewTreeObserver
                            .addOnGlobalLayoutListener(
                                object : ViewTreeObserver.OnGlobalLayoutListener {
                                    override fun onGlobalLayout() {
                                        // At this point the layout is complete and the
                                        // dimensions of recyclerView and any child views
                                        // are known.
                                        binding.couponFiltersRv.findViewHolderForAdapterPosition(0)?.itemView?.performClick()

                                        binding.couponFiltersRv
                                            .viewTreeObserver
                                            .removeOnGlobalLayoutListener(this)
                                    }
                                })
                    } catch (e: Exception) {
                        Log.d("exec-->", "onViewCreated: " + e.localizedMessage)
                    }
                }
                viewFrag.isCancelable = true
                viewFrag.show(childFragmentManager, "view_coupon")
            }

        })
        binding.uploadedCouponsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.uploadedCouponsRv.adapter = couponAdapter
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCouponBinding.inflate(inflater, container, false)
        return binding.root
    }


    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(false)
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