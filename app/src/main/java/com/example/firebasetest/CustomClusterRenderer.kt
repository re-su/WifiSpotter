package com.example.firebasetest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class CustomClusterRenderer(context: Context?,
                               map: GoogleMap?, clusterManager: ClusterManager<MapsFragment.MyItem>?
) : DefaultClusterRenderer<MapsFragment.MyItem>(context, map, clusterManager){
    lateinit var context: Context

    override fun onBeforeClusterItemRendered(
        item: MapsFragment.MyItem,
        markerOptions: MarkerOptions
    ) {
        super.onBeforeClusterItemRendered(item, markerOptions)

        if(item.getType() == WifiIconType.DEFAULT)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.wifi))
        else if(item.getType() == WifiIconType.MY_SPOT)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
    }
}

