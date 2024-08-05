package com.zyron.filetree

import android.util.Log
import java.io.File

data class Node(var file: File, var parent: Node? = null, var level: Int = 0) {
    var isExpanded: Boolean = false
    var childrenStartIndex: Int = 0
    var childrenEndIndex: Int = 0
    var childrenLoaded: Boolean = false

    fun sortNode(): List<Node> {
        val children = file.listFiles()?.asSequence()?.partition { it.isDirectory }
            ?.let { (directories, files) ->
                (directories.sortedBy { it.name.lowercase() } + files.sortedBy { it.name.lowercase() })
            } ?: emptyList()

        val newNodes = children.map { childFile ->
            Node(file = childFile, parent = this, level = level + 1)
        }


        return newNodes

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as Node
        return file.absolutePath == other.file.absolutePath
    }

    override fun hashCode(): Int {
        return file.absolutePath.hashCode()
    }
}

