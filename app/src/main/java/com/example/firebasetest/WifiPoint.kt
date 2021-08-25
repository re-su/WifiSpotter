package com.example.firebasetest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WifiPoint(
    @ColumnInfo(name = "ssid")
    val ssid: String = "",

    @ColumnInfo(name = "password")
    val password: String = "",

    @ColumnInfo(name = "latitude")
    val latitude: String = "",

    @ColumnInfo(name = "longitude")
    val longitude: String = "",

    @ColumnInfo(name = "UserIdentifier")
    val userIdentifier: String = ""
){
}
