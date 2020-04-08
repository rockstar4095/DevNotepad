package com.example.devnotepad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * Главный экран показывает список направлений для изучения, например: Java, Kotlin, Android и т.д.
 * */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
