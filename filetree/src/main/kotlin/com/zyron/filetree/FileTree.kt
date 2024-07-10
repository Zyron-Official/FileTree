package com.zyron.filetree

import android.content.Context
import com.zyron.filetree.model.FileTreeNode
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

import android.util.Log

interface FileTreeAdapterUpdateListener {
    fun onFileTreeUpdated()
}

class FileTree(private val context: Context, private val rootDirectory: String) {

    private val nodes: MutableList<FileTreeNode> = mutableListOf()
    private val expandedNodes: MutableSet<FileTreeNode> = mutableSetOf()
    private var adapterUpdateListener: FileTreeAdapterUpdateListener? = null
    private var loading = false
    private val executor: ThreadPoolExecutor = ThreadPoolExecutor(2, 4, 120L, TimeUnit.SECONDS, LinkedBlockingQueue())

    fun loadTree() {
        if (!loading) {
            loading = true
            val rootPath = Paths.get(rootDirectory)
            addNode(FileTreeNode(rootPath.toFile()))
            adapterUpdateListener?.onFileTreeUpdated()
            loading = false
        }
    }

    fun setAdapterUpdateListener(listener: FileTreeAdapterUpdateListener) {
        this.adapterUpdateListener = listener
    }

    private fun addNode(node: FileTreeNode, parent: FileTreeNode? = null) {
        executor.execute {
            node.parent = parent
            nodes.add(node)
            adapterUpdateListener?.onFileTreeUpdated()
        }
    }

    fun getNodes(): List<FileTreeNode> {
        return nodes
    }

    fun getExpandedNodes(): Set<FileTreeNode> {
        return expandedNodes
    }

 /*   fun expandNode(node: FileTreeNode) {
        if (!node.isExpanded && Files.isDirectory(node.file.toPath())) {
            node.isExpanded = true
            expandedNodes.add(node)
            Files.list(node.file.toPath())?.forEach { path ->
                val file = path.toFile()
                val childNode = FileTreeNode(file, parent = node)
                val parentIndex = nodes.indexOf(node)
                if (parentIndex != -1) {
                    nodes.add(parentIndex + 1, childNode)
                }
            }
            adapterUpdateListener?.onFileTreeUpdated()
        }
    }*/
    
   fun expandNode(node: FileTreeNode) {
    if (!node.isExpanded && node.file.isDirectory) {
        node.isExpanded = true
        expandedNodes.add(node)

        val children = node.file.listFiles()?.toList() ?: emptyList()

        // Add ALL children (directories and files) to the nodes list:
        var insertIndex = nodes.indexOf(node) + 1
    children.forEach { childFile ->
        val childNode = FileTreeNode(
            file = childFile, 
            parent = node, 
            level = node.level + 1 // Set child's level
        )
        nodes.add(insertIndex++, childNode)
    }

        adapterUpdateListener?.onFileTreeUpdated()
    }
}

    fun collapseNode(node: FileTreeNode) {
        node.isExpanded = false
        expandedNodes.remove(node)
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