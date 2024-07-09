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
import com.zyron.filetree.ui.IndentedFileTreeLayoutManager
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val folderIcon: Drawable = ContextCompat.getDrawable(this, R.drawable.ic_folder)!!
        val fileIcon: Drawable = ContextCompat.getDrawable(this, R.drawable.ic_file)!!
        val chevronExpandedIcon: Drawable = ContextCompat.getDrawable(this, R.drawable.ic_chevron_expand)!!
        val chevronCollapsedIcon: Drawable = ContextCompat.getDrawable(this, R.drawable.ic_chevron_collapse)!!

        // 1. Create an instance of FileTree
        val fileTree = FileTree(this, "/storage/emulated/0/AndroidIDEProjects/Falcon Studio")

        // 2. Create an instance of FileTreeAdapter (passing the icons and FileTree)
        val adapter = FileTreeAdapter(this, fileTree, folderIcon, fileIcon, chevronExpandedIcon, chevronCollapsedIcon, object : FileTreeClickListener {
            override fun onFileClick(file: File) {
                Toast.makeText(this@MainActivity, "File clicked: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            }

            override fun onFolderClick(folder: File) {
                Toast.makeText(this@MainActivity, "Folder clicked: ${folder.absolutePath}", Toast.LENGTH_SHORT).show()
            }
        })
        

        // 3. Create the custom layout manager
        val layoutManager = object : LinearLayoutManager(this) {
        override fun canScrollVertically(): Boolean {
        
        return true
        }
        }// Pass adapter and RecyclerView

        // 4. Set up the RecyclerView with the adapter and layout manager
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        // 5. Set the adapter update listener in FileTree
        fileTree.setAdapterUpdateListener(object : FileTreeAdapterUpdateListener {
            override fun onFileTreeUpdated() {
                // Update the UI on the main thread
                runOnUiThread {
                    adapter.updateNodes(fileTree.getNodes())
                    adapter.notifyDataSetChanged() // Update the RecyclerView
                }
            }
        })

        // 6. Load files in a separate thread
        fileTree.loadTree() 
    }
}