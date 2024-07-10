package com.zyron.filetree

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zyron.filetree.ui.FileTreeAdapter
import com.zyron.filetree.ui.FileTreeClickListener
import com.zyron.filetree.extensions.IntendedFileIconProvider
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        
        val fileTree = FileTree(this, "/storage/emulated/0/AndroidIDEProjects/Falcon Studio")
        
        val iconChevronExpanded = ContextCompat.getDrawable(this, R.drawable.ic_chevron_expand)
        
        val iconChevronCollapsed = ContextCompat.getDrawable(this, R.drawable.ic_chevron_collapse)
        
        val fileIconProvider = IntendedFileIconProvider()
        
        val fileTreeAdapter = FileTreeAdapter(this, fileTree, fileIconProvider, iconChevronExpanded!!, iconChevronCollapsed!!, object : FileTreeClickListener {
            override fun onFileClick(file: File) {
                Toast.makeText(this@MainActivity, "File clicked: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            }

            override fun onFolderClick(folder: File) {
                Toast.makeText(this@MainActivity, "Folder clicked: ${folder.absolutePath}", Toast.LENGTH_SHORT).show()
            }
        })
        
        
        val layoutManager = object : LinearLayoutManager(this) {
        override fun canScrollVertically(): Boolean {
        
                return true
            }
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = fileTreeAdapter

        fileTree.setAdapterUpdateListener(object : FileTreeAdapterUpdateListener {
            override fun onFileTreeUpdated() {
         
                runOnUiThread {
                    fileTreeAdapter.updateNodes(fileTree.getNodes())
                    fileTreeAdapter.notifyDataSetChanged() 
                }
            }
        })

        fileTree.loadTree() 
    }
}