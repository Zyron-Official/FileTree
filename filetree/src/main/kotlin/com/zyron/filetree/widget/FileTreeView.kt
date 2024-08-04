package com.zyron.filetree.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zyron.filetree.FileTree
import com.zyron.filetree.FileTreeAdapterUpdateListener
import com.zyron.filetree.utils.Utils.runOnUiThread
import com.zyron.filetree.adapter.FileTreeAdapter
import com.zyron.filetree.provider.FileTreeIconProvider
import com.zyron.filetree.interfaces.FileTreeEventListener

class FileTreeView : RecyclerView {

    private val context: Context
    private var path: String? = null
    private var fileTree: FileTree? = null

    constructor(context: Context) : super(context) {
        this.context = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context = context
    }

    constructor(context: Context, attrs: AttributeSet?, defstyleAttrs: Int) : super(context, attrs, defstyleAttrs) {
        this.context = context
    }

    fun initializeFileTree(path: String) {
        initializeFileTree(path, null,null)
    }

    fun initializeFileTree(path: String,listener: FileTreeEventListener?){
        initializeFileTree(path,listener,null)
    }

    fun initializeFileTree(path: String, listener: FileTreeEventListener?, fileTreeIconProvider: FileTreeIconProvider?) {
        this.path = path
        fileTree = FileTree(context, path)

        val fileTreeAdapter = if (listener == null) {
            FileTreeAdapter(context, fileTree!!)
        } else {
            if (fileTreeIconProvider != null) {
                FileTreeAdapter(context, fileTree!!,fileTreeIconProvider, listener)
            }else{
                FileTreeAdapter(context, fileTree!!, listener)
            }
        }

        layoutManager = LinearLayoutManager(context)
        adapter = fileTreeAdapter
        fileTree!!.loadFileTree()
        fileTree!!.setAdapterUpdateListener(object : FileTreeAdapterUpdateListener {
            override fun onFileTreeUpdated(startPosition: Int, itemCount: Int) {
                listener?.onFileTreeViewUpdated(startPosition, itemCount)
                runOnUiThread {
                    fileTreeAdapter.updateNodes(fileTree!!.getNodes())
                    fileTreeAdapter.notifyItemRangeChanged(startPosition, itemCount)
                }
            }
        })
    }
}