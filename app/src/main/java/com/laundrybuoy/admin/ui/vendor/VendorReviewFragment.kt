package com.laundrybuoy.admin.ui.vendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.vendor.VendorBadgeAdapter
import com.laundrybuoy.admin.adapter.vendor.VendorRatingsAdapter
import com.laundrybuoy.admin.databinding.FragmentVendorReviewBinding
import com.laundrybuoy.admin.model.vendor.VendorRatingResponse
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VendorReviewFragment : BaseFragment() {

    private var _binding: FragmentVendorReviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var vendorRatingAdapter: VendorRatingsAdapter
    private lateinit var vendorBadgeAdapter: VendorBadgeAdapter
    private val vendorViewModel by viewModels<VendorViewModel>()
    var vendorIdReceived: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        private const val VENDOR_ID = "VENDOR_ID"

        fun newInstance(
            vendorId: String,
        ): VendorReviewFragment {
            val viewFragment = VendorReviewFragment()
            val args = Bundle()
            args.putString(VENDOR_ID, vendorId)
            viewFragment.arguments = args
            return viewFragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initRatingRv()
        initBadgeRv()
        initObserver()

        vendorIdReceived = arguments?.getString(VENDOR_ID)
        val jsonPayload = JsonObject()
        jsonPayload.addProperty("partnerId", vendorIdReceived)
        vendorViewModel.getPartnerRating(jsonPayload)
    }

    private fun initBadgeRv() {
        vendorBadgeAdapter =
            VendorBadgeAdapter()

        val layoutManagerBadge =
            GridLayoutManager(requireContext(), 2)
        binding.vendorBadgesRv.layoutManager = layoutManagerBadge
        binding.vendorBadgesRv.adapter = vendorBadgeAdapter
    }

    private fun initObserver() {
        vendorViewModel.partnerRatingLiveData.observe(viewLifecycleOwner,
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
        binding.totalVendorReviewTv.text = "${totalRating.toInt()} customer ratings"
        binding.vendorReviewTv.text = "${averageRating} out of 5"
        binding.vendorReviewRb.rating = averageRating.toFloat()


        vendorBadgeAdapter.submitList(data.badges)
    }

    private fun initRatingRv() {
        vendorRatingAdapter =
            VendorRatingsAdapter()

        val layoutManagerRating =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.vendorRatingsRv.layoutManager = layoutManagerRating
        binding.vendorRatingsRv.adapter = vendorRatingAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentVendorReviewBinding.inflate(inflater, container, false)
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