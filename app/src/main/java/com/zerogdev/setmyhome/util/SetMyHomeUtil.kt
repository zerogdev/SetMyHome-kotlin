package com.zerogdev.setmyhome.util

import android.content.Context
import android.location.Location
import com.zerogdev.setmyhome.R
import com.zerogdev.setmyhome.data.LocationDao
import com.zerogdev.setmyhome.data.LocationData
import com.zerogdev.setmyhome.data.PreferenceProvider
import com.zerogdev.setmyhome.rx.AutoClearedDisposable
import com.zerogdev.setmyhome.ui.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SetMyHomeUtil {

    companion object {
        const val DISTANCE: Int = 100
    }


    fun setRingerModeAndCheckCurrentLocation(context: Context, locationDao:LocationDao, disposables: AutoClearedDisposable?) {
        LocationUtil(context, searchLocationListener = object : LocationUtil.OnSearchLocationListener {
            override fun onSuccessLocation(location: Location?) {
                location?.run {

                    val disposable = (locationDao.getLocations()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {

                            var curLocation: LocationData? = null

                            for (locationData in it) {
                                if (!locationData.onOff) {
                                    continue
                                }

                                val saveLongitude = locationData.longitude
                                val saveLatitude = locationData.latitude

                                val distance = LocationUtil(context).getDistance(
                                    latitude,
                                    longitude,
                                    saveLatitude,
                                    saveLongitude
                                )
                                if (distance <= DISTANCE) {
                                    curLocation = locationData
                                }
                                L.d("saveLongitude=$saveLongitude, saveLatitude=$saveLatitude, longitude=$longitude, latitude=$latitude, distance=$distance")
                            }

                            val curMode: Int = curLocation?.modeIn ?: PreferenceProvider(context).getModeOut()
                            val text: String = curLocation?.name ?: "범위 밖으로 나갔을 때"


                            RingerUtil(context).run {
                                if (!checkRingerMode(curMode)) {
                                    changeRingerMode(curMode)
                                    NotificationUtil(context)
                                        .showNotification(
                                            context.getString(R.string.app_name),
                                            "$text: ${RingerUtil.getModeText(curMode)}으로 설정되었습니다.",
                                            MainActivity::class.java,
                                            1,
                                            1
                                        )
                                }
                            }
                        }
                    )

                    disposables?.add(disposable)
                }
            }
        }).apply {
            searchLocation()
        }
    }
}