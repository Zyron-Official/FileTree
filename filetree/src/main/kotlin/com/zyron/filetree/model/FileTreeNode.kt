package com.zyron.filetree.model

import java.io.File

data class FileTreeNode(val file: File, var isExpanded: Boolean = false, var parent: FileTreeNode? = null)