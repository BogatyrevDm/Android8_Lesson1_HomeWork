package com.example.android8_lesson1_homework.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android8_lesson1_homework.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MapsFragment.newInstance())
                .commitAllowingStateLoss()
        }
    }
}