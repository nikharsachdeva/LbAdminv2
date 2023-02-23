package com.laundrybuoy.admin.ui.profile

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.databinding.FragmentViewCouponDetailsBinding
import com.laundrybuoy.admin.model.profile.GetCouponsResponse
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.makeButtonDisabled
import com.laundrybuoy.admin.utils.makeButtonEnabled
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewCouponDetailsFragment : BaseBottomSheet() {

    private var _binding: FragmentViewCouponDetailsBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    var couponObjectReceived: GetCouponsResponse.Data? = null

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

    companion object {
        private const val COUPON_OBJECT = "COUPON_OBJECT"
        fun newInstance(
            couponObject: GetCouponsResponse.Data?,
        ): ViewCouponDetailsFragment {
            val viewFragment = ViewCouponDetailsFragment()
            val args = Bundle()
            args.putParcelable(COUPON_OBJECT, couponObject)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        couponObjectReceived = arguments?.getParcelable<GetCouponsResponse.Data>(
            COUPON_OBJECT
        )
        setUI(couponObjectReceived)
        initObserver()
        onClick()
    }

    private fun initObserver() {
        profileViewModel.disableCouponLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.editCouponBtn.makeButtonDisabled()
                }
                is NetworkResult.Success -> {
                    binding.editCouponBtn.makeButtonEnabled()
                    getMainActivity()?.showSnackBar(it.data?.message)
                    if (::callback.isInitialized) {
                        dialog?.dismiss()
                        callback.invoke()
                    }
                    /*
                    if (it.data?.success == true) {
                        getMainActivity()?.showSnackBar(it.data?.message)
                        if (::callback.isInitialized) {
                            dialog?.dismiss()
                            callback.invoke()
                        }
                    }

                     */
                }
                is NetworkResult.Error -> {
                    binding.editCouponBtn.makeButtonEnabled()
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })
    }

    private fun setUI(it: GetCouponsResponse.Data?) {
        if(it!=null){

            binding.couponNameTiet.setText(it.name?:"")
            binding.couponDescTiet.setText(it.description?:"")
            when (it.isActive) {
                true -> {
                    binding.couponActiveStsTv.text = "Enabled"
                    binding.couponActiveStsTv.setTextColor(
                        ColorStateList.valueOf(
                            Color.parseColor(
                                "#30b856"
                            )
                        )
                    )
                    binding.couponActiveStsTv.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#1030b856"))

                    binding.editCouponBtn.text="Disable Coupon"
                    binding.editCouponBtn.setTextColor(
                        ColorStateList.valueOf(
                            Color.parseColor(
                                "#fc2254"
                            )
                        )
                    )

                }
                false -> {
                    binding.couponActiveStsTv.text = "Disabled"
                    binding.couponActiveStsTv.setTextColor(
                        ColorStateList.valueOf(
                            Color.parseColor(
                                "#fc2254"
                            )
                        )
                    )
                    binding.couponActiveStsTv.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#10fc2254"))

                    binding.editCouponBtn.text="Enable Coupon"
                    binding.editCouponBtn.setTextColor(
                        ColorStateList.valueOf(
                            Color.parseColor(
                                "#30b856"
                            )
                        )
                    )
                }
                else -> {
                    binding.couponActiveStsTv.text = ""
                }
            }
            when(it.quantityType){
                "cloth"->{
                    binding.radioCloth.isChecked=true
                }
                "kg"->{
                    binding.radioKg.isChecked=true
                }
            }

            binding.discountTiet.setText(it.discountPercentage.toString())
            binding.minQtyTiet.setText(it.minQuantity.toString())
            binding.maxDiscountTiet.setText(it.maxDiscount.toString())

        }
    }

    private fun onClick() {

        binding.backFromCouponIv.setOnClickListener {
            dialog?.dismiss()
        }

        binding.editCouponBtn.setOnClickListener {
            val payload = JsonObject()
            payload.addProperty("couponId", couponObjectReceived?.id)
            if(couponObjectReceived?.isActive==true){
                payload.addProperty("status", 0)
            }else{
                payload.addProperty("status", 1)
            }
            profileViewModel.disableCoupon(payload)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentViewCouponDetailsBinding.inflate(inflater, container, false)
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