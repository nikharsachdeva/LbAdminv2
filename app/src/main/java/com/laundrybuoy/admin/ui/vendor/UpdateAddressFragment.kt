package com.laundrybuoy.admin.ui.vendor

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentUpdateAddressBinding
import com.laundrybuoy.admin.model.vendor.PersonalAddressModelRoot
import com.laundrybuoy.admin.model.vendor.WorkAddressModelRoot
import com.laundrybuoy.admin.model.vendor.VendorProfileModel
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.viewmodel.RiderViewModel
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateAddressFragment : BaseFragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentUpdateAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var placesClient: PlacesClient? = null
    var currentLatitudeLongitude: LatLng? = null
    var currentAddressModel: VendorAddressModel? = null
    var vendorIdReceived: String? = null
    var addressTypeReceived: String? = null
    var userTypeReceived: String? = null

    private val vendorViewModel by viewModels<VendorViewModel>()
    private val riderViewModel by viewModels<RiderViewModel>()

    private lateinit var callback: () -> Unit
    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    companion object {
        fun newInstance(
            addressType: String?,
            vendorIdReceived1: String?,
            screenObj: ArrayList<String>,
            userType: String,
        ) =
            UpdateAddressFragment().apply {
                arguments = Bundle().apply {
                    putString(ADDRESS_TYPE, addressType)
                    putString(VENDOR_ID, vendorIdReceived1)
                    putStringArrayList(SCREEN_OBJ, screenObj)
                    putString(USER_TYPE, userType)
                }
            }
    }

    private fun showSnackbarToSetting() {

        Snackbar.make(
            binding.updateWorkSpaceRoot,
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vendorIdReceived = arguments?.getString(VENDOR_ID)
        addressTypeReceived = arguments?.getString(ADDRESS_TYPE)
        userTypeReceived = arguments?.getString(USER_TYPE)

        initMap()
        initObservers()
        initBtmSht()
        setupAutocompletePlace()
        onClicks()

    }

    private fun initObservers() {
        vendorViewModel.updateVendorLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        getMainActivity()?.showSnackBar(it.data.message)
                        if (::callback.isInitialized) {
                            getMainActivity()?.onBackPressed()
                            callback.invoke()
                        }
                    }
                }
                is NetworkResult.Error -> {
                }
            }
        })

        riderViewModel.updateRiderLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.success == true) {
                        getMainActivity()?.showSnackBar(it.data.message)
                        if (::callback.isInitialized) {
                            getMainActivity()?.onBackPressed()
                            callback.invoke()
                        }
                    }
                }
                is NetworkResult.Error -> {
                }
            }
        })
    }

    private fun initBtmSht() {
        bottomSheetBehavior =
            BottomSheetBehavior.from<RelativeLayout>(binding.updateWorkAddressBtmsht.persistentBottomSheet)

        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, state: Int) {
                print(state)
                when (state) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.updateWorkAddressBtmsht.showRouteIndicatorIv.makeViewVisible()
                        binding.updateWorkAddressBtmsht.closeRouteBtmShtIv.makeViewGone()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.updateWorkAddressBtmsht.showRouteIndicatorIv.makeViewGone()
                        binding.updateWorkAddressBtmsht.closeRouteBtmShtIv.makeViewVisible()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.updateWorkAddressBtmsht.showRouteIndicatorIv.makeViewVisible()
                        binding.updateWorkAddressBtmsht.closeRouteBtmShtIv.makeViewGone()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        bottomSheetBehavior.maxHeight = getMainActivity()?.getDeviceHeight()?.minus(100)!!

        binding.updateWorkAddressBtmsht.closeRouteBtmShtIv.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }

    }

    private fun setupAutocompletePlace() {
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        val field: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS_COMPONENTS
        )

        autocompleteFragment.setPlaceFields(field);
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val latlng = place.latLng
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17f))
            }

            override fun onError(status: Status) {
                Toast.makeText(requireContext(),
                    "Search Cancelled ${status.statusMessage}",
                    Toast.LENGTH_SHORT).show()
            }
        })

        val hintTxt =
            (autocompleteFragment.view?.findViewById(R.id.places_autocomplete_search_input) as EditText)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val face = requireContext().resources.getFont(R.font.lexend_regular)
            hintTxt.typeface =
                face
        }
        hintTxt.textSize =
            18f

        val hintImg =
            (autocompleteFragment.view?.findViewById(R.id.places_autocomplete_search_button) as ImageView)
        hintImg.setImageResource(R.drawable.quantum_ic_search_grey600_24)
        hintImg.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#8da0bb"))

    }

    private fun onClicks() {
        binding.closeUpdateWorkspaceIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }

        binding.updateWorkAddressBtmsht.updateWorkAddressBtn.setOnClickListener {
            if (isValid()) {
                updateWorkAddress()
            } else {
                Toast.makeText(requireContext(), "All fields are required.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun updateWorkAddress() {
        if (addressTypeReceived == "workAddress") {
            val payload = VendorProfileModel.Data.WorkAddress(
                city = binding.updateWorkAddressBtmsht.cityTiet.text.toString(),
                landmark = binding.updateWorkAddressBtmsht.landmarkTiet.text.toString(),
                pin = binding.updateWorkAddressBtmsht.pincodeTiet.text.toString(),
                state = binding.updateWorkAddressBtmsht.stateTiet.text.toString(),
                lattitude = currentLatitudeLongitude?.latitude.toString(),
                longitude = currentLatitudeLongitude?.longitude.toString(),
                line1 = binding.updateWorkAddressBtmsht.addressLine1Tiet.text.toString()
            )

            val addressModelRoot = WorkAddressModelRoot(
                payload
            )
            vendorViewModel.updateVendor(vendorIdReceived ?: "",
                Gson().fromJson(Gson().toJson(addressModelRoot), JsonObject::class.java))

        } else if (addressTypeReceived == "address") {

            val payload = VendorProfileModel.Data.Address(
                city = binding.updateWorkAddressBtmsht.cityTiet.text.toString(),
                landmark = binding.updateWorkAddressBtmsht.landmarkTiet.text.toString(),
                pin = binding.updateWorkAddressBtmsht.pincodeTiet.text.toString(),
                state = binding.updateWorkAddressBtmsht.stateTiet.text.toString(),
                latitude = currentLatitudeLongitude?.latitude.toString(),
                longitude = currentLatitudeLongitude?.longitude.toString(),
                line1 = binding.updateWorkAddressBtmsht.addressLine1Tiet.text.toString()
            )

            val addressModelRoot = PersonalAddressModelRoot(
                payload
            )

            if (userTypeReceived == "vendor") {
                vendorViewModel.updateVendor(vendorIdReceived ?: "",
                    Gson().fromJson(Gson().toJson(addressModelRoot), JsonObject::class.java))
            } else if (userTypeReceived == "rider") {
                riderViewModel.updateRider(vendorIdReceived ?: "",
                    Gson().fromJson(Gson().toJson(addressModelRoot), JsonObject::class.java))
            }

        } else {
            Toast.makeText(requireContext(), "Invalid Address Found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValid(): Boolean {
        var valid = true

        if (binding.updateWorkAddressBtmsht.addressLine1Tiet.text?.trim()?.isEmpty() == true) {
            valid = false
        }

        if (binding.updateWorkAddressBtmsht.cityTiet.text?.trim()?.isEmpty() == true) {
            valid = false
        }

        if (binding.updateWorkAddressBtmsht.stateTiet.text?.trim()?.isEmpty() == true) {
            valid = false
        }

        if (binding.updateWorkAddressBtmsht.pincodeTiet.text?.trim()?.isEmpty() == true) {
            valid = false
        }

        return valid
    }

    private fun setUpMap() {
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        binding.mapMarkerIv.makeViewVisible()

        mMap.setOnCameraMoveStartedListener { reason: Int ->
            when (reason) {
                GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE -> {
                    Log.d("midLatLng-->", "The user gestured on the map.")
                }
                GoogleMap.OnCameraMoveStartedListener
                    .REASON_API_ANIMATION,
                -> {
                    Log.d("midLatLng-->", "The user tapped something on the map.")
                }
                GoogleMap.OnCameraMoveStartedListener
                    .REASON_DEVELOPER_ANIMATION,
                -> {
                    Log.d("midLatLng-->", "The app moved the camera.")
                }
            }
        }

        mMap.setOnCameraIdleListener {
            val midLatLng: LatLng =
                mMap.cameraPosition.target//map's center position latitude & longitude
            currentLatitudeLongitude = midLatLng
            currentAddressModel = getAddress(currentLatitudeLongitude!!)
            binding.updateWorkAddressBtmsht.fullAddressTv.text = currentAddressModel?.fullAddress
        }


    }

    private fun getAddress(latLng: LatLng): VendorAddressModel? {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val address: Address?
        var fullAddress = ""
        val addressModel = VendorAddressModel()
        val addresses: List<Address>? =
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        if (addresses?.isNotEmpty() == true) {
            address = addresses[0]
            fullAddress =
                address.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex
            val city = address.locality;
            val state = address.adminArea;
            val country = address.countryName;
            val postalCode = address.postalCode;
            val knownName = address.featureName; // Only if available else return NULL

            addressModel.fullAddress = fullAddress
            addressModel.city = city
            addressModel.state = state
            addressModel.country = country
            addressModel.postalCode = postalCode
            addressModel.knownName = knownName
            Log.d("addressModel-->", "1: " + fullAddress)
        } else {
            fullAddress = "Location not found"
            addressModel.fullAddress = fullAddress
            addressModel.city = null
            addressModel.state = null
            addressModel.country = null
            addressModel.postalCode = null
            addressModel.knownName = null
            Log.d("addressModel-->", "2: " + fullAddress)

        }

        return addressModel
    }


    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.workSpaceAddress_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val apiKey = getString(R.string.GMap_API_Key)
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }
        placesClient = Places.createClient(requireContext())

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)

        setUpMap()


    }

    override fun onMarkerClick(p0: Marker) = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUpdateAddressBinding.inflate(inflater, container, false)
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
    }

}