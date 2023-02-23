package com.laundrybuoy.admin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.SnapHelper
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.onboardingstatus.OnboardingFilterAdapter
import com.laundrybuoy.admin.adapter.unapprovedvendors.RecyclerViewPaginator
import com.laundrybuoy.admin.adapter.unapprovedvendors.UnapprovedVendorsAdapter
import com.laundrybuoy.admin.databinding.FragmentOnboardingStatusVendorsBinding
import com.laundrybuoy.admin.model.unapprovedvendors.OnboardingFilterModel
import com.laundrybuoy.admin.model.unapprovedvendors.UnApprovedVendorsModel
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingVendorStatusFragment : BaseFragment() {

    private var _binding: FragmentOnboardingStatusVendorsBinding? = null
    private val binding get() = _binding!!

    private lateinit var onboardingFilterAdapter: OnboardingFilterAdapter
    private lateinit var unapprovedPartnerAdapter: UnapprovedVendorsAdapter

    private val homeViewModel by viewModels<HomePageViewModel>()

    private val startPage : Long = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initFilterRv()
        setFilterList()
        initApplicationsRv()
    }

    private fun initApplicationsRv() {
        unapprovedPartnerAdapter = UnapprovedVendorsAdapter(object : UnapprovedVendorsAdapter.OnClickInterface {
            override fun onItemClicked(partner: UnApprovedVendorsModel.Data.Partner) {
                getMainActivity()?.showSnackBar(partner.name?:"")
            }
        })

        binding.onBoardingVendorsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.onBoardingVendorsRv.adapter = unapprovedPartnerAdapter

        binding.onBoardingVendorsRv.addOnScrollListener(object : RecyclerViewPaginator(binding.onBoardingVendorsRv) {
            override val isLastPage: Boolean
                get() = homeViewModel.isLastPage()

            override fun loadMore(start: Long, count: Long) {
                homeViewModel.fetchUnapprovedVendors(start)
            }

        })

    }

    private fun initObserver() {

        homeViewModel.unapprovedVendorsLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.status == true) {
                        unapprovedPartnerAdapter.submitList(it.data.data?.partners)
                    }else{
                        getMainActivity()?.showSnackBar(it.data?.message)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })


    }

    private fun setFilterList() {
        val filterList = arrayListOf<OnboardingFilterModel.OnboardingFilterModelItem>()
        filterList.add(
            OnboardingFilterModel.OnboardingFilterModelItem(
                "#fffaea",
                "Incomplete Applications"
            )
        )
        filterList.add(
            OnboardingFilterModel.OnboardingFilterModelItem(
                "#d8eaff",
                "Pending Applications"
            )
        )
        filterList.add(
            OnboardingFilterModel.OnboardingFilterModelItem(
                "#c1e1c1",
                "Approved Applications"
            )
        )
        filterList.add(
            OnboardingFilterModel.OnboardingFilterModelItem(
                "#ffeae7",
                "Rejected Application"
            )
        )
        onboardingFilterAdapter.submitList(filterList)

    }

    private fun initFilterRv() {

        onboardingFilterAdapter =
            OnboardingFilterAdapter(object : OnboardingFilterAdapter.OnClickInterface {
                override fun onFilterClicked(filter: OnboardingFilterModel.OnboardingFilterModelItem) {

                    homeViewModel.fetchUnapprovedVendors(startPage)

                }
            })
        binding.onBoardingFilterRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.onBoardingFilterRv.adapter = onboardingFilterAdapter
        (binding.onBoardingFilterRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false

        binding.onBoardingFilterRv
            .viewTreeObserver
            .addOnGlobalLayoutListener(
                object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        // At this point the layout is complete and the
                        // dimensions of recyclerView and any child views
                        // are known.
                        binding.onBoardingFilterRv.findViewHolderForAdapterPosition(0)?.itemView?.performClick()

                        binding.onBoardingFilterRv
                            .viewTreeObserver
                            .removeOnGlobalLayoutListener(this)
                    }
                })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingStatusVendorsBinding.inflate(inflater, container, false)
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