### Example (Android View-Based):

#### Define RecyclerView in XML Layout

```XML
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>
```

#### Setup FileTree in Activity or Fragment 

```kotlin 
import androidx.recyclerview.widget.RecyclerView 
import androidx.recyclerview.widget.LinearLayoutManager 
import com.zyron.filetree.adapter.FileTreeAdapter 
import com.zyron.filetree.FileTree
import java.io.File

class MainActivity : AppCompatActivity(), FileTreeClickListener {

        private fun initializeFileTree(fileTree: FileTree) {
        val fileTree = FileTree(this, "storage/emulated/0")
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val fileTreeIconProvider = IntendedFileIconProvider()
        val fileTreeAdapter = FileTreeAdapter(this, fileTree, fileTreeIconProvider, this)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = fileTreeAdapter
        fileTree.loadFileTree()
        fileTree.setAdapterUpdateListener(object : FileTreeAdapterUpdateListener {
            override fun onFileTreeUpdated(startPosition: Int, itemCount: Int) {
                runOnUiThread {
                    fileTreeAdapter.updateNodes(fileTree.getNodes())
                    fileTreeAdapter.notifyItemRangeChanged(startPosition, itemCount)
                }
            }
        })
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
```

#### Define Icons in FileIconProvider class

```Kotlin
import com.zyron.filetree.provider.FileTreeIconProvider
import java.io.File

class FileIconProvider : FileTreeIconProvider {

    override fun getChevronExpandIcon(): Int {
        return R.drawable.ic_chevron_expand
    }

    override fun getChevronCollapseIcon(): Int {
        return R.drawable.ic_chevron_collapse
    }

    override fun getFolderIcon(): Int {
        return R.drawable.ic_folder
    }

    override fun getDefaultFileIcon(): Int {
        return R.drawable.ic_file
    }

    override fun getIconForFile(file: File): Int {
        return when (file.name) {
            "gradlew.bat" -> R.drawable.ic_file
            "gradlew" -> R.drawable.ic_file
            "settings.gradle" -> R.drawable.ic_file
            "build.gradle" -> R.drawable.ic_file
            "gradle.properties" -> R.drawable.ic_file
            else -> getIconForExtension(file.extension)
        }
    }

    override fun getIconForExtension(extension: String): Int {
        return when (extension) {
            "xml" -> R.drawable.ic_file
            "java" -> R.drawable.ic_file
            "kt" -> R.drawable.ic_file
            else -> getDefaultFileIcon()
        }
    }
}
```