package com.zerogdev.setmyhome.ui.register

import com.zerogdev.setmyhome.data.LocationDao
import com.zerogdev.setmyhome.data.LocationData
import com.zerogdev.setmyhome.extensions.runOnIoScheduler
import io.reactivex.disposables.Disposable

class RegisterViewModel(val locationDao: LocationDao) {

    fun addLocation(locationData: LocationData) : Disposable
        = runOnIoScheduler { locationDao.addLocation(locationData) }

}