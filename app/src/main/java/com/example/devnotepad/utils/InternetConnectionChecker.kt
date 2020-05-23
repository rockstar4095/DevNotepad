package com.example.devnotepad.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData
import com.example.devnotepad.BaseApplication

class InternetConnectionChecker(val context: Context) {

    companion object {
        var isInternetConnectedLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

        fun registerNetworkCallbacks() {
            val connectivityManager = BaseApplication.baseApplication.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkRequest = NetworkRequest.Builder().build()
            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    isInternetConnectedLiveData.postValue(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    isInternetConnectedLiveData.postValue(false)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    isInternetConnectedLiveData.postValue(false)
                }
            }

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }
}