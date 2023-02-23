package com.laundrybuoy.admin.ui.vendor

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.UploadDocsAdapter
import com.laundrybuoy.admin.databinding.FragmentVendorDocsBinding
import com.laundrybuoy.admin.model.UploadedDocsModel
import com.laundrybuoy.admin.model.vendor.DocsPayload
import com.laundrybuoy.admin.model.vendor.WorkingPincodePayload
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.makeButtonDisabled
import com.laundrybuoy.admin.utils.makeButtonEnabled
import com.laundrybuoy.admin.viewmodel.RiderViewModel
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class VendorDocsFragment : BaseBottomSheet() {

    private var _binding: FragmentVendorDocsBinding? = null
    private val binding get() = _binding!!
    private val vendorViewModel by viewModels<VendorViewModel>()
    private val riderViewModel by viewModels<RiderViewModel>()
    private lateinit var docAdapter: UploadDocsAdapter
    var vendorDocList: MutableList<UploadedDocsModel.UploadedDocsModelItem> = arrayListOf()
    private var screenTypeReceived: String? = null
    private var screenDataReceived: ArrayList<String>? = null
    var vendorIdReceived: String? = null


    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screenTypeReceived = arguments?.getString(SCREEN_TYPE)
        screenDataReceived = arguments?.getStringArrayList(SCREEN_OBJ)
        vendorIdReceived = arguments?.getString(VENDOR_ID)

        init()
        onClick()
        initObserver()

        screenDataReceived.let {
            vendorDocList.clear()
            it?.map {
                vendorDocList.add(
                    UploadedDocsModel.UploadedDocsModelItem(
                        "-111",
                        it,
                        "Uploaded"
                    )
                )
            }

            vendorViewModel.setVendorDocs(vendorDocList)
        }
    }

    private fun initObserver() {

        vendorViewModel._vendorDocLiveData.observe(viewLifecycleOwner, Observer {
            docAdapter.submitList(it)
            docAdapter.notifyDataSetChanged()

            if (!it.isNullOrEmpty()) {
                binding.submitVendorDocsBtn.makeButtonEnabled()
            } else {
                binding.submitVendorDocsBtn.makeButtonDisabled()
            }
        })

        vendorViewModel.uploadDocLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        val tempUrl = it.data.data
                        vendorDocList.add(
                            UploadedDocsModel.UploadedDocsModelItem(
                                "-111",
                                tempUrl,
                                "Uploaded"
                            )
                        )
                        vendorViewModel.setVendorDocs(vendorDocList)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })

        riderViewModel.uploadDocLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        val tempUrl = it.data.data
                        vendorDocList.add(
                            UploadedDocsModel.UploadedDocsModelItem(
                                "-111",
                                tempUrl,
                                "Uploaded"
                            )
                        )
                        vendorViewModel.setVendorDocs(vendorDocList)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)

                }
            }
        })



        vendorViewModel.updateVendorLiveData.observe(viewLifecycleOwner, Observer {
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


    private fun onClick() {
        binding.uploadLl.setOnClickListener {
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }

        binding.submitVendorDocsBtn.setOnClickListener {
            if (vendorViewModel._vendorDocLiveData.value?.size!! > 0) {
                val docsListAsString = vendorViewModel._vendorDocLiveData.value!!.map {
                    it.image
                }
                val docsPayload = DocsPayload(
                    docsListAsString
                )

                if(screenTypeReceived== VENDOR_DOCS){
                    vendorViewModel.updateVendor(vendorIdReceived ?: "",
                        Gson().fromJson(Gson().toJson(docsPayload), JsonObject::class.java))
                }else if(screenTypeReceived== RIDER_DOCS){
                    riderViewModel.updateRider(vendorIdReceived ?: "",
                        Gson().fromJson(Gson().toJson(docsPayload), JsonObject::class.java))
                }
            }
        }
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
            if(screenTypeReceived== VENDOR_DOCS){
                vendorViewModel.uploadDocNew2(body)
            }else if(screenTypeReceived== RIDER_DOCS){
                riderViewModel.uploadDocNew2(body)
            }
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
            binding.vendorDocRootRl,
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


    companion object {
        @JvmStatic
        fun newInstance(
            vendorIdReceived1: String?,
            screenType: String,
            screenObj: ArrayList<String>,
        ) =
            VendorDocsFragment().apply {
                arguments = Bundle().apply {
                    putString(VENDOR_ID, vendorIdReceived1)
                    putString(SCREEN_TYPE, screenType)
                    putStringArrayList(SCREEN_OBJ, screenObj)
                }
            }
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentVendorDocsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    private fun init() {
        binding.closeVendorDocsIv.setOnClickListener {
            dialog?.dismiss()
        }

        docAdapter = UploadDocsAdapter(object : UploadDocsAdapter.OnClickInterface {


            override fun onImageClicked(doc: UploadedDocsModel.UploadedDocsModelItem) {

            }

            override fun onDeleteClicked(
                position: Int,
                doc: UploadedDocsModel.UploadedDocsModelItem,
            ) {

                vendorDocList.remove(doc)
                vendorViewModel.setVendorDocs(vendorDocList)

            }


        })
        binding.uploadedDocsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.uploadedDocsRv.adapter = docAdapter

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