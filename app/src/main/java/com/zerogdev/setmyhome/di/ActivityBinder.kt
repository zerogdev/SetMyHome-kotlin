package com.zerogdev.setmyhome.di

import com.zerogdev.setmyhome.di.data.DataModule
import com.zerogdev.setmyhome.di.ui.MainModule
import com.zerogdev.setmyhome.receiver.AlarmReceiver
import com.zerogdev.setmyhome.ui.main.MainActivity
import com.zerogdev.setmyhome.ui.register.RegisterActivity
import com.zerogdev.setmyhome.ui.register.RegisterModeOutActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
@Module
abstract class ActivityBinder {


    @ContributesAndroidInjector(modules = arrayOf(MainModule::class))
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector()
    abstract fun bindRegisterActivity(): RegisterActivity

    @ContributesAndroidInjector()
    abstract fun bindRegisterModeOutActivity(): RegisterModeOutActivity

    @ContributesAndroidInjector()
    abstract fun bindAlarmReceiver(): AlarmReceiver

}