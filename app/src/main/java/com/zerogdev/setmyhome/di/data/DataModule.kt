package com.zerogdev.setmyhome.di.data

import android.arch.persistence.room.Room
import android.content.Context
import com.zerogdev.setmyhome.data.LocationDao
import com.zerogdev.setmyhome.data.PreferenceProvider
import com.zerogdev.setmyhome.data.SetRingerModeDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun providePreferenceProvider(@Named("appContext") context: Context) : PreferenceProvider
        = PreferenceProvider(context)

    @Provides
    @Singleton
    fun provideLocationDao(database: SetRingerModeDatabase) : LocationDao
        = database.getLocationDao()

    @Provides
    @Singleton
    fun provideDatabase(@Named("appContext") context: Context) : SetRingerModeDatabase
        = Room
        .databaseBuilder(context, SetRingerModeDatabase::class.java, "setmyhome.db")
        .build()
}