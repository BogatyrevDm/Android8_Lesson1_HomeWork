package com.example.android8_lesson1_homework.utils

import com.example.android8_lesson1_homework.model.Marker
import com.example.android8_lesson1_homework.room.MarkerEntity

fun convertMarkerEntityListToModel(list: List<MarkerEntity>): List<Marker> {
    val markersList = mutableListOf<Marker>()
    for (markerEntity in list) {
        markersList.add(
            Marker(
                id = markerEntity.id,
                name = markerEntity.name ?: "",
                description = markerEntity.description ?: "",
                latitude = markerEntity.latitude,
                longitude = markerEntity.longitude
            )
        )
    }
    return markersList
}

fun convertMarkerEntityToModel(markerEntity: MarkerEntity): Marker {

    return Marker(
        id = markerEntity.id,
        name = markerEntity.name ?: "",
        description = markerEntity.description ?: "",
        latitude = markerEntity.latitude,
        longitude = markerEntity.longitude
    )

}

fun convertModelToMarkerEntity(marker: Marker): MarkerEntity {
    return MarkerEntity(
        id = marker.id,
        name = marker.name,
        description = marker.description,
        latitude = marker.latitude,
        longitude = marker.longitude
    )
}