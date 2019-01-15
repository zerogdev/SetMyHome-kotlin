package com.zerogdev.setmyhome.data

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLocation(location: LocationData)

    @Query("SELECT * FROM location")
    fun getLocations() : Flowable<List<LocationData>>

    @Query("DELETE FROM location")
    fun deleteLocations()

    @Update
    fun updateLocation(location: LocationData)
}