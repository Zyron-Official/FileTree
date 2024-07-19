package com.zyron.filetree

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.zyron.filetree.adapter.FileTreeAdapter
import com.zyron.filetree.adapter.FileTreeClickListener
import com.zyron.filetree.extensions.IntendedFileIconProvider 
import java.io.File

class MainActivity : AppCompatActivity(), FileTreeClickListener {
    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private const val ROOT_DIR = "/storage/emulated/0"
    }
    
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
    }
    
    private fun disableDrawerSwipe() {
    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestStoragePermission()
            } else {
                initializeFileTree()
            }
        } else {
            if (Environment.isExternalStorageManager()) {
                initializeFileTree()
            } else {
                requestAllFilesAccess()
            }
        }
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE)
    }

    private fun requestAllFilesAccess() {
        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeFileTree()
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Storage access is required to browse files. Please grant permission.", Toast.LENGTH_SHORT).show()
                    requestStoragePermission()
                } else {
                    Toast.makeText(this, "Permission denied. Please allow storage access in App Settings.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:" + packageName)
                    startActivity(intent)
                }
            }
        }
    }


    private fun initializeFileTree() {
    val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
    val fileTree = FileTree(this, "/storage/emulated/0/AndroidIDEProjects/Asset Studio")
    val fileTreeIconProvider = IntendedFileIconProvider()
    val fileTreeAdapter = FileTreeAdapter(this, fileTree, fileTreeIconProvider, this) // Pass 'this' as FileTreeClickListener

    val layoutManager = LinearLayoutManager(this) 

    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = fileTreeAdapter
    recyclerView.setItemViewCacheSize(100)

    fileTree.setAdapterUpdateListener(object : FileTreeAdapterUpdateListener {
        override fun onFileTreeUpdated(startPosition: Int, itemCount: Int) {
            runOnUiThread {
                fileTreeAdapter.updateNodes(fileTree.getNodes())
                fileTreeAdapter.notifyItemRangeChanged(startPosition, itemCount)
            }
        }
    })

    fileTree.loadTree()
    
    } 
   
    override fun onFileClick(file: File) {
        Toast.makeText(this, "File clicked: ${file.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onFolderClick(folder: File) {
        Toast.makeText(this, "Folder clicked: ${folder.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onFileLongClick(file: File): Boolean {
        Toast.makeText(this, "File long-clicked: ${file.name}", Toast.LENGTH_SHORT).show()
        return true 
    }

    override fun onFolderLongClick(folder: File): Boolean {
        Toast.makeText(this, "Folder long-clicked: ${folder.name}", Toast.LENGTH_SHORT).show()
        return true 
    }
}