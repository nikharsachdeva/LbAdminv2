package com.laundrybuoy.admin.ui.rider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.vendor.VendorBadgeAdapter
import com.laundrybuoy.admin.adapter.vendor.VendorRatingsAdapter
import com.laundrybuoy.admin.databinding.FragmentRiderReviewBinding
import com.laundrybuoy.admin.databinding.FragmentVendorReviewBinding
import com.laundrybuoy.admin.model.vendor.VendorRatingResponse
import com.laundrybuoy.admin.ui.vendor.VendorReviewFragment
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.RiderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RiderReviewFragment : BaseFragment() {

    private var _binding: FragmentRiderReviewBinding? = null
    private val binding get() = _binding!!
    var riderIdReceived: String? = null
    private val riderViewModel by viewModels<RiderViewModel>()

    private lateinit var vendorRatingAdapter: VendorRatingsAdapter
    private lateinit var vendorBadgeAdapter: VendorBadgeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRatingRv()
        initBadgeRv()
        initObserver()

        riderIdReceived = arguments?.getString(RIDER_ID)
        val jsonPayload = JsonObject()
        jsonPayload.addProperty("riderId",riderIdReceived)
        riderViewModel.getRiderRating(jsonPayload)

    }

    private fun initObserver() {
        riderViewModel.riderRatingLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Success -> {
                        if (it.data?.success == true && it.data?.data != null) {
                            setRatingUI(it.data.data)
                        }
                    }
                    is NetworkResult.Error -> {
                        getMainActivity()?.showSnackBar(it.message)
                    }
                }
            })
    }

    private fun setRatingUI(data: VendorRatingResponse.Data) {

        vendorRatingAdapter.submitList(data.ratings?.subList(0, data.ratings.size - 2))
        var totalRating = 0.0
        var averageRating = 0.0
        val totalRatingObj = data.ratings?.find {
            it?.name == "totalRatings"
        }
        val averageRatingObj = data.ratings?.find {
            it?.name == "average"
        }
        totalRating = totalRatingObj?.figure ?: 0.0
        averageRating = averageRatingObj?.figure?.toDouble() ?: 0.0
        binding.totalRiderReviewTv.text = "${totalRating.toInt()} customer ratings"
        binding.riderReviewTv.text = "${averageRating} out of 5"
        binding.riderReviewRb.rating = averageRating.toFloat()

        vendorBadgeAdapter.submitList(data.badges)
    }

    private fun initRatingRv() {
        vendorRatingAdapter =
            VendorRatingsAdapter()

        val layoutManagerRating =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.riderRatingsRv.layoutManager = layoutManagerRating
        binding.riderRatingsRv.adapter = vendorRatingAdapter
    }

    private fun initBadgeRv() {
        vendorBadgeAdapter =
            VendorBadgeAdapter()

        val layoutManagerBadge =
            GridLayoutManager(requireContext(), 2)
        binding.riderBadgesRv.layoutManager = layoutManagerBadge
        binding.riderBadgesRv.adapter = vendorBadgeAdapter
    }

    companion object {
        private const val RIDER_ID = "RIDER_ID"

        fun newInstance(
            riderId: String,
        ): RiderReviewFragment {
            val viewFragment = RiderReviewFragment()
            val args = Bundle()
            args.putString(RIDER_ID, riderId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRiderReviewBinding.inflate(inflater, container, false)
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