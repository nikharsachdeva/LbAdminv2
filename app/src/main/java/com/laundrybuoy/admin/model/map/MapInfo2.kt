package com.laundrybuoy.admin.model.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class MapInfo2(
    val name: String,
    val latLng: LatLng,
    val role: String,
    val id: String,
) : ClusterItem {
    override fun getPosition(): LatLng = latLng

    override fun getTitle(): String = name

    override fun getSnippet(): String = role
}