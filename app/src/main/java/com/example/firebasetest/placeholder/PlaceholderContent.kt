package com.example.firebasetest.placeholder

import android.util.Log
import com.example.firebasetest.MapsFragment
import com.example.firebasetest.WifiPoint
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

object PlaceholderContent {
    val ITEMS: MutableList<PlaceholderItem> = ArrayList()
    val ITEM_MAP: MutableMap<String, PlaceholderItem> = HashMap()

    init {

    }

    fun addItem(item: PlaceholderItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    fun clearItems() {
        ITEMS.clear()
    }


    /**
     * A placeholder item representing a piece of content.
     */
    data class PlaceholderItem(val id: String, val password: String, val ssid: String, val latitude: Double, val longitude: Double) {
        override fun toString(): String = ssid
    }
}