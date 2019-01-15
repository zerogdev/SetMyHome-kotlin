package com.zerogdev.setmyhome.receiver

import android.content.Context
import android.content.Intent
import com.zerogdev.setmyhome.data.LocationDao
import com.zerogdev.setmyhome.util.SetMyHomeUtil
import com.zerogdev.setmyhome.util.startAlarm
import dagger.android.DaggerBroadcastReceiver
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AlarmReceiver : DaggerBroadcastReceiver() {

    @Inject lateinit var locationDao: LocationDao

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        context?.let {
            SetMyHomeUtil().setRingerModeAndCheckCurrentLocation(context, locationDao, null)
            startAlarm(context, AlarmReceiver::class.java, 0)
        }
    }


}