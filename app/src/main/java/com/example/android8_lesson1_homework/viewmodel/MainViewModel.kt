package com.example.android8_lesson1_homework.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android8_lesson1_homework.model.Marker
import com.example.android8_lesson1_homework.model.Repository
import kotlinx.coroutines.*

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState>,
    private val liveDataToObserveSingle: MutableLiveData<AppStateSingle>,
    private val repo: Repository
) : ViewModel(

) {
    private var marker: Marker? = null

    val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })

    fun cancelJob() {
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    fun handleError(error: Throwable) {
        liveDataToObserve.postValue(AppState.Error(error))
    }

    fun getLiveData() = liveDataToObserve
    fun getLiveDataSingle() = liveDataToObserveSingle
    fun getAllMarkers() = getAllMarkersFromLocalStorage()

    fun getMarkerByLatitudeAndLongitude(latitude: Double, longitude: Double) =
        getMarkerByLatitudeAndLongitudeFromLocalStorage(latitude, longitude)

    fun setMarker(
        latitude: Double,
        longitude: Double
    ) {
        cancelJob()
        viewModelCoroutineScope.launch {
            repo.setMarker(
                Marker(
                    latitude = latitude,
                    longitude = longitude
                )
            )
        }

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
            cancelJob()
            viewModelCoroutineScope.launch {
                repo.setMarker(it)
            }
        }


    }

    private fun getMarkerByLatitudeAndLongitudeFromLocalStorage(
        latitude: Double,
        longitude: Double
    ) {
        liveDataToObserveSingle.value = AppStateSingle.Loading
        cancelJob()
        viewModelCoroutineScope.launch {
            marker = repo.getDataByLatitudeAndLongitude(latitude, longitude)
            marker?.let {
                liveDataToObserveSingle.postValue(AppStateSingle.Success(it))
            }

        }
    }

    private fun getAllMarkersFromLocalStorage() {
        liveDataToObserve.value = AppState.Loading
        cancelJob()
        viewModelCoroutineScope.launch {
            liveDataToObserve.postValue(AppState.Success(repo.getMarkers()))
        }
    }
}