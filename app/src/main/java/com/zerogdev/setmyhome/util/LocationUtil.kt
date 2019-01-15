package com.zerogdev.setmyhome.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import java.util.*

class LocationUtil(
    val context: Context,
    val locationManager: LocationManager = (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager),
    val searchLocationListener: OnSearchLocationListener?) : LocationListener{

    constructor(context: Context,
                locationManager: LocationManager = (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager)) : this(context, locationManager, null)


    interface OnSearchLocationListener {
        fun onSuccessLocation(location: Location?)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    override fun onLocationChanged(location: Location?) {
        locationManager.removeUpdates(this@LocationUtil)
        searchLocationListener?.onSuccessLocation(location)
    }

    fun searchLocation() {
        if (checkLocationPermission()) {
            locationManager.run {
                if (isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.1f, this@LocationUtil)
                }
                if (isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0, 0.1f, this@LocationUtil
                    )
                }
            }
        }
    }


    fun getAddress(latitude:Double, longitude:Double) : String
            = Geocoder(context, Locale.KOREA).run {
        val address : MutableList<Address>? = getFromLocation(latitude, longitude, 10)
        if (address != null && address.size > 0) {
            address[0].getAddressLine(0).toString()
        } else {
            "not found"
        }
    }

    fun getDistance(latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double) : Float{
        val location1 = Location("a")
        location1.latitude = latitude1
        location1.longitude = longitude1

        val location2 = Location("b")
        location2.latitude = latitude2
        location2.longitude = longitude2

        return location1.distanceTo(location2)
    }

    fun checkAndRequestLocationPermission(activity: Activity, reqCode: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), reqCode
                )
                return false
            }
        }
        return true
    }

    fun checkLocationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}