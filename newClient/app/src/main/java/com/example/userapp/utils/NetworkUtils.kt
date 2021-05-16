package com.example.userapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


class NetworkUtil {

    private val DEBUG_TAG = "NetworkStatusExample"

    fun isNetworkAvailable(context: Context): Boolean {
        var result : Boolean = false
        val connectivityManager: ConnectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //기기 버전체크(마시멜로)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let { actingnetwork ->
                if (actingnetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actingnetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    ||actingnetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) result = true }
        }
        else {
            connectivityManager.run{
                connectivityManager.activeNetworkInfo?.let {
                    result = when (it.type){
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }

/*    fun isNetworkConnected(context: Context): Boolean {

        val connectivityManager: ConnectivityManager? =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        connectivityManager?.let { cm ->
            cm.getNetworkCapabilities(connectivityManager.activeNetwork)?.let { nc ->
                if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    ||nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) networkconnected = isNetworkConnected(context)
            }
        }
        return networkconnected
    }*/

    fun showNoconnectedException() : String {
        return "인터넷 연결 끊겼습니다. WIFI나 데이터 연결을 확인해주세요"
    }

}

enum class networktype {
    TRANSPORT_CELLULAR, TRANSPORT_WIFI, TRANSPORT_ETHERNET
}


//Deprecated
/*       var isWifiConn: Boolean = false
        var isMobileConn: Boolean = false
        connectivityManager.allNetworks.forEach { network ->
            connectivityManager.getNetworkInfo(network)?.apply {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn = isWifiConn or isConnected
                }
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn = isMobileConn or isConnected
                }
            }
        }*/






