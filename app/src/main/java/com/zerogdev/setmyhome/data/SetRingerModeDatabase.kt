package com.zerogdev.setmyhome.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(LocationData::class), version = 1)
abstract class SetRingerModeDatabase : RoomDatabase() {
    abstract fun getLocationDao() : LocationDao
}