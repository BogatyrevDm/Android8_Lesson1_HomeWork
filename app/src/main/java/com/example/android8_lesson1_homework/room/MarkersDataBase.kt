package com.example.android8_lesson1_homework.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MarkerEntity::class), version = 1, exportSchema = false)
abstract class MarkersDataBase : RoomDatabase() {
    abstract fun markersDao(): MarkersDao
}