package com.awesome.apps.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast

object NetworkHelper {
    fun isConnected(context: Context): Boolean {
        try {
            val cm = getConnectionManager(context)
            val ni = getNetworkInfo(cm)
            if (ni != null && ni.isConnected) if (ni.state == NetworkInfo.State.CONNECTED) return true
        } catch (e: Exception) {
            return false
        }
        return false
    }

    fun showErrorMessageOnUiThread(context: Context?, errorMessage: String?) {
        val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        val message = mHandler.obtainMessage(-1, errorMessage)
        message.sendToTarget()
    }

    private fun getConnectionManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private fun getNetworkInfo(connectivityManager: ConnectivityManager): NetworkInfo? {
        return connectivityManager.activeNetworkInfo
    }
}