package com.zyron.filetree

import android.content.Context
import android.util.Log
import com.zyron.filetree.cache.CacheManager
import com.zyron.filetree.cache.FileCache
import com.zyron.filetree.utils.Utils
import kotlinx.coroutines.*
import kotlin.collections.*
import java.io.File
import java.nio.file.*

interface FileTreeAdapterUpdateListener {
    fun onFileTreeUpdated(startPosition: Int, itemCount: Int)
}



class FileTree(private val context: Context, private val rootDirectory: String) {

    private val nodes: MutableList<Node> = mutableListOf()
    private val expandedNodes: MutableSet<Node> = mutableSetOf()
    private var adapterUpdateListener: FileTreeAdapterUpdateListener? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var loading = false


    init {
        val file = File(rootDirectory)
        val rw = file.canRead() && file.canWrite()
        if (!file.exists() || !rw){
            Log.e(this::class.java.simpleName,"Provided path : $rootDirectory is invalid or does not exist")
            Log.d(this::class.java.simpleName,"continuing anyways...")
        }
        scope.coroutineContext[Job]?.invokeOnCompletion {
            if (it is CancellationException) {
            }
        }

        scope.launch {
            if (!loading) {
                loading = true
                val rootPath = Paths.get(rootDirectory)
                val rootNode = Node(rootPath.toFile())
                addNode(rootNode)
                CacheManager.startFileCacher(nodes)
                expandNode(rootNode)
            }
        }

    }

    fun setAdapterUpdateListener(listener: FileTreeAdapterUpdateListener) {
        this.adapterUpdateListener = listener
    }



    fun getNodes(): List<Node> = nodes

    fun getExpandedNodes(): Set<Node> = expandedNodes

    private suspend fun addNode(node: Node, parent: Node? = null) {

        node.parent = parent
        nodes.add(node)
        withContext(Dispatchers.Main) {
            adapterUpdateListener?.onFileTreeUpdated(0, 1)
        }
    }

    fun expandNode(node: Node) {
        if (!node.isExpanded && Files.isDirectory(node.file.toPath())) {
            node.isExpanded = true

            scope.launch {
                expandedNodes.add(node)

                var newNodes:List<Node>?
                synchronized(FileCache.fileMap){
                    newNodes = FileCache.fileMap[node]
                }
                if (newNodes == null){
                    newNodes = node.sortNode()
                }



                if (newNodes!!.isNotEmpty()) {
                    val insertIndex = nodes.indexOf(node) + 1
                    node.childrenStartIndex = insertIndex
                    node.childrenEndIndex = insertIndex + newNodes!!.size

                    nodes.addAll(insertIndex, newNodes!!)

                    withContext(Dispatchers.Main) {
                        adapterUpdateListener?.onFileTreeUpdated(
                            node.childrenStartIndex,
                            newNodes!!.size
                        )
                    }
                }
            }
        }
    }

    private fun collectAllChildren(node: Node, nodesToRemove: MutableList<Node>) {
        val children = nodes.filter { it.parent == node }
        nodesToRemove.addAll(children)
        children.forEach { childNode ->
            collectAllChildren(childNode, nodesToRemove)
        }
    }

    fun collapseNode(node: Node) {
        if (node.isExpanded) {
            node.isExpanded = false

            scope.launch {
                expandedNodes.remove(node)
                val nodesToRemove = mutableListOf<Node>()
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