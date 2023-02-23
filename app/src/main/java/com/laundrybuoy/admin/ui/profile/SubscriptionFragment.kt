package com.laundrybuoy.admin.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.CouponFilterAdapter
import com.laundrybuoy.admin.adapter.PackagesAdapter
import com.laundrybuoy.admin.adapter.SubscriptionAdapter
import com.laundrybuoy.admin.databinding.FragmentPackageBinding
import com.laundrybuoy.admin.databinding.FragmentSubscriptionBinding
import com.laundrybuoy.admin.model.profile.GetPackagesResponse
import com.laundrybuoy.admin.model.profile.GetSubscriptionResponse
import com.laundrybuoy.admin.utils.HorizontalMarginItemDecoration
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionFragment : BaseFragment() {

    private var _binding: FragmentSubscriptionBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    lateinit var packageFilterAdapter: CouponFilterAdapter
    private lateinit var subscriptionAdapter: SubscriptionAdapter
    var currentSelectedFilter = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
        initObserver()

        try {
            binding.subscriptionFiltersRv
                .viewTreeObserver
                .addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            // At this point the layout is complete and the
                            // dimensions of recyclerView and any child views
                            // are known.
                            binding.subscriptionFiltersRv.findViewHolderForAdapterPosition(0)?.itemView?.performClick()

                            binding.subscriptionFiltersRv
                                .viewTreeObserver
                                .removeOnGlobalLayoutListener(this)
                        }
                    })
        } catch (e: Exception) {
            Log.d("exec-->", "onViewCreated: " + e.localizedMessage)
        }


        onClick()

    }

    private fun init() {

        initFilterRv()
        initSubscriptionVp()
    }

    private fun initObserver() {

        profileViewModel.getSubscriptionLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        setSubscriptionData(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

        profileViewModel.disableSubscriptionLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        getSubscription(currentSelectedFilter)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

    }

    private fun setSubscriptionData(data: List<GetSubscriptionResponse.Data?>?) {
        if (data.isNullOrEmpty()) {
            binding.subscriptionViewPager.makeViewGone()
            binding.subscriptionUnavailable.makeViewVisible()
        } else {
            binding.subscriptionViewPager.makeViewVisible()
            binding.subscriptionUnavailable.makeViewGone()
            subscriptionAdapter.submitList(data)

        }
        binding.subscriptionViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

    }


    private fun initFilterRv() {
        packageFilterAdapter = CouponFilterAdapter(object : CouponFilterAdapter.OnClickInterface {
            override fun onFilterSelected(filter: String) {
                currentSelectedFilter = filter
                getSubscription(filter)
            }


        })
        binding.subscriptionFiltersRv.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.subscriptionFiltersRv.adapter = packageFilterAdapter

        packageFilterAdapter.submitList(arrayListOf("all", "active", "inactive"))
    }

    private fun getSubscription(filter: String) {
        profileViewModel.getAllSubscription(filter)
    }

    private fun onClick() {
        binding.closeSubscriptionIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }

        binding.addSubIv.setOnClickListener {
            val addFrag = AddSubscriptionFragment()
            addFrag.setCallback {
                getSubscription(currentSelectedFilter)
            }
            addFrag.isCancelable = true
            addFrag.show(childFragmentManager, "add_sub")

        }
    }

    private fun initSubscriptionVp() {
        subscriptionAdapter = SubscriptionAdapter(object : SubscriptionAdapter.OnClickInterface {

            override fun onSubscriptionClicked(packages: GetSubscriptionResponse.Data) {
                val payload = JsonObject()
                payload.addProperty("subscriptionId", packages?.id)
                if (packages?.isActive == true) {
                    payload.addProperty("status", false)
                } else {
                    payload.addProperty("status", true)
                }
                profileViewModel.disableSubscription(payload)
            }

        })
        binding.subscriptionViewPager.adapter = subscriptionAdapter
        // You need to retain one page on each side so that the next and previous items are visible
        binding.subscriptionViewPager.offscreenPageLimit = 1

        // Add a PageTransformer that translates the next and previous items horizontally
        // towards the center of the screen, which makes them visible
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * Math.abs(position))
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }
        binding.subscriptionViewPager.setPageTransformer(pageTransformer)

        // The ItemDecoration gives the current (centered) item horizontal margin so that
        // it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.subscriptionViewPager.addItemDecoration(itemDecoration)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
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