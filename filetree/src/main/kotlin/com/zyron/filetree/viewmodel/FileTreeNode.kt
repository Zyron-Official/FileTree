package com.zyron.filetree.viewmodel

import java.io.File

data class FileTreeNode(
    val file: File,
    var parent: FileTreeNode? = null,
    var level: Int = 0
) {
    var isExpanded: Boolean = false
    var childrenStartIndex: Int = 0
    var childrenEndIndex: Int = 0
    var childrenLoaded: Boolean = false
}