package com.laundrybuoy.admin.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentAddCouponBinding
import com.laundrybuoy.admin.model.profile.AddCouponPayload
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCouponFragment : BaseBottomSheet() {

    private var _binding: FragmentAddCouponBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    private var selectedQuantityType: String? = null

    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        onClick()
    }

    private fun initObserver() {
        profileViewModel.addCouponLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true && it.data.data?.id != null) {
                        if (::callback.isInitialized) {
                            dialog?.dismiss()
                            callback.invoke()
                        }
                    } else {
                        Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })
    }

    private fun onClick() {


        binding.backFromCouponIv.setOnClickListener {
            dialog?.dismiss()
        }

        binding.radioGroupCouponType.setOnCheckedChangeListener { radioGroup, checkedId ->

            when (checkedId) {
                R.id.radioCloth -> {
                    selectedQuantityType = "cloth"
                }

                R.id.radioKg -> {
                    selectedQuantityType = "kg"
                }

            }

        }


        binding.addCouponBtn.setOnClickListener {
            if (isValid()) {
                var payload = AddCouponPayload(
                    name = binding.couponNameTiet.text.toString(),
                    description = binding.couponDescTiet.text.toString(),
                    minQuantity = binding.minQtyTiet.text.toString().toDouble(),
                    maxDiscount = binding.maxDiscountTiet.text.toString().toDouble(),
                    discountPercentage = binding.discountTiet.text.toString().toDouble(),
                    quantityType = selectedQuantityType

                )
                profileViewModel.addCoupon(payload)
            } else {
                Toast.makeText(requireContext(),
                    "All items must be filled!",
                    Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun isValid(): Boolean {

        var valid = true
        if (binding.couponNameTiet.text?.trim().isNullOrEmpty()) {
            valid = false
        }

        if (binding.couponDescTiet.text?.trim().isNullOrEmpty()) {
            valid = false
        }

        if (binding.discountTiet.text?.trim().isNullOrEmpty()) {
            valid = false
        }

        if (binding.minQtyTiet.text?.trim().isNullOrEmpty()) {
            valid = false
        }

        if (binding.maxDiscountTiet.text?.trim().isNullOrEmpty()) {
            valid = false
        }

        if (selectedQuantityType == null) {
            valid = false
        }


        return valid

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddCouponBinding.inflate(inflater, container, false)
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