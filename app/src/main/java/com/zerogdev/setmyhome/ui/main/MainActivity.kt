package com.zerogdev.setmyhome.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.zerogdev.setmyhome.R
import com.zerogdev.setmyhome.data.LocationDao
import com.zerogdev.setmyhome.data.LocationData
import com.zerogdev.setmyhome.data.PreferenceProvider
import com.zerogdev.setmyhome.receiver.AlarmReceiver
import com.zerogdev.setmyhome.rx.AutoClearedDisposable
import com.zerogdev.setmyhome.ui.register.RegisterActivity
import com.zerogdev.setmyhome.util.SetMyHomeUtil
import com.zerogdev.setmyhome.util.cancelAlarm
import com.zerogdev.setmyhome.util.startAlarm
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    internal val disposable = AutoClearedDisposable(this)

    internal val viewDisposable = AutoClearedDisposable(lifecycleOwner = this, alwaysClearOnStop = false)

    @Inject
    lateinit var adapter: MainAdapter

    @Inject
    lateinit var locationDao: LocationDao

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var preferenceProvider: PreferenceProvider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(disposable)
        lifecycle.addObserver(viewDisposable)

        with(recycler) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
        adapter.preferenceProvider = preferenceProvider
        adapter.setItemClickListener(object : MainAdapter.ItemClickListener {
            override fun onItemOnOffClick(locationData: LocationData, checked: Boolean) {
                locationData.onOff = checked
                viewDisposable.add(mainViewModel.updateLocation(locationData))

                var allOff = true
                for (item in adapter.getItems()) {
                    if (item.onOff) {
                        allOff = false
                    }
                }
                if (allOff) {
                    //전체 Off 인경우 알람 끄기
                    cancelAlarm(applicationContext, AlarmReceiver::class.java, 0)
                } else {
                    cancelAlarm(applicationContext, AlarmReceiver::class.java, 0)
                    SetMyHomeUtil().setRingerModeAndCheckCurrentLocation(applicationContext, locationDao, viewDisposable)
                    startAlarm(applicationContext, AlarmReceiver::class.java, 0)
                }
            }

            override fun onItemClick(locationData: LocationData) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAddClick() {
                startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
            }
        })


        viewDisposable.add(
            mainViewModel.registerList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items ->
                    with(adapter) {
                        if (!items.isEmpty()) {
                            setItems(items)
                        }
                        notifyDataSetChanged()
                    }
                })

        viewDisposable.add(
            preferenceProvider.modeOut
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.modeOut = it
                }
        )
    }
}
