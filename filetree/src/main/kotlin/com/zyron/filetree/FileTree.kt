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

// FileTree class for managing the tree structure
class FileTree(private val context: Context, private val rootDirectory: File) {

    private val nodes: MutableList<FileTreeNode> = mutableListOf()
    private val expandedNodes: MutableSet<FileTreeNode> = mutableSetOf()
    private val fileOperationExecutor = FileOperationExecutor()

    private var loading = false // Flag for loading state

    fun loadTree() {
        if (!loading) {
            loading = true
            addNode(FileTreeNode(rootDirectory)) // Add the root node
            loadChildNodes(FileTreeNode(rootDirectory)) // Load child nodes of the root
            loading = false // Reset loading flag after initial loading
        }
    }

    // Add a single node to the list
    private fun addNode(node: FileTreeNode, parent: FileTreeNode? = null) {
        node.parent = parent
        nodes.add(node)
    }

    // Recursive function to load child nodes for directories
    private fun loadChildNodes(node: FileTreeNode) {
        if (node.file.isDirectory) {
            node.file.listFiles()?.forEach {
                addNode(FileTreeNode(it, parent = node))
                loadChildNodes(FileTreeNode(it, parent = node)) // Recursively load child nodes
            }
        }
    }

    // Public getter for the 'nodes' list
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
            // Lazy loading of children
            loadChildNodes(node) 
        }
    }

    fun collapseNode(node: FileTreeNode) {
        node.isExpanded = false
        expandedNodes.remove(node)
        // Remove child nodes
        nodes.removeAll { it.parent === node }
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