package com.laundrybuoy.admin.ui.profile

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentAddPackageBinding
import com.laundrybuoy.admin.databinding.FragmentAddSubscriptionBinding
import com.laundrybuoy.admin.model.profile.AddPackagePayload
import com.laundrybuoy.admin.model.profile.AddSubscriptionPayload
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.makeButtonDisabled
import com.laundrybuoy.admin.utils.makeButtonEnabled
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSubscriptionFragment : BaseBottomSheet() {

    private var _binding: FragmentAddSubscriptionBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    private var selectedSubType: String? = null

    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.pushUpdatedBenefitList(arrayListOf())
        initObserver()
        onClick()

    }

    private fun createTagChip(context: Context, chipName: String): Chip {
        Log.d("chipName--", "createTagChip: " + chipName)
        return Chip(context).apply {
            text = chipName
            setChipBackgroundColorResource(R.color.profileHeading)
            isCloseIconVisible = true
            setTextColor(ContextCompat.getColor(context, R.color.hintColor))
            setTextAppearance(R.style.ChipTextAppearance)
            setOnCloseIconClickListener {
                binding.benefitChipGroup.removeView(this as View)
                val currentList = profileViewModel.benefitArray.value
                currentList?.remove(chipName)
                profileViewModel.pushUpdatedBenefitList(currentList?.toMutableList()
                    ?: arrayListOf())

            }

        }

    }


    private fun initObserver() {

        profileViewModel.benefitArray.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                binding.addSubsBtn.makeButtonDisabled()
            } else {

                binding.benefitChipGroup.removeAllViews()
                it.forEach {
                    binding.benefitChipGroup.addView(createTagChip(requireContext(), it))
                }
                binding.addSubsBtn.makeButtonEnabled()
            }
        })

        profileViewModel.addSubscriptionLiveData.observe(viewLifecycleOwner, Observer {
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
        binding.radioGroupSubType.setOnCheckedChangeListener { radioGroup, checkedId ->

            when (checkedId) {
                R.id.radioCloth -> {
                    selectedSubType = "cloth"
                }

                R.id.radioKg -> {
                    selectedSubType = "weight"
                }

            }

        }

        binding.addPointIv.setOnClickListener {
            if (binding.pointTiet.text.toString().isNotEmpty()) {
                val currentList = profileViewModel.benefitArray.value
                currentList?.add(binding.pointTiet.text.toString())
                profileViewModel.pushUpdatedBenefitList(currentList?.toMutableList()
                    ?: arrayListOf())
                binding.pointTiet.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Please type something.", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        binding.addSubsBtn.setOnClickListener {
            if (isValid()) {
                var subPayload = AddSubscriptionPayload(
                    description = binding.subsDescTiet.text.toString(),
                    details = profileViewModel.benefitArray.value,
                    actualPrice = binding.priceTiet.text.toString().toDouble(),
                    discountedPrice = binding.discountTiet.text.toString().toDouble(),
                    name = binding.subsNameTiet.text.toString(),
                    validity = binding.validityTiet.text.toString().toInt(),
                    quantity = binding.quantityTiet.text.toString().toDouble(),
                    quantityType = selectedSubType
                )
                profileViewModel.addSubscription(subPayload)
            }
        }
    }

    private fun isValid(): Boolean {
        var valid = true

        if (binding.subsNameTiet.text.toString().isNullOrEmpty()) {
            valid = false
            Toast.makeText(requireContext(), "Package name is required", Toast.LENGTH_SHORT).show()
        }

        if (binding.subsDescTiet.text.toString().isNullOrEmpty()) {
            valid = false
            Toast.makeText(requireContext(), "Package description is required", Toast.LENGTH_SHORT)
                .show()
        }

        if (binding.validityTiet.text.toString().isNullOrEmpty()) {
            valid = false
            Toast.makeText(requireContext(), "Validity is required", Toast.LENGTH_SHORT).show()
        }

        if (binding.priceTiet.text.toString().isNullOrEmpty()) {
            valid = false
            Toast.makeText(requireContext(), "Price is required", Toast.LENGTH_SHORT).show()
        }

        if (binding.discountTiet.text.toString().isNullOrEmpty()) {
            valid = false
            Toast.makeText(requireContext(), "Discounted price is required", Toast.LENGTH_SHORT)
                .show()
        }

        if (binding.quantityTiet.text.toString().isNullOrEmpty()) {
            valid = false
            Toast.makeText(requireContext(), "Quantity is required", Toast.LENGTH_SHORT)
                .show()
        }

        if (profileViewModel.benefitArray.value.isNullOrEmpty()) {
            valid = false
            Toast.makeText(requireContext(), "Please add at least one benefit", Toast.LENGTH_SHORT)
                .show()
        }

        if (selectedSubType.isNullOrEmpty()) {
            valid = false
            Toast.makeText(requireContext(), "Please select subscription type", Toast.LENGTH_SHORT)
                .show()
        }

        return valid
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddSubscriptionBinding.inflate(inflater, container, false)
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