package com.example.devnotepad.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData

class InternetConnectionChecker(val context: Context) {

    private var isInternetConnected: Boolean = false

    companion object {
        var isInternetConnectedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    }

    init {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder().build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                handleNetworkStateChange(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                handleNetworkStateChange(false)
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        getConnection()
    }

    private fun handleNetworkStateChange(isInternetConnected: Boolean) {
        if (isInternetConnected != this.isInternetConnected) {
            isInternetConnectedLiveData.postValue(isInternetConnected)
            this.isInternetConnected = isInternetConnected
        }
    }

    private fun getConnection() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        isInternetConnectedLiveData.postValue(networkInfo != null && networkInfo.isConnected)
    }
}