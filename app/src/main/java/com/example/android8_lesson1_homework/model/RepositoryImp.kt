package com.example.android8_lesson1_homework.model

import com.example.android8_lesson1_homework.room.MarkersDao
import com.example.android8_lesson1_homework.utils.convertMarkerEntityListToModel
import com.example.android8_lesson1_homework.utils.convertMarkerEntityToModel
import com.example.android8_lesson1_homework.utils.convertModelToMarkerEntity

class RepositoryImp(private val markersDao: MarkersDao) : Repository {
    override suspend fun setMarker(marker: Marker) {
        if (marker.id == 0) {
            markersDao.insert(convertModelToMarkerEntity(marker))
        } else {
            markersDao.update(convertModelToMarkerEntity(marker))
        }
    }

    override suspend fun getMarker(id: Int): Marker =
        convertMarkerEntityToModel(markersDao.getDataById(id))


    override suspend fun getDataByLatitudeAndLongitude(latitude: Double, longitude: Double): Marker =
        convertMarkerEntityToModel(markersDao.getDataByLatitudeAndLongitude(latitude, longitude))

    override suspend fun getMarkers(): List<Marker> = convertMarkerEntityListToModel(markersDao.all())
}