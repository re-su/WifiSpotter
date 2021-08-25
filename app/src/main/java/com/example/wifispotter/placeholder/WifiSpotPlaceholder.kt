package com.example.wifispotter.placeholder

import java.util.*

object WifiSpotPlaceholder {
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