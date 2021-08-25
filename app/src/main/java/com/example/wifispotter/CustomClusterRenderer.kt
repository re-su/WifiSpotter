package com.example.wifispotter

import android.content.Context
import com.example.firebasetest.R
import com.example.wifispotter.enums.WifiIconType
import com.example.wifispotter.fragments.MapsFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class CustomClusterRenderer(context: Context?,
                               map: GoogleMap?, clusterManager: ClusterManager<MapsFragment.MySpotClusterMarker>?
) : DefaultClusterRenderer<MapsFragment.MySpotClusterMarker>(context, map, clusterManager){
    lateinit var context: Context

    override fun onBeforeClusterItemRendered(
        item: MapsFragment.MySpotClusterMarker,
        markerOptions: MarkerOptions
    ) {
        super.onBeforeClusterItemRendered(item, markerOptions)

        if(item.getType() == WifiIconType.DEFAULT)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.wifi))
        else if(item.getType() == WifiIconType.MY_SPOT)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.wifiicon2))
    }
}

