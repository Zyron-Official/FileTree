package com.zyron.filetree

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zyron.filetree.adapter.FileTreeAdapter
import com.zyron.filetree.adapter.FileTreeClickListener
import com.zyron.filetree.provider.FileTreeIconProvider
import com.zyron.filetree.utils.Utils.runOnUiThread

class TreeView : RecyclerView {
    private val context: Context
    private var path: String? = null
    private var fileTree: FileTree? = null

    constructor(context: Context) : super(context) {
        this.context = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context = context
    }

    constructor(context: Context, attrs: AttributeSet?, defstyleAttrs: Int) : super(
        context, attrs, defstyleAttrs
    ) {
        this.context = context
    }


    fun init(path: String) {
        init(path, null,null)
    }

    fun init(path: String,listener: FileTreeClickListener?){
        init(path,listener,null)
    }

    fun init(
        path: String,
        listener: FileTreeClickListener?,
        fileTreeIconProvider: FileTreeIconProvider?
    ) {
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
                listener?.onTreeViewUpdate(startPosition, itemCount)
                runOnUiThread {
                    fileTreeAdapter.updateNodes(fileTree!!.getNodes())
                    fileTreeAdapter.notifyItemRangeChanged(startPosition, itemCount)
                }
            }
        })
    }

}