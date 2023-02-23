package com.laundrybuoy.admin.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentMapBinding
import com.laundrybuoy.admin.databinding.FragmentOrdersBinding
import com.laundrybuoy.admin.model.map.MapCoordinatesModel
import com.laundrybuoy.admin.model.map.MapInfo2
import com.laundrybuoy.admin.ui.rider.RiderRootFragment
import com.laundrybuoy.admin.ui.vendor.VendorRootFragment
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.map.BitmapHelper
import com.laundrybuoy.admin.utils.map.ClusteringRenderer
import com.laundrybuoy.admin.utils.map.MarkerInfoWindowAdapter
import com.laundrybuoy.admin.viewmodel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : BaseFragment(), GoogleMap.OnInfoWindowClickListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel by viewModels<MapViewModel>()
    private var roleSelected: String? = null
    private val places: MutableList<MapInfo2> = arrayListOf()
    private var circle: Circle? = null
    private var mapFragment: SupportMapFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNav()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserver()
        onClicks()
        setBottomNav()
    }

    private fun init() {
        mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment

    }

    private fun onClicks() {

        binding.rlPartner.setOnClickListener {
            binding.rlPartner.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.colorBlueHighlight))
            binding.tvPartner.setTextColor(Color.WHITE)
            binding.rlRider.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.colorBlueDark))
            binding.tvRider.setTextColor(getMainActivity()!!.resources.getColor(R.color.colorBlueHighlight))

            roleSelected = "partner"
            mapViewModel.getMapCoordinates(roleSelected!!)
        }

        binding.rlRider.setOnClickListener {
            binding.rlRider.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.colorBlueHighlight))
            binding.tvRider.setTextColor(Color.WHITE)
            binding.rlPartner.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.colorBlueDark))
            binding.tvPartner.setTextColor(getMainActivity()!!.resources.getColor(R.color.colorBlueHighlight))

            roleSelected = "rider"
            mapViewModel.getMapCoordinates(roleSelected!!)

        }

        binding.rlPartner.performClick()

    }

    private fun initObserver() {
        mapViewModel.mapCoordinatesLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.data != null) {
                        setMapUI(it.data?.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })
    }

    private fun setMapUI(data: List<MapCoordinatesModel.Data>) {
        places.clear()
        data.forEach {
            places.add(it.toPlace())
        }

        mapFragment?.getMapAsync { googleMap ->
            // Ensure all places are visible in the map
            googleMap.clear()
            googleMap.setOnMapLoadedCallback {
                googleMap.setOnInfoWindowClickListener(this);
                val bounds = LatLngBounds.builder()
                places.forEach { bounds.include(it.latLng) }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
            }
            addClusteredMarkers(googleMap)
        }

    }

    /**
     * Adds markers to the map with clustering support.
     */
    private fun addClusteredMarkers(googleMap: GoogleMap) {
        // Create the ClusterManager class and set the custom renderer
        val clusterManager = ClusterManager<MapInfo2>(requireContext(), googleMap)
        clusterManager.renderer =
            ClusteringRenderer(
                requireContext(),
                googleMap,
                clusterManager
            )

        // Set custom info window adapter
        clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(requireContext()))

        // Add the places to the ClusterManager
        clusterManager.addItems(places)
        clusterManager.cluster()

        // Show polygon
        clusterManager.setOnClusterItemClickListener { item ->
            addCircle(googleMap, item)
            return@setOnClusterItemClickListener false
        }

        // When the camera starts moving, change the alpha value of the marker to translucent
        googleMap.setOnCameraMoveStartedListener {
            clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
        }

        googleMap.setOnCameraIdleListener {
            // When the camera stops moving, change the alpha value back to opaque
            clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }

            // Call clusterManager.onCameraIdle() when the camera stops moving so that re-clustering
            // can be performed when the camera stops moving
            clusterManager.onCameraIdle()
        }
    }


    /**
     * Adds a [Circle] around the provided [item]
     */
    private fun addCircle(googleMap: GoogleMap, item: MapInfo2) {
        circle?.remove()
        circle = googleMap.addCircle(
            CircleOptions()
                .center(item.latLng)
                .radius(1000.0)
                .fillColor(ContextCompat.getColor(requireContext(), R.color.teal_200))
                .strokeColor(ContextCompat.getColor(requireContext(), R.color.teal_700))
        )
    }

    private val bicycleIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), R.color.teal_200)
        BitmapHelper.vectorToBitmap(requireContext(),
            R.drawable.ic_directions_bike_black_24dp,
            color)
    }


    override fun onInfoWindowClick(marker: Marker) {
        val markerObj = marker.tag as? MapInfo2 ?: null

        if (markerObj != null) {
            val userId = markerObj.id
            val role = markerObj.role

            if (role == "partner") {
                val frag = VendorRootFragment.newInstance(userId ?: "")
                getMainActivity()?.addFragment(true,
                    getMainActivity()?.getVisibleFrame()!!,
                    frag)
            } else if (role == "rider") {
                val frag = RiderRootFragment.newInstance(userId ?: "")
                getMainActivity()?.addFragment(true,
                    getMainActivity()?.getVisibleFrame()!!,
                    frag)
            }
        }

    }

    fun MapCoordinatesModel.Data.toPlace(): MapInfo2 = MapInfo2(
        name = name,
        latLng = LatLng(address.latitude.toDouble(), address.longitude.toDouble()),
        role = role,
        id = id
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
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