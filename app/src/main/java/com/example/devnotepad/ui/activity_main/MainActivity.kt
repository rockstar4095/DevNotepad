package com.example.devnotepad.ui.activity_main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.devnotepad.R
import com.example.devnotepad.ui.fragment_directions.DirectionsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        setMainFragment()
    }

    /**TODO: временно устанавливает главный фрагмент.*/
    private fun setMainFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragmentMain = DirectionsFragment()
        fragmentTransaction.replace(R.id.fragmentContainer, fragmentMain)
        fragmentTransaction.commit()
    }
}
