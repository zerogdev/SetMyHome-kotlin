package com.zerogdev.setmyhome.di.ui

import com.zerogdev.setmyhome.data.LocationDao
import com.zerogdev.setmyhome.data.LocationData
import com.zerogdev.setmyhome.ui.main.MainAdapter
import com.zerogdev.setmyhome.ui.main.MainViewModel
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    fun provideAdapter(): MainAdapter
        = MainAdapter()

    @Provides
    fun providerModel(locationData: LocationDao): MainViewModel
     = MainViewModel(locationData)
}