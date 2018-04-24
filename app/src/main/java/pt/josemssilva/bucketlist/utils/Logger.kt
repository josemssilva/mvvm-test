package pt.josemssilva.bucketlist.utils

import android.app.Application
import android.util.Log
import java.lang.Exception

class Logger(val app: Application) {

    private val TAG = app.javaClass.canonicalName
    private val TAG_E = TAG.plus("_E")

    fun log(message: String) {
        Log.d(TAG, message)
    }

    fun log(e: Exception) {
        Log.e(TAG_E, e.localizedMessage)
    }
}