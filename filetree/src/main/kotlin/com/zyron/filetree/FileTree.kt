package com.zyron.filetree

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.zyron.filetree.model.FileTreeNode
import kotlinx.coroutines.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.coroutines.CoroutineContext

interface FileTreeAdapterUpdateListener {
    fun onFileTreeUpdated()
}

class FileTree(private val context: Context, private val rootDirectory: String) {

    private val nodes: MutableList<FileTreeNode> = mutableListOf()
    private val expandedNodes: MutableSet<FileTreeNode> = mutableSetOf()
    private var adapterUpdateListener: FileTreeAdapterUpdateListener? = null
    private var loading = false
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun loadTree() {
        if (!loading) {
            loading = true
                val rootPath = Paths.get(rootDirectory)
                addNode(FileTreeNode(rootPath.toFile()))
                adapterUpdateListener?.onFileTreeUpdated()
        }
    }

    fun setAdapterUpdateListener(listener: FileTreeAdapterUpdateListener) {
        this.adapterUpdateListener = listener
    }

    private fun addNode(node: FileTreeNode, parent: FileTreeNode? = null) {
        node.parent = parent
        nodes.add(node)
    }

    fun getNodes(): List<FileTreeNode> {
    return nodes
}

fun getExpandedNodes(): Set<FileTreeNode> {
    return expandedNodes
}

fun expandNode(node: FileTreeNode) {
    if (!node.isExpanded && Files.isDirectory(node.file.toPath())) {
        node.isExpanded = true
        expandedNodes.add(node)
        // Load child nodes
        Files.list(node.file.toPath())?.forEach { path ->
            val file = path.toFile()
            val childNode = FileTreeNode(file, parent = node)
            // Find the index of the parent node
            val parentIndex = nodes.indexOf(node)
            // Only proceed if the parent node is found
            if (parentIndex != -1) {
                // Insert the child node at the correct position (after the parent)
                nodes.add(parentIndex + 1, childNode) // Insert after the parent
            }
        }
        // Notify the adapter for UI update
        adapterUpdateListener?.onFileTreeUpdated()
    }
}

fun collapseNode(node: FileTreeNode) {
    node.isExpanded = false
    expandedNodes.remove(node)
    // Remove all children and their sub-nodes recursively
    val nodesToRemove = mutableListOf<FileTreeNode>()
    collectAllChildren(node, nodesToRemove)
    nodes.removeAll(nodesToRemove)
    adapterUpdateListener?.onFileTreeUpdated()
}

private fun collectAllChildren(node: FileTreeNode, nodesToRemove: MutableList<FileTreeNode>) {
    val children = nodes.filter { it.parent == node }
    nodesToRemove.addAll(children)
    children.forEach { childNode ->
        collectAllChildren(childNode, nodesToRemove)
    }
}
/*

fun collapseNode(node: FileTreeNode) {
    node.isExpanded = false
    expandedNodes.remove(node)
    nodes.removeAll { it.parent === node }
    adapterUpdateListener?.onFileTreeUpdated()
}*/

    /*  fun collapseNode(node: FileTreeNode) {
        node.isCollapsed = true
        expandedNodes.remove(node)
        nodes.removeAll { it.parent === node }
        adapterUpdateListener?.onFileTreeUpdated()
    }*/
    
     /*   fun collapseNode(node: FileTreeNode) {
        node.isExpanded = false
        expandedNodes.remove(node)

        // Remove only the direct children of the collapsed node
        nodes.removeAll { it.parent === node } // Check for expanded children

        adapterUpdateListener?.onFileTreeUpdated()
    }*/
    

    
}
/*
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
}*/






