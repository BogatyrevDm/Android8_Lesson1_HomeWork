package com.example.android8_lesson1_homework.room

import androidx.room.*

@Dao
interface MarkersDao {
    @Query("SELECT * FROM MarkerEntity")
    suspend fun all(): List<MarkerEntity>

    @Query("SELECT * FROM MarkerEntity WHERE id = :id")
    suspend fun getDataById(id: Int): MarkerEntity

    @Query("SELECT * FROM MarkerEntity WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun getDataByLatitudeAndLongitude(latitude:Double, longitude:Double): MarkerEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: MarkerEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<MarkerEntity>)

    @Update
    suspend fun update(entity: MarkerEntity)

    @Delete
    suspend fun delete(entity: MarkerEntity)
}