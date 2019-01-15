package com.zerogdev.setmyhome.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "location")
class LocationData (
    var name: String,
    var address: String,
    var latitude: Double,
    var longitude: Double,
    var modeIn: Int,
    var onOff: Boolean,
    @PrimaryKey val time:Long
)