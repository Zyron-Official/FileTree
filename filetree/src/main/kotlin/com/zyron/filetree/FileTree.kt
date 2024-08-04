package com.zyron.filetree

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import kotlin.collections.*
import java.io.File
import java.nio.file.*

interface FileTreeAdapterUpdateListener {
    fun onFileTreeUpdated(startPosition: Int, itemCount: Int)
}

data class FileTreeNode(var file: File, var parent: FileTreeNode? = null, var level: Int = 0) {
    var isExpanded: Boolean = false
    var childrenStartIndex: Int = 0
    var childrenEndIndex: Int = 0
    var childrenLoaded: Boolean = false
}

class FileTree(private val context: Context, private val rootDirectory: String) {

    private val nodes: MutableList<FileTreeNode> = mutableListOf()
    private val expandedNodes: MutableSet<FileTreeNode> = mutableSetOf()
    private var adapterUpdateListener: FileTreeAdapterUpdateListener? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var loading = false

    fun initializeFileTree {
        val file = File(rootDirectory)
        val rw = file.canRead() && file.canWrite()
        if (!file.exists() || !rw){
            Log.e("FileTree","Provided path : $rootDirectory is invalid or does not exist")
            Log.d("FileTree","continuing anyways...")
        }
        scope.coroutineContext[Job]?.invokeOnCompletion {
            if (it is CancellationException) {
            }
        }
    }

    fun setAdapterUpdateListener(listener: FileTreeAdapterUpdateListener) {
        this.adapterUpdateListener = listener
    }

    fun loadFileTree() {
        scope.launch {
            if (!loading) {
                loading = true
                val rootPath = Paths.get(rootDirectory)
                val rootNode = FileTreeNode(rootPath.toFile())
                addNode(rootNode)
                expandNode(rootNode)
            }
        }
    }

    fun getNodes(): List<FileTreeNode> = nodes

    fun getExpandedNodes(): Set<FileTreeNode> = expandedNodes

    private suspend fun addNode(node: FileTreeNode, parent: FileTreeNode? = null) {
    
        node.parent = parent
        nodes.add(node)
        withContext(Dispatchers.Main) {
            adapterUpdateListener?.onFileTreeUpdated(0, 1)
        }
    }

    fun expandNode(node: FileTreeNode) {
        if (!node.isExpanded && Files.isDirectory(node.file.toPath())) {
            node.isExpanded = true

            scope.launch {
                expandedNodes.add(node)

                val children = node.file.listFiles()?.asSequence()
                    ?.partition { it.isDirectory }
                    ?.let { (directories, files) ->
                        (directories.sortedBy { it.name.lowercase() } + files.sortedBy { it.name.lowercase() })
                    }
                    ?: emptyList()

                if (children.isNotEmpty()) {
                    val insertIndex = nodes.indexOf(node) + 1
                    node.childrenStartIndex = insertIndex
                    node.childrenEndIndex = insertIndex + children.size

                    val newNodes = children.map { childFile ->
                        FileTreeNode(file = childFile, parent = node, level = node.level + 1)
                    }

                    nodes.addAll(insertIndex, newNodes)

                    withContext(Dispatchers.Main) {
                        adapterUpdateListener?.onFileTreeUpdated(
                            node.childrenStartIndex,
                            children.size
                        )
                    }
                }
            }
        }
    }

    private fun collectAllChildren(node: FileTreeNode, nodesToRemove: MutableList<FileTreeNode>) {
        val children = nodes.filter { it.parent == node }
        nodesToRemove.addAll(children)
        children.forEach { childNode ->
            collectAllChildren(childNode, nodesToRemove)
        }
    }

    fun collapseNode(node: FileTreeNode) {
        if (node.isExpanded) {
            node.isExpanded = false

            scope.launch {
                expandedNodes.remove(node)
                val nodesToRemove = mutableListOf<FileTreeNode>()
                collectAllChildren(node, nodesToRemove)

                nodes.removeAll(nodesToRemove)

                withContext(Dispatchers.Main) {
                    adapterUpdateListener?.onFileTreeUpdated(0, 1)
                }
            }
        }
    }

    fun cancelAllCoroutines() {
        scope.cancel()
    }

    fun onDestroy() {
        scope.coroutineContext[Job]?.cancelChildren()
    }
}