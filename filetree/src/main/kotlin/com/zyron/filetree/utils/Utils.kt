package com.zyron.filetree.utils

import android.os.Handler
import android.os.Looper

object Utils {
    private val handler = Handler(Looper.getMainLooper())
    fun runOnUiThread(runnable: Runnable){
        handler.post(runnable)
    }
}