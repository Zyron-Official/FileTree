package com.zyron.filetree.interfaces

import java.io.File

interface FileTreeEventListener {
    fun onFileClick(file: File)
    fun onFolderClick(folder: File)
    fun onFileLongClick(file: File): Boolean
    fun onFolderLongClick(folder: File): Boolean
    fun onFileTreeViewUpdated(startPosition: Int, itemCount: Int)
}