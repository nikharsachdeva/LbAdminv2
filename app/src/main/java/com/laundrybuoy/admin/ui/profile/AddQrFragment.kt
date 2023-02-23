package com.laundrybuoy.admin.ui.profile

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.widget.RxTextView
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentAddQrBinding
import com.laundrybuoy.admin.model.profile.AddQrPayload
import com.laundrybuoy.admin.utils.*
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class AddQrFragment : BaseBottomSheet() {

    private var _binding: FragmentAddQrBinding? = null
    private val binding get() = _binding!!
    val compositeDisposable = CompositeDisposable()
    private val profileViewModel by viewModels<ProfileViewModel>()
    private val vendorViewModel by viewModels<VendorViewModel>()

    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserver()
        onClick()
        profileViewModel.setAddedQrImage(null)

    }

    private fun initObserver() {
        profileViewModel._addedQrImageLiveData.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                binding.addQrBtn.makeButtonDisabled()
                binding.deleteAddedQrIv.makeViewGone()
                binding.qrAddedCv.makeViewGone()
            } else {
                binding.addQrBtn.makeButtonEnabled()
                binding.deleteAddedQrIv.makeViewVisible()
                binding.qrAddedCv.makeViewVisible()
                binding.qrAddedIv.loadImageWithGlide(it)
            }
        })


        vendorViewModel.uploadDocLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        val tempUrl = it.data.data
                        profileViewModel.setAddedQrImage(tempUrl)
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

        profileViewModel.addQrLiveData.observe(viewLifecycleOwner, Observer {
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


        compositeDisposable.add(
            RxTextView.textChanges(binding.qrNameTiet)
                .subscribe {
                    if (it.trim().isNotEmpty()) {
                        binding.addQrBtn.makeButtonEnabled()
                    } else {
                        binding.addQrBtn.makeButtonDisabled()
                    }
                })

    }

    private fun onClick() {

        binding.deleteAddedQrIv.setOnClickListener {
            profileViewModel.setAddedQrImage(null)
        }

        binding.addQrBtn.setOnClickListener {
            if (isValid()) {
                var payload = AddQrPayload(
                    name = binding.qrNameTiet.text.toString(),
                    description = binding.qrDescTiet.text.toString(),
                    qrImage = profileViewModel._addedQrImageLiveData.value
                )
                profileViewModel.addQrCode(payload)
            } else {
                Toast.makeText(requireContext(),
                    "Mandatory items must be filled!",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.uploadQrCv.setOnClickListener {
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }

        binding.closeAdd.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun isValid(): Boolean {
        var valid = true
        if (binding.qrNameTiet.text?.trim().isNullOrEmpty()) {
            valid = false
        }

        if (profileViewModel._addedQrImageLiveData.value == null) {
            valid = false
        }

        return valid
    }

    private fun init() {

    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var grantedCount = 0;
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    ++grantedCount;
                } else {
                    showSnackbarToSetting()
                }

                if (grantedCount == 3) {
                    openImagePicker()
                }
            }
        }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(requireContext()) // optional usage
            uploadImageNew2(uriContent!!, uriFilePath)
        } else {
            val exception = result.error
            getMainActivity()?.showSnackBar(exception?.localizedMessage.toString())
        }
    }

    private fun uploadImageNew2(uriContent: Uri, uriFilePath: String?) {

        val tFile = File(uriFilePath)
        if (tFile != null) {
            val requestFile = tFile.asRequestBody(
                requireContext().contentResolver.getType(uriContent)?.toMediaTypeOrNull()
            )
            val body = MultipartBody.Part.createFormData("photoId", tFile.name, requestFile)
            vendorViewModel.uploadDocNew2(body)
        } else {
            getMainActivity()?.showSnackBar("Empty File")
        }

    }

    private fun openImagePicker() {
        cropImage.launch(
            options {
                setImageSource(
                    includeGallery = true,
                    includeCamera = true,
                )
                // Normal Settings
                setScaleType(CropImageView.ScaleType.FIT_CENTER)
                setCropShape(CropImageView.CropShape.RECTANGLE)
                setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                setAspectRatio(1, 1)
                setMaxZoom(4)
                setAutoZoomEnabled(true)
                setMultiTouchEnabled(true)
                setCenterMoveEnabled(true)
                setShowCropOverlay(true)
                setAllowFlipping(true)
                setSnapRadius(3f)
                setTouchRadius(48f)
                setInitialCropWindowPaddingRatio(0.1f)
                setBorderLineThickness(3f)
                setBorderLineColor(Color.argb(170, 255, 255, 255))
                setBorderCornerThickness(2f)
                setBorderCornerOffset(5f)
                setBorderCornerLength(14f)
                setBorderCornerColor(Color.WHITE)
                setGuidelinesThickness(1f)
                setGuidelinesColor(R.color.white)
                setBackgroundColor(Color.argb(119, 0, 0, 0))
                setMinCropWindowSize(24, 24)
                setMinCropResultSize(20, 20)
                setMaxCropResultSize(99999, 99999)
                setActivityTitle("")
                setActivityMenuIconColor(0)
                setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                setOutputCompressQuality(90)
                setRequestedSize(0, 0)
                setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                setInitialCropWindowRectangle(null)
                setInitialRotation(0)
                setAllowCounterRotation(false)
                setFlipHorizontally(false)
                setFlipVertically(false)
                setCropMenuCropButtonTitle(null)
                setCropMenuCropButtonIcon(0)
                setAllowRotation(true)
                setNoOutputImage(false)
                setFixAspectRatio(false)

            }
        )
    }


    private fun showSnackbarToSetting() {

        Snackbar.make(
            binding.rootAddQrRl,
            "Permission blocked",
            Snackbar.LENGTH_LONG
        ).setAction("Settings") {
            // Responds to click on the action
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri: Uri =
                Uri.fromParts("package", getMainActivity()?.applicationContext?.packageName, null)
            intent.data = uri
            startActivity(intent)
        }.show()

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
        _binding = FragmentAddQrBinding.inflate(inflater, container, false)
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