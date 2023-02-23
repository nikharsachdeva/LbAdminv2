package com.laundrybuoy.admin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.SearchAdapter
import com.laundrybuoy.admin.databinding.FragmentSearchBottomSheetBinding
import com.laundrybuoy.admin.model.SearchModel
import com.laundrybuoy.admin.model.order.SearchResultModel
import com.laundrybuoy.admin.ui.vendor.VendorRootFragment
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable

@AndroidEntryPoint
class SearchBottomSheetFragment : BaseFragment() {


    private var _binding: FragmentSearchBottomSheetBinding? = null
    private val binding get() = _binding!!
    val compositeDisposable = CompositeDisposable()


    private lateinit var searchAdapter: SearchAdapter
    private val ordersViewModel by viewModels<OrdersViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBottomNav()
    }

    internal companion object {
        fun newInstance(): SearchBottomSheetFragment {
            return SearchBottomSheetFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.orderSearchEt.requestFocus()
        initObservers()
        initSearchRv()
        initTextListener()
//        searchAdapter.submitList(getDummySearchList())
    }

    private fun initObservers() {
        ordersViewModel.searchLiveData.observe(viewLifecycleOwner, Observer {

            binding.searchShimmerSl.visibility = View.VISIBLE
            binding.searchShimmerSl.startShimmer()
            binding.searchRv.visibility = View.GONE

            when (it) {
                is NetworkResult.Loading -> {
                    binding.searchShimmerSl.visibility = View.VISIBLE
                    binding.searchShimmerSl.startShimmer()
                    binding.searchRv.visibility = View.GONE

                }
                is NetworkResult.Success -> {
                    binding.searchShimmerSl.visibility = View.GONE
                    binding.searchRv.visibility = View.VISIBLE
                    binding.searchShimmerSl.stopShimmer()
                    searchAdapter.submitList(it.data?.data)
                }
                is NetworkResult.Error -> {
                    binding.searchShimmerSl.visibility = View.GONE
                    binding.searchShimmerSl.stopShimmer()
                    binding.searchRv.visibility = View.GONE
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })
    }

    private fun initTextListener() {

        compositeDisposable.add(
            RxTextView.textChanges(binding.orderSearchEt)
                .subscribe {
                    if (it.trim().length > 2) {
                        ordersViewModel.searchAll(it.trim().toString())
                    } else {
                        ordersViewModel.cancelSearchJob()
                        searchAdapter.submitList(emptyList())
                    }
                })

    }

    private fun initSearchRv() {
        searchAdapter =
            SearchAdapter(object : SearchAdapter.OnClickInterface {

                override fun onSearchClicked(searchItem: SearchResultModel.Data) {
                    getMainActivity()?.hideKeyboard()
                    getMainActivity()?.addFragment(
                        true,
                        getMainActivity()?.getVisibleFrame()!!,
                        VendorRootFragment()
                    )
                }

            })

        val layoutManagerFilter = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.searchRv.layoutManager = layoutManagerFilter
        binding.searchRv.adapter = searchAdapter
    }

    private fun getDummySearchList(): MutableList<SearchModel.SearchModelItem> {
        val list: MutableList<SearchModel.SearchModelItem> = arrayListOf()

        list.add(
            SearchModel.SearchModelItem(
                title = "#145",
                type = "order",
                id = "dsds"
            )
        )

        list.add(
            SearchModel.SearchModelItem(
                title = "Nikhar Sachdeva",
                type = "customer",
                id = "dsdds"
            )
        )

        list.add(
            SearchModel.SearchModelItem(
                title = "Mayank Singh",
                type = "delivery",
                id = "sdsdd2"
            )
        )

        list.add(
            SearchModel.SearchModelItem(
                title = "Ritvi Sachdeva",
                type = "partner",
                id = "sd92u99"
            )
        )



        return list

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