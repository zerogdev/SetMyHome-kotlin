package com.zerogdev.setmyhome.util

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.widget.Toast

class RingerUtil(val context: Context) {

    interface onCheckPermissionListener {
        fun onGrantedPermission()
        fun onNotGrantedPermission()
    }

    var permissionListener: onCheckPermissionListener? = null
    var requestCode: Int = 0

    private val audioServiceManager: AudioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    val ringermode
        get() = audioServiceManager.ringerMode

    companion object {
        fun getModeText(mode: Int): String = when (mode) {
            AudioManager.RINGER_MODE_NORMAL -> "벨소리"
            AudioManager.RINGER_MODE_SILENT -> "무음"
            AudioManager.RINGER_MODE_VIBRATE -> "진동"
            else -> ""
        }
    }


    fun changeRingerMode(mode : Int) {
        audioServiceManager.ringerMode = mode
        Toast.makeText(context, "${getModeText(mode)} 으로 설정되었습니다.", Toast.LENGTH_LONG).show()
    }

    fun checkRingerMode(mode: Int): Boolean =
        audioServiceManager.ringerMode == mode


    fun checkAndRequestNotificationPermission(activity: Activity, requestCode: Int, onCheckPermissionListener: onCheckPermissionListener, isRequested:Boolean = false) {
        if (!checkNotificationPermission()) {
            if (!isRequested) {
                permissionListener = onCheckPermissionListener
                this.requestCode = requestCode
                activity.startActivityForResult(
                    Intent(
                        android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
                    ), requestCode
                )
            } else {
                onCheckPermissionListener.let {
                    onCheckPermissionListener.onNotGrantedPermission()
                    permissionListener = null
                }
            }
        } else {
            onCheckPermissionListener.let {
                onCheckPermissionListener.onGrantedPermission()
                permissionListener = null
            }
        }
    }

    fun onActivityResultPermission(activity: Activity, requestCode: Int, resultCode: Int) {
        if (resultCode == 0) {
            if (this.requestCode == requestCode) {
                permissionListener?.let {
                    checkAndRequestNotificationPermission(activity, requestCode, permissionListener!!, true)
                }
            }
        }
    }

    private fun checkNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return notificationManager.isNotificationPolicyAccessGranted
        }
        return true
    }
}