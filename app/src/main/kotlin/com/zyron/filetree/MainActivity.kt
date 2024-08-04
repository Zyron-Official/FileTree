package com.zyron.filetree

import android.Manifest
import android.content.*
import android.content.pm.*
import android.net.*
import android.os.*
import android.provider.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.*
import androidx.core.app.*
import androidx.core.view.*
import androidx.core.content.*
import androidx.recyclerview.widget.*
import androidx.drawerlayout.widget.*
import com.google.android.material.navigation.NavigationView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.zyron.filetree.FileTreeView
import com.zyron.filetree.adapter.FileTreeAdapter 
import com.zyron.filetree.adapter.FileTreeClickListener 
import java.io.File

class MainActivity : AppCompatActivity(), FileTreeClickListener {

companion object {
    private const val REQUEST_EXTERNAL_STORAGE = 1
    private const val REQUEST_DIRECTORY_SELECTION = 2
}

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var fileTreeView: FileTreeView
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        
        fileTreeView = findViewById(R.id.file_tree_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        toolbar.setTitle("FileTree")
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        setupOnClickListeners()
        checkPermission()
    }

    fun setupOnClickListeners() {  
        val selectDirectory: MaterialButton = findViewById(R.id.select_directory)
        selectDirectory.setOnClickListener {
        selectDirectory()
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestStoragePermission()
            }
        } else {
            if (!Environment.isExternalStorageManager()) {
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

    private fun selectDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, REQUEST_DIRECTORY_SELECTION)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_DIRECTORY_SELECTION -> {
                if (resultCode == RESULT_OK && data != null) {
                    val treeUri = data.data
                    val path = treeUri?.path?.replace("/tree/primary:", "/storage/emulated/0/")
                    if (path != null) {
                        fileTreeView.init(path,this)
                    }else{
                        Toast.makeText(this,"File Path is null", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
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

    override fun onTreeViewUpdate(startPosition: Int, itemCount: Int) {
        println("FileTree updated")
    }
}