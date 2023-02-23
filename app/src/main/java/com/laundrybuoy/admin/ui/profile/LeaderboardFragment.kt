package com.laundrybuoy.admin.ui.profile

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.LeaderboardAdapter
import com.laundrybuoy.admin.databinding.FragmentLeaderboardBinding
import com.laundrybuoy.admin.model.profile.LeaderboardPayload
import com.laundrybuoy.admin.model.profile.LeaderboardResponse
import com.laundrybuoy.admin.ui.vendor.VendorDocsFragment
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.loadImageWithGlide
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

const val SCREEN_TYPE = "SCREEN_TYPE"

@AndroidEntryPoint
class LeaderboardFragment : BaseFragment() {

    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding!!
    var screenTypeReceived: String? = null
    private val profileViewModel by viewModels<ProfileViewModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    private lateinit var leaderboardAdapter: LeaderboardAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            screenTypeReceived = arguments?.getString(SCREEN_TYPE)
        }

        initObserver()
        init()
        onClicks()
        setLeaderboardRv()

    }

    private fun setLeaderboardRv() {

        leaderboardAdapter = LeaderboardAdapter(object : LeaderboardAdapter.OnClickInterface {
            override fun onLeaderboardClicked(doc: LeaderboardResponse.Data) {
                getMainActivity()?.showSnackBar(doc.name)
            }

        })
        binding.leaderboardBtmsht.leaderboardListRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.leaderboardBtmsht.leaderboardListRv.adapter = leaderboardAdapter

    }

    private fun initObserver() {
        profileViewModel.riderLeaderboardLiveData.observe(viewLifecycleOwner, Observer {
            showShimmer()
            when (it) {
                is NetworkResult.Loading -> {
                    showShimmer()
                }
                is NetworkResult.Success -> {
                    hideShimmer()
                    if (it.data?.status == true && it.data?.data != null) {
                        setUI(it.data.data)
                    }

                }
                is NetworkResult.Error -> {
                    hideShimmer()
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

        profileViewModel.vendorLeaderboardLiveData.observe(viewLifecycleOwner, Observer {
            showShimmer()
            when (it) {
                is NetworkResult.Loading -> {
                    showShimmer()
                }
                is NetworkResult.Success -> {
                    hideShimmer()
                    if (it.data?.status == true && it.data?.data != null) {
                        setUI(it.data.data)
                    }

                }
                is NetworkResult.Error -> {
                    hideShimmer()
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })
    }

    private fun onClicks() {
        binding.leaderboardBtmsht.closeRouteBtmShtIv.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }

        binding.backFromLeaderboardIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }

    }

    private fun MutableList<LeaderboardResponse.Data>.checkIfIndexExist(index: Int): Boolean {
        return index >= 0 && index < this.size
    }

    private fun setUI(leaderboardListing: MutableList<LeaderboardResponse.Data>) {
        if (leaderboardListing.checkIfIndexExist(0)) {
            binding.leaderboardNo1Iv.loadImageWithGlide(leaderboardListing[0].profile.toString())
            binding.leaderboardNo1Name.text =
                leaderboardListing[0].name.toString().split(" ").toTypedArray()[0]
            binding.leaderboardNo1Points.text = leaderboardListing[0].points.toString()
        }

        if (leaderboardListing.checkIfIndexExist(1)) {
            binding.leaderboardNo3Iv.loadImageWithGlide(leaderboardListing[1].profile.toString())
            binding.leaderboardNo3Name.text =
                leaderboardListing[1].name.toString().split(" ").toTypedArray()[0]
            binding.leaderboardNo3Points.text = leaderboardListing[1].points.toString()
        }

        if (leaderboardListing.checkIfIndexExist(2)) {
            binding.leaderboardNo2Iv.loadImageWithGlide(leaderboardListing[2].profile.toString())
            binding.leaderboardNo2Name.text =
                leaderboardListing[2].name.toString().split(" ").toTypedArray()[0]
            binding.leaderboardNo2Points.text = leaderboardListing[2].points.toString()
        }

        if (leaderboardListing.size > 3) {
            binding.leaderboardBtmsht.leaderboardListRv.makeViewVisible()
            binding.leaderboardBtmsht.noDeliveryBoyTv.makeViewGone()
            leaderboardAdapter.submitList(leaderboardListing.subList(3, leaderboardListing.size))
        } else {
            binding.leaderboardBtmsht.leaderboardListRv.makeViewGone()
            binding.leaderboardBtmsht.noDeliveryBoyTv.makeViewVisible()
        }

    }


    private fun init() {
        bottomSheetBehavior =
            BottomSheetBehavior.from<RelativeLayout>(binding.leaderboardBtmsht.persistentBottomSheet)

        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, state: Int) {
                print(state)
                when (state) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.leaderboardBtmsht.showRouteIndicatorIv.makeViewVisible()
                        binding.leaderboardBtmsht.closeRouteBtmShtIv.makeViewGone()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.leaderboardBtmsht.showRouteIndicatorIv.makeViewGone()
                        binding.leaderboardBtmsht.closeRouteBtmShtIv.makeViewVisible()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.leaderboardBtmsht.showRouteIndicatorIv.makeViewVisible()
                        binding.leaderboardBtmsht.closeRouteBtmShtIv.makeViewGone()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        bottomSheetBehavior.maxHeight = getMainActivity()?.getDeviceHeight()?.minus(100)!!

        binding.rlMonthly.setOnClickListener {
            binding.rlMonthly.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.primary))
            binding.tvMonthly.setTextColor(Color.WHITE)
            binding.rlAllTime.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#6a5ab0"))
            binding.tvAllTime.setTextColor(getMainActivity()!!.resources.getColor(R.color.hintColor))
            fetchLeaderboard("month", screenTypeReceived)
        }
        binding.rlAllTime.setOnClickListener {
            binding.rlAllTime.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.primary))
            binding.tvAllTime.setTextColor(Color.WHITE)
            binding.rlMonthly.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#6a5ab0"))
            binding.tvMonthly.setTextColor(getMainActivity()!!.resources.getColor(R.color.hintColor))
            fetchLeaderboard("all", screenTypeReceived)

        }

        binding.rlMonthly.performClick()
    }

    private fun fetchLeaderboard(duration: String, screenTypeReceived: String?) {
        val payload = LeaderboardPayload(
            reqType = duration,
            month = getMainActivity()?.getCurrentMonth(),
            year = getMainActivity()?.getCurrentYear()
        )

        if (screenTypeReceived == "PARTNER") {
            binding.leaderboardBtmsht.noDeliveryBoyTv.text="No other partners present!"
            profileViewModel.getVendorLeaderboard(payload)
        } else if (screenTypeReceived == "RIDER") {
            binding.leaderboardBtmsht.noDeliveryBoyTv.text="No other riders present!"
            profileViewModel.getRiderLeaderboard(payload)
        }
    }


    private fun showShimmer() {
        binding.shimmerLeaderboard.pendingOrderShimmerSl.makeViewVisible()
        binding.leaderboardRootRl.makeViewGone()
        binding.leaderboardBtmsht.leaderboardListRv.makeViewGone()
        binding.leaderboardBtmsht.noDeliveryBoyTv.makeViewGone()
        binding.shimmerLeaderboard.pendingOrderShimmerSl.startShimmer()
    }

    private fun hideShimmer() {
        binding.leaderboardRootRl.makeViewVisible()
        binding.shimmerLeaderboard.pendingOrderShimmerSl.makeViewGone()
        binding.leaderboardBtmsht.leaderboardListRv.makeViewVisible()
        binding.leaderboardBtmsht.noDeliveryBoyTv.makeViewVisible()
        binding.shimmerLeaderboard.pendingOrderShimmerSl.stopShimmer()
    }


    companion object {
        @JvmStatic
        fun newInstance(
            screenType: String,
        ) =
            LeaderboardFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_TYPE, screenType)
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
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