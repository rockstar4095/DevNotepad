package com.example.devnotepad.ui.activity_main

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.devnotepad.R
import com.example.devnotepad.ui.ViewModelProviderFactory
import com.example.devnotepad.ui.fragment_directions.DirectionsFragment
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    private lateinit var mainActivityViewModel: MainActivityViewModel

    companion object {
        private const val DIRECTIONS_FRAGMENT_TAG = "directions fragment tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mainActivityViewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainActivityViewModel::class.java)

        setMainFragment()

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.customView = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()

                if (isDirectionsFragmentVisible()) {
                    makeHomeButtonDisabled()
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Проверяет, находится ли в данный момент на экране фрагмент с направлениями.
     * */
    private fun isDirectionsFragmentVisible(): Boolean {
        return supportFragmentManager.findFragmentByTag(DIRECTIONS_FRAGMENT_TAG)!!.isVisible
    }

    /**
     * Делает home button недоступной.
     * */
    private fun makeHomeButtonDisabled() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    /**TODO: временно устанавливает главный фрагмент.*/
    private fun setMainFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val directionsFragment = DirectionsFragment()
        fragmentTransaction.replace(R.id.fragmentContainer, directionsFragment, DIRECTIONS_FRAGMENT_TAG)
        fragmentTransaction.commit()
    }
}
