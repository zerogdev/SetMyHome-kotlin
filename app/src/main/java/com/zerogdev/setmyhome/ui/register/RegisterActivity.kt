package com.zerogdev.setmyhome.ui.register

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.media.AudioManager
import android.os.Bundle
import android.widget.Toast
import com.zerogdev.setmyhome.R
import com.zerogdev.setmyhome.data.LocationDao
import com.zerogdev.setmyhome.data.LocationData
import com.zerogdev.setmyhome.rx.AutoClearedDisposable
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_add_location.*
import java.util.*
import javax.inject.Inject
import com.zerogdev.setmyhome.receiver.AlarmReceiver
import com.zerogdev.setmyhome.util.*


class RegisterActivity : DaggerAppCompatActivity(), LocationUtil.OnSearchLocationListener {

    private var isFindLocation = false
    private var isSelectedInMode = false

    var modeIn : Int = AudioManager.RINGER_MODE_SILENT

    lateinit var curLocation : Location
    lateinit var registerViewModel: RegisterViewModel

    @Inject lateinit var locationDao : LocationDao

    private val disposables = AutoClearedDisposable(this, false)
    private val locationManager: LocationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val locationUtil : LocationUtil by lazy {
        LocationUtil(this, locationManager,this)
    }

    private val ringerUtil: RingerUtil by lazy {
        RingerUtil(this@RegisterActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)

        registerViewModel = RegisterViewModel(locationDao)

        //저장
        save.setOnClickListener {
            if (name.text.toString().isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_LONG).show()
            } else if (!isFindLocation) {
                Toast.makeText(this, "위치 입력해주세요", Toast.LENGTH_LONG).show()
            } else if (!isSelectedInMode) {
                Toast.makeText(this, "위치 안에 들어온경우 모드를 선택해주세요", Toast.LENGTH_LONG).show()
            } else {
                if (modeIn == AudioManager.RINGER_MODE_SILENT) {
                    ringerUtil.checkAndRequestNotificationPermission(
                        this@RegisterActivity,
                        1,
                        object : RingerUtil.onCheckPermissionListener {

                            override fun onNotGrantedPermission() {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "무음 모드에서는 ${getString(R.string.app_name)}의 On 설정이 필요합니다.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            override fun onGrantedPermission() {
                                setAlarm()
                                finish()
                            }
                        })
                } else {
                    setAlarm()
                    finish()
                }
            }
        }

        findLocationBtn.setOnClickListener {
            searchLocation()
        }

        inMode.setOnCheckedChangeListener { _, resId ->
            when (resId) {
                R.id.inModeRing -> modeIn =
                        AudioManager.RINGER_MODE_NORMAL
                R.id.inModeVibration -> modeIn =
                        AudioManager.RINGER_MODE_VIBRATE
                R.id.inModeSilent -> {
                    modeIn = AudioManager.RINGER_MODE_SILENT
                }
            }
            isSelectedInMode = true
        }
    }

    private fun searchLocation() {
        if (locationUtil.checkAndRequestLocationPermission(this, 0)) {
            locationText.text = "현재 위치 수신 중..."
            locationUtil.searchLocation()
        }
    }

    private fun setAlarm() {
        LocationData(
            name.text.toString(),
            locationText.text.toString(),
            curLocation.latitude,
            curLocation.longitude,
            modeIn,
            true,
            Date().time
        )
            .run {
                disposables.add(registerViewModel.addLocation(this))
            }

        startAlarm(applicationContext, AlarmReceiver::class.java, 0)

        SetMyHomeUtil().setRingerModeAndCheckCurrentLocation(this@RegisterActivity, locationDao, disposables)

    }

    override fun onSuccessLocation(location: Location?) {
        if (location != null) {
            curLocation = location
            locationText.text = locationUtil.getAddress(location.latitude, location.longitude)
            isFindLocation = true
        } else {
            locationText.text = "현재 위치를 찾을 수 없습니다..."
            isFindLocation = false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.size >= 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                searchLocation()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        ringerUtil.onActivityResultPermission(this, requestCode, resultCode)
        super.onActivityResult(requestCode, resultCode, data)
    }
}