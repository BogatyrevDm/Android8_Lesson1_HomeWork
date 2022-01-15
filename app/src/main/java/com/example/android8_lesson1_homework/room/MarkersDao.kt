package com.example.android8_lesson1_homework.room

import androidx.room.*

@Dao
interface MarkersDao {
    @Query("SELECT * FROM MarkerEntity")
    fun all(): List<MarkerEntity>

    @Query("SELECT * FROM MarkerEntity WHERE id = :id")
    fun getDataById(id: Int): MarkerEntity

    @Query("SELECT * FROM MarkerEntity WHERE latitude = :latitude AND longitude = :longitude")
    fun getDataByLatitudeAndLongitude(latitude:Double, longitude:Double): MarkerEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: MarkerEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(entities: List<MarkerEntity>)

    @Update
    fun update(entity: MarkerEntity)

    @Delete
    fun delete(entity: MarkerEntity)
}