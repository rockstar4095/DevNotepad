package com.example.devnotepad.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.MutableLiveData

class InternetConnectionChecker(val context: Context) {

    companion object {
        var isInternetConnected: MutableLiveData<Boolean> = MutableLiveData()
    }

    init {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder().build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isInternetConnected.postValue(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isInternetConnected.postValue(false)
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        getConnection()
    }

    private fun getConnection() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        isInternetConnected.postValue(networkInfo != null && networkInfo.isConnected)
    }
}