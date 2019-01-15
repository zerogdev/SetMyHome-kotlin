package com.zerogdev.setmyhome.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import com.zerogdev.setmyhome.R

class NotificationUtil(val  context: Context) {

    val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun showNotification(title: String, text: String, sourceActivityClass: Class<*>, requestCode: Int, notificationId: Int) {
        val notification = NotificationCompat.Builder(context, context.applicationInfo.loadLabel(context.packageManager).toString()).run {
            setContentTitle(title)
            setContentText(text)
            setSmallIcon(R.mipmap.ic_stat_h)
            val taskStackBuilder = TaskStackBuilder.create(context).run {
                addParentStack(sourceActivityClass)
                addNextIntent(Intent(context, sourceActivityClass))
            }
            val pendingIntent = taskStackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT)
            setContentIntent(pendingIntent)
            build()
        }
        notificationManager.notify(notificationId, notification)
    }


    fun cancel(id: Int) {
        notificationManager.cancel(id)
    }


}