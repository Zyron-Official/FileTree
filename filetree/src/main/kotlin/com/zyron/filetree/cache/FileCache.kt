package com.zyron.filetree.cache

import android.util.Log
import com.zyron.filetree.Node
import java.io.File

class FileCache(nodes: List<Node>) : Runnable {
    val Xnodes: MutableList<Node> = mutableListOf()

    companion object {
        val fileMap = HashMap<Node, List<Node>>()
    }

    init {
        Xnodes.addAll(nodes)
    }

    private fun job(nodes: List<Node>){
        for (node in nodes) {
            println(node.file.name)
            val nodelist = node.sortNode().also { Thread{job(it)}.start() }
            synchronized(fileMap){
                fileMap[node] = nodelist
            }
        }
    }

    override fun run() {
        job(Xnodes)
    }

}