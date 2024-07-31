## FileTree: A Simplified File Explorer Library for IDEs and Code Editors.

This documentation guides you on how to integrate and use the ```FileTree``` library within your IDEs and Code Editors to provide a user-friendly and efficient file browsing experience.


### Table of Contents:


  **1. Introduction**

  **2. Integration**

   &#9702; 2.1. Dependencies

   &#9702; 2.2. Initialization and Loading

   &#9702; 2.3. UI Integration

   &#9702; 2.4. Event Handling

  **3. File Operations**

   &#9702; 3.1. Copy, Cut, Paste

   &#9702; 3.2. Delete

   &#9702; 3.3. Rename

   &#9702; 3.4. Create

  **4. Customizing Icons**

  **5. Thread System**

  **6. Example (Android View-Based)**

  **7. Frequently Asked Questions (FAQ)**

## 1. Introduction

The ```FileTree``` library provides a robust and modular way to manage and display file trees within your IDEs and code editors. It offers the following key features:

- **Hierarchical File Tree Representation:** Presents files and folders in a hierarchical tree structure.

- **Lazy Loading:** Optimizes loading time by only loading child nodes when expanded.

- **Asynchronous File Operations:** Performs file operations (copy, paste, delete, etc.) in the background to avoid blocking the UI.

- **Customizable Icons:** Allows you to set custom icons for files and folders.

- **Flexibility:** Integrates seamlessly with various IDE UI frameworks (Swing, SWT, Jetpack Compose).

## 2. Integration
### 2.1. Dependencies

First, add the ```FileTree``` library as a dependency to your project using a build system like Maven or Gradle:
```groovy
implementation 'com.zyron.openapi.filetree:filetree:1.0.0'
```
The specific instructions for adding a dependency will depend on your IDE and build system.

### 2.2. Initialization and Loading

**1. Create an Instance:** Instantiate a FileTree object, providing the root directory:

```kotlin
val fileTree = FileTree(this, "/storage/emulated/0")
```

**2. Load the Tree:** Call the loadTree() method to load the initial file tree structure. This will load the root directory and its immediate children:

```kotlin
fileTree.loadFileTree()
```
### 2.3. UI Integration

This step involves integrating the ```FileTree``` library with your IDE's UI framework. The specific implementation will vary depending on your chosen UI framework. Below are examples for common frameworks:

- **Android View-Based (Android/Kotlin/Java):** Use `RecyclerView` to display your `FileTree` data.

    **Example** [Getting Sarted](docs/integration/Android.md)

- **Jetpack Compose (Android/Kotlin):** Define a composable that renders the file tree using your ```FileTree``` data. 

    You might need to create a custom composable to display the tree nodes (similar to a custom TreeCellRenderer in Swing).

    **Example** [Getting Sarted](docs/integration/Android-Jetpack-Compose.md)

- **Swing (Java):** Use a JTree component and create a custom TreeModel that uses the FileTreeNode data from your ```FileTree``` object. 

    Alternatively, use the FileTreeAdapter (if provided) to populate the JTree.

    **Example** [Getting Sarted](docs/integration/Java-Swing.md)

- **SWT (Java):** Similar to Swing, use a TreeViewer and a custom TreeContentProvider to map your FileTreeNode data.

    **Example** [Getting Sarted](docs/integration/Java-SWT.md)

### 2.4. Event Handling

Your ```FileTree``` library should provide events or callbacks that you can use to react to user actions within the file tree:

**Node Selection:** Register a listener to get notified when the user selects a file or folder.

**Expansion/Collapse:** Get notified when a folder is expanded or collapsed.

**File Operations Completion:** Get notified when file operations (copy, paste, delete, etc.) are completed.

Handle these events in your IDE's logic to perform actions like opening files, displaying properties, or refreshing UI elements.

## 3. File Operations

The ```FileTree``` library provides methods for performing common file system operations.

### 3.1. Copy, Cut, Paste

**Copy:** Use the ```copyFile(source: File, destination: File)``` method to copy a file or folder.

**Cut:** Use the ```moveFile(source: File, destination: File)``` method to move a file or folder.

**Paste:** You'll need to implement paste logic within your IDE, using the copied or cut file from the clipboard and the copyFile or moveFile methods to perform the actual file operation.

### 3.2. Delete

Use the ```deleteFile(file: File)``` method to delete a file or folder. If you delete a folder, it will recursively delete all its contents.

### 3.3. Rename

Use the ```renameFile(file: File, newName: String)``` method to rename a file or folder.

### 3.4. Create

**Create File:** Use the ```createFile(parent: File, fileName: String)``` method to create a new file.

**Create Folder:** Use the ```createFolder(parent: File, folderName: String)``` method to create a new folder.

## 4. Customizing Icons

The ```FileTree``` library allows you to specify custom icons for files and folders. You can either provide a default set of icons or allow users to customize them within your IDE's settings.

Resource IDs (Android): If you're using Android, you can provide resource IDs (e.g., R.drawable.iconFolder) for icons.

Paths (Generic): For cross-platform compatibility, you can allow users to provide file paths for icon images.

## 5. Thread System

The ```FileTree``` library typically uses a thread system to perform file operations in the background. This ensures that the UI thread remains responsive while the operations are executed. The library may provide methods for controlling the thread system:

**Initiating Loading:** You may need to start a background thread to load the initial file tree structure.

**Pausing/Resuming:** In some cases, you might want to pause or resume loading operations.

## 6. Example (Swing)

Here's a basic example of using the FileTree library with Android views:

#### Layout

```xml
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

### Integration

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

## 7. Frequently Asked Questions (FAQ)

**Q:** How do I get the selected file or folder in the file tree?

**A:** Use the event listener for node selection to retrieve the selected node. The selected node will likely represent the FileTreeNode object that you can use to access the underlying File object.

**Q:** How do I refresh the file tree after making changes to the file system?

**A:** Use the loadTree() method to reload the tree structure. If you only need to update a portion of the tree, you can refresh specific nodes or sections.

**Q:** Can I customize the appearance of the file tree?

**A:** Yes, you can customize the appearance by using custom renderers (Swing, SWT) or composables (Jetpack Compose) to control the display of nodes.

**Q:** Can I use the FileTree library for other purposes besides IDEs and Code Editors?

**A:** Yes, the library can be adapted for other applications that require file tree management, such as file explorers or project management tools.

### License
```
Copyright 2024 Zyron

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
