package com.zerogdev.setmyhome.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock

fun startAlarm(content: Context, cls: Class<*>, id: Int) {
    Intent(content, cls).let { intent ->
        PendingIntent.getBroadcast(content, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            .let { pendingIntent ->
                var firstTime = SystemClock.elapsedRealtime()
                firstTime += (30 * 60 * 1000).toLong()
//                firstTime += (10 * 1000).toLong() //10초 후 알람 이벤트 발생

                val alarmManager = content.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, pendingIntent)
                } else {
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, pendingIntent)
                }
            }
    }
}


fun cancelAlarm(content: Context, cls: Class<*>, id: Int) {
    val alarmManager = content.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    Intent(content, cls).let { intent ->
        PendingIntent.getBroadcast(content, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            .let {
                alarmManager.cancel(it)
            }
    }
}

fun existAlarm(content: Context, cls: Class<*>, id: Int): Boolean {
    Intent(content, cls).let {
        return PendingIntent.getBroadcast(content, id, it, PendingIntent.FLAG_NO_CREATE) != null
    }
}