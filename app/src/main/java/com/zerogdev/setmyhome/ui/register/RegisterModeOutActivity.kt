package com.zerogdev.setmyhome.ui.register

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.widget.Toast
import com.zerogdev.setmyhome.R
import com.zerogdev.setmyhome.data.PreferenceProvider
import com.zerogdev.setmyhome.util.RingerUtil
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_mode_out.*
import javax.inject.Inject

class RegisterModeOutActivity: DaggerAppCompatActivity() {

    private var isSelectedOutMode = false
    var modeOut : Int = AudioManager.RINGER_MODE_SILENT

    private val ringerUtil: RingerUtil by lazy {
        RingerUtil(this@RegisterModeOutActivity)
    }

    @Inject lateinit var preferenceProvider: PreferenceProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_out)


        outMode.setOnCheckedChangeListener { _, resId ->
            when (resId) {
                R.id.outModeRing -> modeOut =
                        AudioManager.RINGER_MODE_NORMAL
                R.id.outModeVibration -> modeOut =
                        AudioManager.RINGER_MODE_VIBRATE
                R.id.outModeSilent  -> modeOut =
                        AudioManager.RINGER_MODE_SILENT
            }
            isSelectedOutMode = true
        }

        save.setOnClickListener {
            if (!isSelectedOutMode) {
                Toast.makeText(this, "위치 밖으로 나간경우 모드를 선택해주세요", Toast.LENGTH_LONG).show()
            }
            if (modeOut == AudioManager.RINGER_MODE_SILENT) {
                ringerUtil.checkAndRequestNotificationPermission(
                    this@RegisterModeOutActivity,
                    1,
                    object : RingerUtil.onCheckPermissionListener {

                        override fun onNotGrantedPermission() {
                            Toast.makeText(
                                this@RegisterModeOutActivity,
                                "무음 모드에서는 ${getString(R.string.app_name)}의 On 설정이 필요합니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onGrantedPermission() {
                            preferenceProvider.updateModeOut(modeOut)
                            finish()
                        }
                    })
            } else {
                preferenceProvider.updateModeOut(modeOut)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        ringerUtil.onActivityResultPermission(this, requestCode, resultCode)
        super.onActivityResult(requestCode, resultCode, data)
    }
}