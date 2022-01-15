package com.example.android8_lesson1_homework.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android8_lesson1_homework.model.Marker
import com.example.android8_lesson1_homework.model.Repository

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState>,
    private val liveDataToObserveSingle: MutableLiveData<AppStateSingle>,
    private val repo: Repository
) : ViewModel(

) {
    private var marker: Marker? = null

    fun getLiveData() = liveDataToObserve
    fun getLiveDataSingle() = liveDataToObserveSingle
    fun getAllMarkers() = getAllMarkersFromLocalStorage()

    fun getMarkerByLatitudeAndLongitude(latitude: Double, longitude: Double) =
        getMarkerByLatitudeAndLongitudeFromLocalStorage(latitude, longitude)

    fun setMarker(
        latitude: Double,
        longitude: Double
    ) {
        Thread {
            repo.setMarker(
                Marker(
                    latitude = latitude,
                    longitude = longitude
                )
            )
        }.start()

    }

    fun getLocalMarker(): Marker? {
        return marker
    }

    fun setLocalMarkerFields(name: String, desctiption: String) {
        marker?.let {
            it.name = name
            it.description = desctiption
        }
    }

    fun setMarkerById() {
        marker?.let {
            Thread {
                repo.setMarker(it)
            }.start()
        }


    }

    private fun getMarkerByLatitudeAndLongitudeFromLocalStorage(
        latitude: Double,
        longitude: Double
    ) {
        liveDataToObserveSingle.value = AppStateSingle.Loading
        Thread {
            marker = repo.getDataByLatitudeAndLongitude(latitude, longitude)
            marker?.let {
                liveDataToObserveSingle.postValue(AppStateSingle.Success(it))
            }

        }.start()
    }

    private fun getMarkerByIdFromLocalStorage(
        id: Int
    ) {
        liveDataToObserveSingle.value = AppStateSingle.Loading
        Thread {
            liveDataToObserveSingle.postValue(AppStateSingle.Success(repo.getMarker(id)))
        }.start()
    }

    private fun getAllMarkersFromLocalStorage() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            liveDataToObserve.postValue(AppState.Success(repo.getMarkers()))
        }.start()
    }
}