package com.example.android8_lesson1_homework.viewmodel

import com.example.android8_lesson1_homework.model.Marker

sealed class AppStateSingle {
    data class Success(val marker: Marker) : AppStateSingle()
    data class Error(val error: Throwable) : AppStateSingle()
    object Loading : AppStateSingle()
}