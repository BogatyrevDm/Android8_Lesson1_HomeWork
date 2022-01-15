package com.example.android8_lesson1_homework.application

import android.app.Application
import com.example.android8_lesson1_homework.di.application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Android8Lesson1HomeWorkApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(listOf(application))
        }
    }
}