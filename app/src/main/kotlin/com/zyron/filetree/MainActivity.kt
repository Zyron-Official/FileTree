package com.zyron.filetree

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zyron.filetree.ui.FileTreeAdapter
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

// Example usage in MainActivity
public class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get references to the RecyclerView and other UI elements
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val folderIcon: Drawable = ContextCompat.getDrawable(this, R.drawable.ic_folder)!!
        val fileIcon: Drawable = ContextCompat.getDrawable(this, R.drawable.ic_file)!!

        // Create an instance of FileTree
        val fileTree = FileTree(this, File("/"))

        // Create an instance of FileTreeAdapter
        val adapter = FileTreeAdapter(this, fileTree, folderIcon, fileIcon, object : FileTreeClickListener {
            override fun onFileClick(file: File) {
                // Handle file click
                Toast.makeText(this@MainActivity, "File clicked: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            }

            override fun onFolderClick(folder: File) {
                // Handle folder click
                Toast.makeText(this@MainActivity, "Folder clicked: ${folder.absolutePath}", Toast.LENGTH_SHORT).show()
            }
        })

        // Set up the RecyclerView with the adapter and a suitable layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Load files in a separate thread
        Thread {
            // Load files from the root directory
            fileTree.loadTree()
            // Update the UI on the main thread
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }.start()
    }
}