package com.laundrybuoy.admin.ui.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentCustomerPackageBinding
import com.laundrybuoy.admin.databinding.FragmentCustomerSubscriptionBinding

class CustomerPackageFragment : BaseFragment() {


    private var _binding: FragmentCustomerPackageBinding? = null
    private val binding get() = _binding!!
    var customerIdReceived: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerPackageBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val CUSTOMER_ID = "CUSTOMER_ID"

        fun newInstance(
            customerId: String,
        ): CustomerPackageFragment {
            val viewFragment = CustomerPackageFragment()
            val args = Bundle()
            args.putString(CUSTOMER_ID, customerId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerIdReceived = arguments?.getString(CUSTOMER_ID)

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setBottomNav()
        }
    }

    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(true)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}