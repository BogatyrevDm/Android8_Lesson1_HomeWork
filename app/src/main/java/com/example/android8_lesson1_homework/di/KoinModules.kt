package com.example.android8_lesson1_homework.di

import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.android8_lesson1_homework.model.Repository
import com.example.android8_lesson1_homework.model.RepositoryImp
import com.example.android8_lesson1_homework.room.MarkersDataBase
import com.example.android8_lesson1_homework.viewmodel.AppState
import com.example.android8_lesson1_homework.viewmodel.AppStateSingle
import com.example.android8_lesson1_homework.viewmodel.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val application = module {
    single { Room.databaseBuilder(get(), MarkersDataBase::class.java, "MarkersDB").build() }
    single { get<MarkersDataBase>().markersDao() }
    single(named("default")) { MutableLiveData<AppState>() }
    single(named("single")) { MutableLiveData<AppStateSingle>() }
    single<Repository> { RepositoryImp(get()) }
    viewModel { MainViewModel(get(named("default")), get(named("single")), get()) }
}
