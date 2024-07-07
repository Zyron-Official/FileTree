package com.zyron.filetree

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zyron.filetree.model.FileTreeNode
import com.zyron.filetree.operations.FileOperationExecutor
import com.zyron.filetree.ui.FileTreeAdapter
import com.zyron.filetree.utils.EditTextDialog
import java.io.File
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

interface FileTreeAdapterUpdateListener {
    fun onFileTreeUpdated()
}

class FileTree(private val context: Context, private val rootDirectory: File) {

    internal val nodes: MutableList<FileTreeNode> = mutableListOf()
    private val expandedNodes: MutableSet<FileTreeNode> = mutableSetOf()
    private val fileOperationExecutor = FileOperationExecutor()
    private var adapterUpdateListener: FileTreeAdapterUpdateListener? = null

    private var loading = false 

    fun loadTree() {
        if (!loading) {
            loading = true
            addNode(FileTreeNode(rootDirectory)) // Add the root node
            adapterUpdateListener?.onFileTreeUpdated() // Notify the adapter
        }
    }

    fun setAdapterUpdateListener(listener: FileTreeAdapterUpdateListener) {
        this.adapterUpdateListener = listener
    }

    private fun addNode(node: FileTreeNode, parent: FileTreeNode? = null) {
        node.parent = parent
        nodes.add(node)
    }

    private fun loadChildNodes(node: FileTreeNode) {
        if (node.file.isDirectory) {
            // Use a background thread to load child nodes
            Thread {
                node.file.listFiles()?.forEach {
                    addNode(FileTreeNode(it, parent = node))
                    loadChildNodes(FileTreeNode(it, parent = node)) // Recursive call
                    // Notify the adapter on the main thread
                    Handler(Looper.getMainLooper()).post {
                        adapterUpdateListener?.onFileTreeUpdated()
                    }
                }
            }.start()
        }
    }

    fun getNodes(): List<FileTreeNode> {
        return nodes
    }

    fun getExpandedNodes(): Set<FileTreeNode> {
        return expandedNodes
    }

    fun expandNode(node: FileTreeNode) {
        if (!node.isExpanded && node.file.isDirectory) {
            node.isExpanded = true
            expandedNodes.add(node)
            loadChildNodes(node)
        }
    }

    fun collapseNode(node: FileTreeNode) {
        node.isExpanded = false
        expandedNodes.remove(node)
        nodes.removeAll { it.parent === node }
        adapterUpdateListener?.onFileTreeUpdated()
    }

    // File operations using coroutines
    fun copyFile(source: File, destination: File) {
        fileOperationExecutor.copyFile(source, destination)
    }

    fun moveFile(source: File, destination: File) {
        fileOperationExecutor.moveFile(source, destination)
    }

    fun deleteFile(file: File) {
        fileOperationExecutor.deleteFile(file)
    }

    fun renameFile(file: File, newName: String) {
        fileOperationExecutor.renameFile(file, newName)
    }

    fun createFile(parent: File, fileName: String) {
        fileOperationExecutor.createFile(parent, fileName)
    }

    fun createFolder(parent: File, folderName: String) {
        fileOperationExecutor.createFolder(parent, folderName)
    }
}