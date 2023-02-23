package com.laundrybuoy.admin.ui.rider

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.databinding.FragmentRiderUpdateBinding
import com.laundrybuoy.admin.ui.vendor.VendorUpdateFragment
import com.laundrybuoy.admin.utils.*
import com.laundrybuoy.admin.viewmodel.RiderViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable

const val SCREEN_TYPE = "SCREEN_TYPE"
const val RIDER_ID = "RIDER_ID"
const val SCREEN_DATA = "SCREEN_DATA"
const val NAME = "name"
const val ALTERNATE_NUMBER = "alternateNumber"
const val PRIMARY_NUMBER = "primaryNumber"



@AndroidEntryPoint
class RiderUpdateFragment : BaseBottomSheet() {

    private var _binding: FragmentRiderUpdateBinding? = null
    private val binding get() = _binding!!
    val compositeDisposable = CompositeDisposable()

    private var screenTypeReceived: String? = null
    private var screenDataReceived: String? = null
    var riderIdReceived: String? = null
    private val riderViewModel by viewModels<RiderViewModel>()

    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        @JvmStatic
        fun newInstance(
            riderIdReceived: String,
            screenType: String,
            screenObj: String,
        ) =
            RiderUpdateFragment().apply {
                arguments = Bundle().apply {
                    putString(RIDER_ID, riderIdReceived)
                    putString(SCREEN_TYPE, screenType)
                    putString(SCREEN_DATA, screenObj)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screenTypeReceived = arguments?.getString(SCREEN_TYPE)
        screenDataReceived = arguments?.getString(SCREEN_DATA)
        riderIdReceived = arguments?.getString(RIDER_ID)

        initObserver()
        onClicks()
        setUIAccordingly(screenTypeReceived, screenDataReceived)

    }

    private fun initObserver() {
        when (screenTypeReceived) {

            ALTERNATE_NUMBER, PRIMARY_NUMBER -> {
                compositeDisposable.add(
                    RxTextView.textChanges(binding.enterNumberEt)
                        .subscribe {
                            if (it.trim().length == 10) {
                                binding.updateProfileBtn.makeButtonEnabled()
                            } else {
                                binding.updateProfileBtn.makeButtonDisabled()
                            }
                        })
            }
            NAME -> {
                compositeDisposable.add(
                    RxTextView.textChanges(binding.enterNameEt)
                        .subscribe {
                            if (it.trim().isNotEmpty()) {
                                binding.updateProfileBtn.makeButtonEnabled()
                            } else {
                                binding.updateProfileBtn.makeButtonDisabled()
                            }
                        })
            }

        }

        riderViewModel.updateRiderLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        if (::callback.isInitialized) {
                            dialog?.dismiss()
                            callback.invoke()
                        }
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })
    }

    private fun setUIAccordingly(
        screenTypeReceived: String?,
        screenDataReceived: String?,
    ) {
        when (screenTypeReceived) {

            ALTERNATE_NUMBER -> {
                binding.updateProfileHeading.text = "Update Alternate Number"
                binding.updateAltNumberRl.makeViewVisible()
                binding.updateNameRl.makeViewGone()
                binding.enterNumberEt.setText(screenDataReceived.toString())
            }

            NAME ->{
                binding.updateProfileHeading.text = "Update Name"
                binding.updateAltNumberRl.makeViewGone()
                binding.updateNameRl.makeViewVisible()
                binding.enterNameEt.setText(screenDataReceived.toString())
            }

            PRIMARY_NUMBER -> {
                binding.updateProfileHeading.text = "Update Primary Number"
                binding.updateAltNumberRl.makeViewVisible()
                binding.updateNameRl.makeViewGone()
                binding.enterNumberEt.setText(screenDataReceived.toString())
            }

        }
    }

    private fun onClicks() {
        binding.closeUpdateProfileIv.setOnClickListener {
            dialog?.dismiss()
        }

        binding.updateProfileBtn.setOnClickListener {

            when (screenTypeReceived) {

                ALTERNATE_NUMBER -> {
                    if (binding.enterNumberEt.text.trim().length == 10) {
                        val numberPayload = JsonObject()
                        numberPayload.addProperty(
                            "altMobile",
                            (binding.enterNumberEt.text.toString())
                        )
                        riderViewModel.updateRider(riderIdReceived ?: "", numberPayload)

                    }
                }

                PRIMARY_NUMBER -> {
                    if (binding.enterNumberEt.text.trim().length == 10) {
                        val numberPayload = JsonObject()
                        numberPayload.addProperty(
                            "mobile",
                            (binding.enterNumberEt.text.toString())
                        )
                        riderViewModel.updateRider(riderIdReceived ?: "", numberPayload)

                    }
                }

                NAME ->{
                    if (binding.enterNameEt.text.trim().isNotEmpty()) {
                        val namePayload = JsonObject()
                        namePayload.addProperty(
                            "name",
                            (binding.enterNameEt.text.toString())
                        )
                        riderViewModel.updateRider(riderIdReceived ?: "", namePayload)

                    }
                }
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRiderUpdateBinding.inflate(inflater, container, false)
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
        compositeDisposable.dispose()
    }

}