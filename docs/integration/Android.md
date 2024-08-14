### Example (Android View-Based):

#### Define FileTreeView in Layout (XML)

```XML
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <com.zyron.filetree.widget.FileTreeView
        android:id="@+id/file_tree_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>
```

#### Setup FileTree in Activity or Fragment (Kotlin)

```kotlin 
import androidx.appcompat.app.AppCompatActivity
import com.zyron.filetree.FileTree
import com.zyron.filetree.widget.FileTreeView
import com.zyron.filetree.adapter.FileTreeAdapter 
import com.zyron.filetree.resources.FileIconProvider
import com.zyron.filetree.operationexecutor.FileOperationExecutor
import java.io.File

class MainActivity : AppCompatActivity() {

        val fileTreeView: FileTreeView = findViewById(R.id.file_tree_view)
        val fileIconProvider = FileIconProvider()
        val fileOperationExecutor = FileOperationExecutor(requireContext())
        fileTreeView.initializeFileTree("/storage/emulated/0", fileOperationExecutor, fileIconProvider, this)
}
```

### Setup EventListener Class for File Operations

The `FileTree` library provides Event Listeners for performing common file system operations by using FileTreeEventListener Interface.

```kotlin
import com.zyron.filetree.adapter.FileTreeEventListener 
import java.io.File

class FileOperationExecutor(private val context: Context) : FileTreeEventListener {

    override fun onFileClick(file: File) {
        Toast.makeText(context, "File clicked: ${file.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onFolderClick(folder: File) {
        Toast.makeText(context, "Folder clicked: ${folder.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onFileLongClick(file: File): Boolean {
        Toast.makeText(context, "File long-clicked: ${file.name}", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onFolderLongClick(folder: File): Boolean {
        Toast.makeText(context, "Folder long-clicked: ${folder.name}", Toast.LENGTH_SHORT).show()
        return true
    }
}
```

#### Define Icons in FileIconProvider Class(Kotlin)

```kotlin
import com.zyron.filetree.provider.FileTreeIconProvider
import java.io.File

class FileIconProvider : FileTreeIconProvider {

    override fun getChevronIcon(): Int {
        return R.drawable.ic_chevron
    }

    override fun getDefaultFolderIcon(): Int {
        return R.drawable.ic_folder
    }

    override fun getDefaultFileIcon(): Int {
        return R.drawable.ic_file
    }

    override fun getIconForFolder(folder: File): Int {
        return when (folder.name) {
            "app" -> R.drawable.ic_folder
            "src" -> R.drawable.ic_folder
            "kotlin" -> R.drawable.ic_folder
            "java" -> R.drawable.ic_folder
            "res" -> R.drawable.ic_folder
            else -> getDefaultFolderIcon()
        }
    }

    override fun getIconForFile(file: File): Int {
        return when (file.name) {
            "gradlew.bat" -> R.drawable.ic_gradlewbat
            "gradlew" -> R.drawable.ic_gradlew
            "settings.gradle" -> R.drawable.ic_gradle_settings
            "build.gradle" -> R.drawable.ic_gradle_build
            "gradle.properties" -> R.drawable.ic_gradle_properties
            else -> getIconForExtension(file.extension)
        }
    }

    override fun getIconForExtension(extension: String): Int {
        return when (extension) {
            "xml" -> R.drawable.ic_xml
            "java" -> R.drawable.ic_java
            "kt" -> R.drawable.ic_kotlin
            else -> getDefaultFileIcon()
        }
    }
}
```