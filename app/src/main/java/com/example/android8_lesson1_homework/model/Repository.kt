package com.example.android8_lesson1_homework.model

interface Repository {
    fun setMarker(marker: Marker)
    fun getMarker(id: Int): Marker
    fun getDataByLatitudeAndLongitude(latitude: Double, longitude: Double): Marker
    fun getMarkers(): List<Marker>
}