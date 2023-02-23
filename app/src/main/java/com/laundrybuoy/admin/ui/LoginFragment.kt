package com.laundrybuoy.admin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentHomeBinding
import com.laundrybuoy.admin.databinding.FragmentLoginBinding
import com.laundrybuoy.admin.model.auth.VerifyOtpResponse
import com.laundrybuoy.admin.ui.order.OrdersFragment
import com.laundrybuoy.admin.utils.*
import com.laundrybuoy.admin.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    val compositeDisposable = CompositeDisposable()
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClicks()
        initObservers()

    }

    private fun initObservers() {

        authViewModel.otpInfo.observe(viewLifecycleOwner, Observer { otpInfo ->
            resendOTPTimer()
            binding.otpNumberRl.makeViewVisible()
            binding.submitOtpBtn.makeViewVisible()
            binding.enterOtpWhatsappEt.setText(otpInfo.get("otp").asString)

            binding.submitOtpBtn.setOnClickListener {
                submitOtp(otpInfo.get("otp").asString,otpInfo.get("id").asString)
            }

        })

        compositeDisposable.add(
            RxTextView.textChanges(binding.enterPhoneWhatsappEt)
                .subscribe {
                    if (it.trim().length == 12) {
                        getMainActivity()?.hideKeyboard()
                        binding.submitWhatsappNumberBtn.makeButtonEnabled()
                    } else {
                        binding.submitWhatsappNumberBtn.makeButtonDisabled()
                    }
                })

        compositeDisposable.add(
            RxTextView.textChanges(binding.enterOtpWhatsappEt)
                .subscribe {
                    if (it.trim().length == 6) {
                        getMainActivity()?.hideKeyboard()
                        binding.submitOtpBtn.makeButtonEnabled()
                    } else {
                        binding.submitOtpBtn.makeButtonDisabled()
                    }
                })

        authViewModel.requestOTPLiveData.observe(viewLifecycleOwner, Observer {
            binding.submitWhatsappNumberBtn.makeButtonDisabled()
            binding.otpNumberRl.makeViewGone()
            binding.submitOtpBtn.makeViewGone()

            when (it) {
                is NetworkResult.Loading -> {
                    binding.submitWhatsappNumberBtn.makeButtonDisabled()
                    binding.otpNumberRl.makeViewGone()
                    binding.submitOtpBtn.makeViewGone()
                }
                is NetworkResult.Success -> {
                    binding.submitWhatsappNumberBtn.makeButtonEnabled()
                    if (it.data != null && it.data.success == true) {
                        val otpInfo = JsonObject()
                        otpInfo.addProperty("id", it.data.data?.id)
                        otpInfo.addProperty("otp", it.data.data?.otp)
                        authViewModel.setOtpInfo(otpInfo)
                    }
                }
                is NetworkResult.Error -> {
                    binding.submitWhatsappNumberBtn.makeButtonEnabled()
                    binding.otpNumberRl.makeViewGone()
                    binding.submitOtpBtn.makeViewGone()

                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })


        authViewModel.verifyOTPLiveData.observe(viewLifecycleOwner, Observer {
            binding.submitOtpBtn.makeButtonDisabled()

            when (it) {
                is NetworkResult.Loading -> {
                    binding.submitOtpBtn.makeButtonDisabled()
                }
                is NetworkResult.Success -> {
                    binding.submitOtpBtn.makeButtonEnabled()
                    if (it.data != null && it.data.success == true) {
                        setAdminLogin(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    binding.submitOtpBtn.makeButtonEnabled()
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })

    }

    private fun setAdminLogin(data: VerifyOtpResponse.Data?) {
        data?.token.let {
            if (!it.isNullOrBlank()) {
                SharedPreferenceManager.setBearerToken(it)
                getMainActivity()?.addFragment(false,
                    1,
                    OrdersFragment())
            } else {
                getMainActivity()?.showSnackBar("Null auth token received.")
            }
        }
    }

    private fun resendOTPTimer() {
        compositeDisposable.add(
            Observable.interval(1, TimeUnit.SECONDS)
                .take(30)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    binding.submitWhatsappNumberBtn.makeButtonEnabled()
                    binding.submitWhatsappNumberBtn.text = " Resend OTP"
                }
                .subscribe {
                    binding.submitWhatsappNumberBtn.text = " Resend in ${30 - it}s"
                    binding.submitWhatsappNumberBtn.makeButtonDisabled()
                })
    }

    private fun onClicks() {
        binding.submitWhatsappNumberBtn.setOnClickListener {
            requestOTP()
            binding.enterOtpWhatsappEt.text.clear()

        }

    }

    private fun submitOtp(otp: String, id: String) {

        if (id.isNotEmpty()) {
            val verifyPayload = JsonObject()
            verifyPayload.addProperty("otp", otp)
            verifyPayload.addProperty("_id", id)
            authViewModel.verifyOTP(verifyPayload)
        }
    }

    fun requestOTP() {
        val otpPayload = JsonObject()
        otpPayload.addProperty(
            "mobile",
            (binding.enterPhoneWhatsappEt.text.toString())
        )
        authViewModel.requestOTP(otpPayload)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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