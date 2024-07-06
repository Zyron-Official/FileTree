## FileTree Library: A Powerful File Explorer for IDEs and Code Editors

This documentation guides you on how to integrate and use the ```FileTree``` library within your IDEs and Code Editors to provide a user-friendly and efficient file browsing experience.


### Table of Contents:


### 1. Introduction

### 2. Integration

    &#9702; 2.1. Dependencies

    &#9702; 2.2. Initialization and Loading

    &#9702; 2.3. UI Integration

    &#9702; 2.4. Event Handling

### 3. File Operations

    &#9702; 3.1. Copy, Cut, Paste

    &#9702; 3.2. Delete

    &#9702; 3.3. Rename

    &#9702; 3.4. Create

### 4. Customizing Icons

### 5. Thread System

### 6. Example (Swing)

### 7. Frequently Asked Questions (FAQ)

## 1. Introduction

The ```FileTree``` library provides a robust and modular way to manage and display file trees within your IDEs and code editors. It offers the following key features:

- **Hierarchical File Tree Representation:** Presents files and folders in a hierarchical tree structure.

- **Lazy Loading:** Optimizes loading time by only loading child nodes when expanded.

- **Asynchronous File Operations:** Performs file operations (copy, paste, delete, etc.) in the background to avoid blocking the UI.

- **Customizable Icons:** Allows you to set custom icons for files and folders.

- **Flexibility:** Integrates seamlessly with various IDE UI frameworks (Swing, SWT, Jetpack Compose).

## 2. Integration
### 2.1. Dependencies

First, add the ```FileTree``` library as a dependency to your project using a build system like Maven or Gradle. The specific instructions for adding a dependency will depend on your IDE and build system.

### 2.2. Initialization and Loading

**1. Create an Instance:** Instantiate a FileTree object, providing the root directory:

```java
FileTree fileTree = new FileTree(new File("/")); // Replace with your root directory
```

**2. Load the Tree:** Call the loadTree() method to load the initial file tree structure. This will load the root directory and its immediate children:

```java
fileTree.loadTree();
```
### 2.3. UI Integration

This step involves integrating the ```FileTree``` library with your IDE's UI framework. The specific implementation will vary depending on your chosen UI framework. Below are examples for common frameworks:

- **Swing (Java):** Use a JTree component and create a custom TreeModel that uses the FileTreeNode data from your ```FileTree``` object. 
Alternatively, use the FileTreeAdapter (if provided) to populate the JTree.

- **SWT (Java):** Similar to Swing, use a TreeViewer and a custom TreeContentProvider to map your FileTreeNode data.

- **Jetpack Compose (Android/Kotlin):** Define a composable that renders the file tree using your ```FileTree``` data. 

You might need to create a custom composable to display the tree nodes (similar to a custom TreeCellRenderer in Swing).

### Example (Swing):

```java
import com.example.filetree.FileTree;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IDEExample {

    private JTree fileTree;
    private FileTree fileTreeModel;

    public void init() {
        // 1. Create a FileTree instance
        fileTreeModel = new FileTree(new File("/")); // Your root directory

        // 2. Load the file tree
        fileTreeModel.loadTree();

        // 3. Create a JTree and set a custom TreeModel (optional)
        fileTree = new JTree();
        fileTree.setModel(new CustomTreeModel(fileTreeModel.getNodes()));

        // 4. Set up listeners for node selection, expansion/collapse, etc.
        //    and handle events accordingly in your IDE's logic. 

        // 5. Handle file operations
        fileTreeModel.copyFile(sourceFile, destinationFile); // Example
        fileTreeModel.deleteFile(fileToDelete); // Example

        // 6. Add your icons for files and folders to the JTree
        //    using the FileTreeAdapter if it's provided or by using a
        //    custom renderer if you're using the FileTree directly.
    }

    // Custom TreeModel for JTree (optional)
    public class CustomTreeModel extends DefaultTreeModel {
        // Implement a custom TreeModel based on your FileTree data
    }
}
```

### 2.4. Event Handling

