package com.laundrybuoy.admin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.databinding.FragmentSplashBinding
import com.laundrybuoy.admin.model.splash.SplashDataModel
import com.laundrybuoy.admin.ui.order.OrdersFragment
import com.laundrybuoy.admin.ui.order.OrdersRootFragment
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.SharedPreferenceManager
import com.laundrybuoy.admin.viewmodel.AuthViewModel
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNav()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()

        if (SharedPreferenceManager.getBearerToken().isNullOrBlank()){
            lifecycleScope.launch(Dispatchers.Main) {
                delay(1000)
                getMainActivity()!!.addFragment(
                    false,
                    1,
                    LoginFragment()
                )
            }

        }else{
            fetchSplashData()
        }
    }

    private fun proceedFurther() {
        getMainActivity()!!.addFragment(
            false,
            1,
            OrdersFragment()
        )
    }

    private fun fetchSplashData() {
        authViewModel.getSplashDataApi()
    }

    private fun initObserver() {

        authViewModel.splashDataLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {

                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.data != null && it.data.success == true) {
                        saveDataToDb(it.data.data)
                        proceedFurther()
                    } else {
                        getMainActivity()?.showSnackBar(it.data?.message)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }

        })

    }

    private fun saveDataToDb(data: SplashDataModel.SplashData) {
        lifecycleScope.launch {
            authViewModel.saveSplashDataDb(data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setBottomNav()
        }
    }

    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(false)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}