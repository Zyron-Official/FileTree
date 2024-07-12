package com.zyron.filetree

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zyron.filetree.ui.FileTreeAdapter
import com.zyron.filetree.ui.FileTreeClickListener
import com.zyron.filetree.extensions.IntendedFileIconProvider
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private const val ROOT_DIR = "/storage/emulated/0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            // Check permission for Android 10 and below
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestStoragePermission()
            } else {
                initializeFileTree()
            }
        } else {
            // Check permission for Android 11 and above with new storage access
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
                // Permission denied (or only temporarily granted)
                if (shouldShowRequestPermissionRationale(Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
                    // Explain why permission is needed and ask again
                    Toast.makeText(this, "Storage access is required to browse files. Please grant permission.", Toast.LENGTH_SHORT).show()
                    requestStoragePermission()
                } else {
                    // User selected "Don't ask again," guide to settings
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
        //val fileTree = FileTree(this, "/storage/emulated/0")
        val fileTree = FileTree(this, "/storage/emulated/0/AndroidIDEProjects/Falcon Studio")
        //val fileTree = FileTree(this, "/storage/emulated/0/AndroidIDEProjects/Falcon Studio/app/src/main/java/com/zyron/falconstudio/MainActivity.java"); 


        val iconChevronExpanded = ContextCompat.getDrawable(this, R.drawable.ic_chevron_expand)
        val iconChevronCollapsed = ContextCompat.getDrawable(this, R.drawable.ic_chevron_collapse)
        val fileIconProvider = IntendedFileIconProvider()

        val fileTreeAdapter = FileTreeAdapter(
            this,
            fileTree,
            fileIconProvider,
            iconChevronExpanded!!,
            iconChevronCollapsed!!,
            object : FileTreeClickListener {
                override fun onFileClick(file: File) {
                    Toast.makeText(this@MainActivity, "File clicked: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                }

                override fun onFolderClick(folder: File) {
                    Toast.makeText(this@MainActivity, "Folder clicked: ${folder.absolutePath}", Toast.LENGTH_SHORT).show()
                }
            }
        )

        val layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return true
            }
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = fileTreeAdapter

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
    
}