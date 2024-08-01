## FileTree: A Simplified File Explorer Library for IDEs and Code Editors.

This documentation guides you on how to integrate and use the `FileTree` library within your IDEs and Code Editors to provide a user-friendly and efficient file browsing experience.


### Table of Contents:


  **1. Introduction**

  **2. Integration**

   &#9702; 2.1. Dependencies

   &#9702; 2.2. Initialization and Loading

   &#9702; 2.3. UI Integration

   &#9702; 2.4. Event Handling

  **3. File Operations**

  **4. Customizing Icons**

  **5. Asynchronous File System**

  **6. Example (Android View-Based)**

  **7. Frequently Asked Questions (FAQ)**

  **8. License.**

## 1. Introduction

The `FileTree` library provides a robust and modular way to manage and display file trees within your IDEs and code editors. It offers the following key features:

- **Hierarchical File Tree Representation:** Presents files and folders in a hierarchical tree structure.

- **Hover-Navigated Directory**: Highlights directories as users navigate through them by clicking. This feature enhances user experience by visually indicating the current directory and providing a clear, interactive method for exploring the directory tree.

- **Lazy Loading:** Optimizes loading time by only loading child nodes when expanded.

- **Asynchronous File System:** Performs file functions like expanding, collapsing, loading files in `FileTree`, etc. in the background thread to avoid blocking the Main UI Thread.

- **Customizable Icons:** Allows you to set custom icons for files and folders.

- **Flexibility:** Integrates seamlessly with various IDE UI frameworks (Android Views, Swing, SWT, Jetpack Compose).

## 2. Integration
### 2.1. Dependencies

First, add the `FileTree` library as a dependency to your project using a build system like Maven or Gradle:
```groovy
implementation 'com.zyron.openapi.filetree:filetree:1.0.0'
```
The specific instructions for adding a dependency will depend on your IDE and build system.

### 2.2. Initialization and Loading

**1. Create an Instance:** Instantiate a `FileTree` object, providing the root directory:

```kotlin
val fileTree = FileTree(this, "/storage/emulated/0")
```

**2. Load the FileTree:** Call the `loadFileTree()` method to load the initial file tree structure. This will load the root directory and its immediate children:

```kotlin
fileTree.loadFileTree()
```
### 2.3. UI Integration

This step involves integrating the `FileTree` library with your IDE's UI framework. The specific implementation will vary depending on your chosen UI framework. Below are common frameworks with basic examples:

- **Android View-Based (Android/Kotlin/Java):** Use `RecyclerView` to display your `FileTree` data.

    Docs [Getting Sarted](docs/integration/Android.md)

- **Jetpack Compose (Android/Kotlin):** Define a composable that renders the file tree using your `FileTree` data. 

    Docs [Getting Sarted](docs/integration/Android-Jetpack-Compose.md)

- **Swing (Java):** Use a `JTree` component and create a custom TreeModel that uses the `FileTreeNode` data from your `FileTree` object. 

    Docs [Getting Sarted](docs/integration/Java-Swing.md)

- **SWT (Java):** Similar to Swing, use a `TreeViewer` and a custom `TreeContentProvider` to map your `FileTreeNode` data.

    Docs [Getting Sarted](docs/integration/Java-SWT.md)

### 2.4. Event Handling

The `FileTree` library provides events that you can use to react to user actions within the file tree:

Handle these events in your IDE's logic to perform actions like opening files, displaying properties, or refreshing UI elements.

## 3. File Operations

The `FileTree` library provides methods for performing common file system operations by using onClick and onLongClick Listeners.

```kotlin
import com.zyron.filetree.adapter.FileTreeClickListener 

class FileOperationExecutor : FileTreeClickListener {

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

## 4. Customizing Icons

The `FileTree` library allows you to specify custom icons for files and folders. 

Here's a basic example to customize icons

```kotlin
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

## 5. Asynchronous File System

The `FileTree` library typically uses a built-in asynchronous system powered by Kotlin Coroutines to perform core file functions such as expanding, collapsing and loading files in `FileTree` on the background thread. This ensures that the UI thread remains responsive while the functions are executed.

## 6. Example (Android view-based)

Here's a basic example of using the `FileTree` library with Android views:

#### Define RecyclerView in Layout (XML)

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

#### Setup FileTree in Activity or Fragment (Kotlin)

```kotlin 
import androidx.recyclerview.widget.RecyclerView 
import androidx.recyclerview.widget.LinearLayoutManager 
import com.zyron.filetree.adapter.FileTreeAdapter 
import com.zyron.filetree.FileTree
import java.io.File

class MainActivity : AppCompatActivity() {

        private fun initializeFileTree(fileTree: FileTree) {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val fileTree = FileTree(this, "storage/emulated/0")
        val fileTreeIconProvider = FileIconProvider()
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
}
```

## 7. Frequently Asked Questions (FAQ)

**Q:** How do I get the selected file or folder in the file tree?

**A:** Use the event listener for node selection to retrieve the selected node. The selected node will likely represent the FileTreeNode object that you can use to access the underlying File object.

**Q:** How do I refresh the file tree after making changes to the file system?

**A:** Use the `loadFileTree()` method to reload the tree structure. If you only need to update a portion of the tree, you can refresh specific nodes or sections.

**Q:** Can I customize the appearance of the file tree?

**A:** Yes, you can customize the appearance by using custom renderers (Swing, SWT) or composables (Jetpack Compose) to control the display of nodes.

**Q:** Can I use the `FileTree` library for other purposes besides IDEs and Code Editors?

**A:** Yes, the library can be adapted for other applications that require file tree management, such as file explorers or project management tools.

## License
```
Copyright 2024 Zyron Official.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
