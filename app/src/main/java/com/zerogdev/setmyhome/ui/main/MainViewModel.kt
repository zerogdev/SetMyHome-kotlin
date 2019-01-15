package com.zerogdev.setmyhome.ui.main

import com.zerogdev.setmyhome.data.LocationDao
import com.zerogdev.setmyhome.data.LocationData
import com.zerogdev.setmyhome.extensions.runOnIoScheduler
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

class MainViewModel(val locationDao: LocationDao) {

    val registerList: Flowable<List<LocationData>>
        get() = locationDao.getLocations()

    fun updateLocation(locationData: LocationData) : Disposable
            = runOnIoScheduler { locationDao.addLocation(locationData) }
}