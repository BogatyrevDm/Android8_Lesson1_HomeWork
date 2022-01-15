package com.example.android8_lesson1_homework.model

interface Repository {
    suspend fun setMarker(marker: Marker)
    suspend fun getMarker(id: Int): Marker
    suspend fun getDataByLatitudeAndLongitude(latitude: Double, longitude: Double): Marker
    suspend fun getMarkers(): List<Marker>
}