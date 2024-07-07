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
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val folderIcon: Drawable = ContextCompat.getDrawable(this, R.drawable.ic_folder)!!
        val fileIcon: Drawable = ContextCompat.getDrawable(this, R.drawable.ic_file)!!

        // 1. Create an instance of FileTree
        val fileTree = FileTree(this, File("/storage/emulated/0/AppProjects/Android 11"))

        // 2. Create an instance of FileTreeAdapter (passing the icons and FileTree)
        val adapter = FileTreeAdapter(this, fileTree, folderIcon, fileIcon, object : FileTreeClickListener {
            override fun onFileClick(file: File) {
                Toast.makeText(this@MainActivity, "File clicked: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            }

            override fun onFolderClick(folder: File) {
                Toast.makeText(this@MainActivity, "Folder clicked: ${folder.absolutePath}", Toast.LENGTH_SHORT).show()
            }
        })

        // 3. Set up the RecyclerView with the adapter and a suitable layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 4. Set the adapter update listener in FileTree 
        fileTree.setAdapterUpdateListener(object : FileTreeAdapterUpdateListener {
            override fun onFileTreeUpdated() {
                // Update the UI on the main thread
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }
        })

        // 5. Load files in a separate thread
        fileTree.loadTree() // Call loadTree()
    }
}