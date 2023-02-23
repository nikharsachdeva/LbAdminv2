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
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.CouponFilterAdapter
import com.laundrybuoy.admin.adapter.PackagesAdapter
import com.laundrybuoy.admin.databinding.FragmentPackageBinding
import com.laundrybuoy.admin.model.profile.GetPackagesResponse
import com.laundrybuoy.admin.utils.HorizontalMarginItemDecoration
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.abs

@AndroidEntryPoint
class PackageFragment : BaseFragment() {

    private var _binding: FragmentPackageBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    lateinit var packageFilterAdapter: CouponFilterAdapter
    private lateinit var packagesAdapter: PackagesAdapter
    var currentSelectedFilter = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserver()

        try {
            binding.packageFiltersRv
                .viewTreeObserver
                .addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            // At this point the layout is complete and the
                            // dimensions of recyclerView and any child views
                            // are known.
                            binding.packageFiltersRv.findViewHolderForAdapterPosition(0)?.itemView?.performClick()

                            binding.packageFiltersRv
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
        initPackageRv()
    }

    private fun initObserver() {

        profileViewModel.getPackagesLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        setPackagesData(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

        profileViewModel.disablePackageLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        getPackages(currentSelectedFilter)

                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

    }

    private fun setPackagesData(data: List<GetPackagesResponse.Data?>?) {
        if (data.isNullOrEmpty()) {
            binding.packageViewPager.makeViewGone()
            binding.packageUnavailable.makeViewVisible()
        } else {
            binding.packageViewPager.makeViewVisible()
            binding.packageUnavailable.makeViewGone()
            packagesAdapter.submitList(data)

        }
        binding.packageViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

    }


    private fun initFilterRv() {
        packageFilterAdapter = CouponFilterAdapter(object : CouponFilterAdapter.OnClickInterface {
            override fun onFilterSelected(filter: String) {
                currentSelectedFilter = filter
                getPackages(filter)
            }


        })
        binding.packageFiltersRv.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.packageFiltersRv.adapter = packageFilterAdapter

        packageFilterAdapter.submitList(arrayListOf("all", "active", "inactive"))
    }

    private fun getPackages(filter: String) {
        profileViewModel.getAllPackages(filter)
    }

    private fun onClick() {
        binding.closePackageIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }

        binding.addPackIv.setOnClickListener {
            val addFrag = AddPackageFragment()
            addFrag.setCallback {
                getPackages(currentSelectedFilter)
            }
            addFrag.isCancelable = true
            addFrag.show(childFragmentManager, "add_package")

        }
    }

    private fun initPackageRv() {
        packagesAdapter = PackagesAdapter(object : PackagesAdapter.OnClickInterface {
            override fun onPackageClicked(packages: GetPackagesResponse.Data) {

                val payload = JsonObject()
                payload.addProperty("packageId", packages?.id)
                if (packages?.isActive == true) {
                    payload.addProperty("status", false)
                } else {
                    payload.addProperty("status", true)
                }
                profileViewModel.disablePackage(payload)

            }

        })
        binding.packageViewPager.adapter = packagesAdapter
        // You need to retain one page on each side so that the next and previous items are visible
        binding.packageViewPager.offscreenPageLimit = 1

        // Add a PageTransformer that translates the next and previous items horizontally
        // towards the center of the screen, which makes them visible
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * abs(position))
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }
        binding.packageViewPager.setPageTransformer(pageTransformer)

        // The ItemDecoration gives the current (centered) item horizontal margin so that
        // it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.packageViewPager.addItemDecoration(itemDecoration)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPackageBinding.inflate(inflater, container, false)
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