package com.zerogdev.setmyhome.di

import android.app.Application
import com.zerogdev.setmyhome.SetRingerModeApp
import com.zerogdev.setmyhome.di.data.DataModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        AppModule::class,
        AndroidSupportInjectionModule::class,
        DataModule::class,
        ActivityBinder::class
    )
)
interface AppComponent : AndroidInjector<SetRingerModeApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app : Application) : Builder

        fun build(): AppComponent
    }
}