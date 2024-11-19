## FileTree: A Powerful File Explorer Library for IDEs and Code Editors.

This documentation guides you on how to integrate and use the `FileTree` library within your IDEs and Code Editors to provide a user-friendly and efficient file browsing experience.

> [!WARNING]
> 
> This Project is deprecated and no longer maintained in the favor of [Orbit-VFMS](https://github.com/Zyron-Official/Orbit-VFMS)

### Table of Contents:


  **1. Introduction**

  **2. Integration**

   &#9702; 2.1. Dependencies

   &#9702; 2.2. Initialization and Loading

   &#9702; 2.3. UI Integration

   &#9702; 2.4. Event Handling

  **3. File Operations**

  **4. Customizing Icons**

  **5. AttributeSet**

  **6. Asynchronous File System**

  **7. Example (Android View-Based)**

  **8. License.**

## 1. Introduction

The `FileTree` library offers a comprehensive and modular solution for managing and displaying file trees within IDEs and code editors. It is designed to provide the following key features:

- **Hierarchical File Tree Representation:** Displays files and folders in an organized hierarchical tree structure.

- **Hover-Navigated Directory:** Highlights directories as users navigate through them with clicks. This feature enhances user experience by visually indicating the current directory, offering a clear and interactive method for exploring the directory tree.

- **Lazy Loading:** Improves loading time by loading child nodes only when they are expanded.

- **Asynchronous File System:** Executes file operations like expanding, collapsing, and loading files in the `FileTree` on a background thread, preventing the Main UI Thread from being blocked.

- **Customizable Icons:** Enables the use of custom icons for files and folders.

- **Concurrent FileMap:** Maintains a record of recently accessed files and directories, facilitating quick access to previously expanded files and directories.

- **RecyclerItemView:** Utilizes the built-in RecycledViewPool of RecyclerView to reuse old views, reducing memory usage by avoiding the creation of new views.

### Overview

<div >
<img src="/fastlane/metadata/android/images/Screenshot_20240801-154141_FileTree.jpg" width="48%" />
<img src="/fastlane/metadata/android/images/Screenshot_20240801-154809_FileTree.jpg" width="48%" />
</div>
<div >
<img src="/fastlane/metadata/android/images/Screenshot_20240801-154111_FileTree.jpg" width="48%" />
<img src="/fastlane/metadata/android/images/Screenshot_20240801-154830_FileTree.jpg" width="48%" />
</div>

## 2. Integration
### 2.1. Dependencies

First, add the `FileTree` library as a dependency to your project using a build system like Maven or Gradle:

#### Note: 
(This Dependency is in pending in Maven for Review) you can't use this dependency at the moment, if you want to integrate FileTree you should add FileTree Locally within your project as a library module.

```groovy
implementation 'com.zyron.openapi.filetree:filetree:1.0.0'
```
The specific instructions for adding a dependency will depend on your IDE and build system.

### 2.2. Initialization and Loading

**1. Initialize:** Initialize `FileTree`, providing the root directory:

```kotlin
fileTreeView.initializeFileTree(this, "/storage/emulated/0")
```
### 2.3. UI Integration

This step involves integrating the `FileTree`  Library within your  project, here's a basic example using Android Views:

- **Android View-Based (Android/Kotlin/Java):** Define `FileTreeView` in your Layout (XML) to display your `FileTree` data.

    Docs [Getting Sarted](docs/integration/Android.md)

### 2.4. Event Handling

The `FileTree` library provides events that you can use to react to user actions within the file tree:

Handle these events in your IDE's logic to perform actions like opening files, displaying properties, or refreshing UI elements.

## 3. File Operations

The `FileTree` library provides Event Listeners for performing common file system operations by using FileTreeEventListener Interface.

```kotlin
import com.zyron.filetree.events.FileTreeEventListener 
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

## 4. Customizing Icons

The `FileTree` library allows you to specify custom icons for files and folders. 

Here's a basic example to customize icons

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
            "app" -> R.drawable.ic_folder_app
            "src" -> R.drawable.ic_folder_src
            "kotlin" -> R.drawable.ic_folder_kotlin
            "java" -> R.drawable.ic_folder_java
            "res" -> R.drawable.ic_folder_res
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

## 5. AttributeSet 

| Attribute Name            | Description                                                                                     | Default Value | XML Attribute                   | Type    |
|---------------------------|-------------------------------------------------------------------------------------------------|---------------|---------------------------------|---------|
| **recyclerItemViewCount** | Defines the maximum number of items the RecyclerView can hold in its recycled view pool.        | 200           | `app:recyclerItemViewCount`     | Integer |
| **recyclerItemViewEnabled** | Enables or disables the use of a recycled view pool for the RecyclerView.                      | true          | `app:recyclerItemViewEnabled`   | Boolean |
| **itemViewCacheSize**     | Sets the maximum size for the view cache in the RecyclerView.                                   | 100           | `app:itemViewCacheSize`         | Integer |
| **itemViewCachingEnabled** | Enables or disables caching of item views in the RecyclerView.                                 | true          | `app:itemViewCachingEnabled`    | Boolean |
| **fileMapMaxSize**        | Specifies the maximum number of entries in the file map, which caches recently accessed files and directories. | 150           | `app:fileMapMaxSize`            | Integer |
| **fileMapEnabled**        | Enables or disables the file map functionality.                                                 | true          | `app:fileMapEnabled`            | Boolean |
| **fileTreeAnimation**     | Sets the animation style for expanding and collapsing the file tree.                            | 4             | `app:fileTreeAnimation`         | Integer |
| **fileTreeAnimationEnabled** | Enables or disables animations for the file tree.                                             | true          | `app:fileTreeAnimationEnabled`  | Boolean |

### Usage Example

To configure these attributes in your XML layout file, use the following syntax:

```xml
<com.zyron.filetree.widget.FileTreeView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:recyclerItemViewCount="250"
    app:recyclerItemViewEnabled="true"
    app:itemViewCacheSize="120"
    app:itemViewCachingEnabled="true"
    app:fileMapMaxSize="180"
    app:fileMapEnabled="true"
    app:fileTreeAnimation="FallDown"
    app:fileTreeAnimationEnabled="false"/>
```
Adjust these values according to your application's needs to optimize performance and user experience.

## 6. Asynchronous File System

The `FileTree` library typically uses a built-in asynchronous system powered by Kotlin Coroutines to perform core file functions such as expanding, collapsing and loading files in `FileTree` on the background thread. This ensures that the UI thread remains responsive while the functions are executed.

## 7. Example (Android view-based)

Here's a basic example of using the `FileTree` library with Android views:

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
import com.zyron.filetree.widget.FileTreeView 
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

## 8. License
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