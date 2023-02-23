package com.laundrybuoy.admin.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.all.AllFilterAdapter
import com.laundrybuoy.admin.adapter.all.AllItemLoaderAdapter
import com.laundrybuoy.admin.adapter.all.AllItemPagingAdapter
import com.laundrybuoy.admin.databinding.FragmentVendorListBinding
import com.laundrybuoy.admin.model.profile.AllListingResponse
import com.laundrybuoy.admin.model.splash.SplashDataModel
import com.laundrybuoy.admin.ui.customer.CustomerRootFragment
import com.laundrybuoy.admin.ui.order.OrderDetailRootFragment
import com.laundrybuoy.admin.ui.rider.RiderRootFragment
import com.laundrybuoy.admin.ui.vendor.VendorRootFragment
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.AuthViewModel
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllListFragment : BaseFragment() {

    private var _binding: FragmentVendorListBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private var screenTypeReceived: String? = null
    private var screenSourceReceived: String? = null
    lateinit var allItemAdapter: AllItemPagingAdapter
    lateinit var filterAdapter: AllFilterAdapter
    val compositeDisposable = CompositeDisposable()

    private lateinit var callback: (AllListingResponse.Data.Partner?) -> Unit
    fun setCallback(callback: (AllListingResponse.Data.Partner?) -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNav()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        init()
        initObserver()
        onClick()
    }

    private fun initObserver() {
        authViewModel.splashDataDb.observe(viewLifecycleOwner, Observer {
            setFiltersUI(it)
        })

        profileViewModel.filterSelectedLD.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                fetchData(if (binding.searchItemEt.text.trim()
                        .isEmpty()
                ) null else binding.searchItemEt.text.trim().toString(), it)
            } else {
                fetchData(if (binding.searchItemEt.text.trim()
                        .isEmpty()
                ) null else binding.searchItemEt.text.trim().toString(), null)
            }
        })
    }

    private fun setFiltersUI(it: SplashDataModel.SplashData?) {
        when (screenTypeReceived) {
            "partner" -> {
                filterAdapter.submitList(it?.partner)
            }
            "rider" -> {
                filterAdapter.submitList(it?.rider)
            }
            "order" -> {
                filterAdapter.submitList(it?.order)
            }
            "user" -> {
                filterAdapter.submitList(it?.user)
            }
        }
    }

    private fun onClick() {
        binding.backFromAllListIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }
    }

    private fun initRv() {
        allItemAdapter =
            AllItemPagingAdapter(object : AllItemPagingAdapter.OnClickInterface {
                override fun onSelected(item: AllListingResponse.Data.Partner, position: Int) {
                    if (screenSourceReceived == "order_detail") {
                        if (screenTypeReceived == "rider") {
                            if (::callback.isInitialized) {
                                getMainActivity()?.onBackPressed()
                                callback.invoke(item)
                            }
                        }
                    } else if (screenSourceReceived == "profile") {
                        if (item.role == "order") {
                            getMainActivity()?.hideKeyboard()
                            val frag = OrderDetailRootFragment.newInstance(item.id ?: "")
                            getMainActivity()?.addFragment(
                                true,
                                getMainActivity()?.getVisibleFrame()!!,
                                frag
                            )
                        }else if(item.role=="partner"){
                            getMainActivity()?.hideKeyboard()
                            val frag = VendorRootFragment.newInstance(item.id ?: "")
                            getMainActivity()?.addFragment(true,getMainActivity()?.getVisibleFrame()!!,frag)
                        }
                        else if(item.role=="rider"){
                            getMainActivity()?.hideKeyboard()
                            val frag = RiderRootFragment.newInstance(item.id ?: "")
                            getMainActivity()?.addFragment(true,getMainActivity()?.getVisibleFrame()!!,frag)
                        }
                        else if(item.role=="user"){
                            getMainActivity()?.hideKeyboard()
                            val frag = CustomerRootFragment.newInstance(item.id ?: "")
                            getMainActivity()?.addFragment(true,getMainActivity()?.getVisibleFrame()!!,frag)
                        }

                    }

                }

            })

        binding.allItemsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.allItemsRv.setHasFixedSize(true)
        binding.allItemsRv.adapter = allItemAdapter.withLoadStateHeaderAndFooter(
            header = AllItemLoaderAdapter(),
            footer = AllItemLoaderAdapter()
        )
        allItemAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && allItemAdapter.itemCount < 1) {
                binding.itemsUnAvailableLl.makeViewVisible()
                binding.allItemsRv.makeViewGone()
            } else {
                binding.itemsUnAvailableLl.makeViewGone()
                binding.allItemsRv.makeViewVisible()
            }
        }


        filterAdapter = AllFilterAdapter(object : AllFilterAdapter.OnClickInterface {
            override fun onFilterSelected(filter: SplashDataModel.SplashData.FilterObj) {
                profileViewModel.setAllFilterSelected(filter.filterQuery.toString())
            }

        })
        binding.allFiltersRv.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.allFiltersRv.adapter = filterAdapter

    }

    private fun init() {
        screenTypeReceived = arguments?.getString(SCREEN_TYPE)
        screenSourceReceived = arguments?.getString(SCREEN_SOURCE)

        if (!screenTypeReceived.isNullOrBlank()) {
            binding.allItemHeadingTv.text = screenTypeReceived?.capitalize()
            binding.searchItemEt.hint = "${screenTypeReceived?.capitalize()} Name Here"
            binding.itemUnavailableTv.text = "No ${screenTypeReceived?.capitalize()} Available"
//            fetchData(null, null)
        } else {
            getMainActivity()?.onBackPressed()
        }

        compositeDisposable.add(
            RxTextView.textChanges(binding.searchItemEt)
                .subscribe {
                    if (it.trim().length > 1) {
                        fetchData(it.trim().toString(), profileViewModel.filterSelectedLD.value)
                    } else if (it.trim().isEmpty()) {
                        fetchData(null, profileViewModel.filterSelectedLD.value)
                    }
                })
    }

    private fun fetchData(searchValue: String? = null, filterSelected: String? = null) {
        viewLifecycleOwner.lifecycleScope.launch {
            profileViewModel.getAllItems(screenTypeReceived ?: "", searchValue, filterSelected)
                .observe(viewLifecycleOwner) {
                    if (it != null) {
                        authViewModel.getSplashDataDb()
                        allItemAdapter.submitData(lifecycle, it)
                    }
                }
        }

    }

    companion object {
        private const val SCREEN_TYPE = "SCREEN_TYPE"
        private const val SCREEN_SOURCE = "SCREEN_SOURCE"
        fun newInstance(
            screenType: String,
            screenSource: String,
        ): AllListFragment {
            val allListFragment = AllListFragment()
            val args = Bundle()
            args.putString(SCREEN_TYPE, screenType)
            args.putString(SCREEN_SOURCE, screenSource)
            allListFragment.arguments = args
            return allListFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentVendorListBinding.inflate(inflater, container, false)
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
        compositeDisposable.dispose()
    }

}