Your ```FileTree``` library should provide events or callbacks that you can use to react to user actions within the file tree:

**Node Selection:** Register a listener to get notified when the user selects a file or folder.

**Expansion/Collapse:** Get notified when a folder is expanded or collapsed.

**File Operations Completion:** Get notified when file operations (copy, paste, delete, etc.) are completed.

Handle these events in your IDE's logic to perform actions like opening files, displaying properties, or refreshing UI elements.

## 3. File Operations

The ```FileTree``` library provides methods for performing common file system operations.

### 3.1. Copy, Cut, Paste

**Copy:** Use the copyFile(source: File, destination: File) method to copy a file or folder.

**Cut:** Use the moveFile(source: File, destination: File) method to move a file or folder.

**Paste:** You'll need to implement paste logic within your IDE, using the copied or cut file from the clipboard and the copyFile or moveFile methods to perform the actual file operation.

### 3.2. Delete

Use the deleteFile(file: File) method to delete a file or folder. If you delete a folder, it will recursively delete all its contents.

### 3.3. Rename

Use the renameFile(file: File, newName: String) method to rename a file or folder.

### 3.4. Create

**Create File:** Use the createFile(parent: File, fileName: String) method to create a new file.

**Create Folder:** Use the createFolder(parent: File, folderName: String) method to create a new folder.

## 4. Customizing Icons

The ```FileTree``` library allows you to specify custom icons for files and folders. You can either provide a default set of icons or allow users to customize them within your IDE's settings.

Resource IDs (Android): If you're using Android, you can provide resource IDs (e.g., R.drawable.my_folder_icon) for icons.

Paths (Generic): For cross-platform compatibility, you can allow users to provide file paths for icon images.

## 5. Thread System

The ```FileTree``` library typically uses a thread system to perform file operations in the background. This ensures that the UI thread remains responsive while the operations are executed. The library may provide methods for controlling the thread system:

**Initiating Loading:** You may need to start a background thread to load the initial file tree structure.

**Pausing/Resuming:** In some cases, you might want to pause or resume loading operations.

## 6. Example (Swing)

Here's a basic example of using the FileTree library with Swing:

```java
import com.example.filetree.FileTree;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class IDEExample {

    private JTree fileTree;
    private FileTree fileTreeModel;

    public void init() {
        // 1. Create a FileTree instance
        fileTreeModel = new FileTree(new File("/")); // Your root directory

        // 2. Load the file tree
        fileTreeModel.loadTree();

        // 3. Create a JTree and set a custom TreeModel (optional)
        fileTree = new JTree();
        fileTree.setModel(new CustomTreeModel(fileTreeModel.getNodes()));

        // 4. Add listeners for selection, expansion, and other events 
        //    and handle them accordingly in your IDE logic. 
        // 5. Handle file operations
        fileTreeModel.copyFile(sourceFile, destinationFile); // Example
        fileTreeModel.deleteFile(fileToDelete); // Example

        // 6. Add your icons for files and folders (using custom renderers or the
        //    FileTreeAdapter if provided)
    }

    // Custom TreeModel for JTree (optional)
    public class CustomTreeModel extends DefaultTreeModel {
        // Implement a custom TreeModel based on your FileTree data
    }
}
```

## 7. Frequently Asked Questions (FAQ)

### Q: How do I get the selected file or folder in the file tree?

### A: Use the event listener for node selection to retrieve the selected node. The selected node will likely represent the FileTreeNode object that you can use to access the underlying File object.

### Q: How do I refresh the file tree after making changes to the file system?

### A: Use the loadTree() method to reload the tree structure. If you only need to update a portion of the tree, you can refresh specific nodes or sections.

### Q: Can I customize the appearance of the file tree?

### A: Yes, you can customize the appearance by using custom renderers (Swing, SWT) or composables (Jetpack Compose) to control the display of nodes.

### Q: Can I use the FileTree library for other purposes besides IDEs and Code Editors?

### A: Yes, the library can be adapted for other applications that require file tree management, such as file explorers or project management tools.

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
