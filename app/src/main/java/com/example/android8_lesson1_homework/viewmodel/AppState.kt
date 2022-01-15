package com.example.android8_lesson1_homework.viewmodel

import com.example.android8_lesson1_homework.model.Marker

sealed class AppState {
    data class Success(val markers: List<Marker>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}