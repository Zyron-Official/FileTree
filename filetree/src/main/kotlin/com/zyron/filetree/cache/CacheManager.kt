package com.zyron.filetree.cache

import android.content.Context
import android.util.Log
import com.zyron.filetree.Node

object CacheManager {
    private var fileCacheThread: Thread? = null
    private var viewHolderCacheThread: Thread? = null

    fun stopFileCacher() {
        fileCacheThread?.interrupt()
        fileCacheThread = null
    }

    fun startFileCacher(nodes: List<Node>, priority: Int? = null) {
        if (fileCacheThread == null) {
            fileCacheThread = Thread(FileCache(nodes)).apply {
                priority?.let { this.priority = it }
                start()
            }
        } else {
            Log.e(this::class.java.simpleName, "FileCacher is already running; this might cause issues")
        }
    }


}
