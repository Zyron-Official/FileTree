package com.zyron.filetree.model

import java.io.File

data class FileTreeNode(val file: File, var isExpanded: Boolean = false, var isCollapsed: Boolean = false, var parent: FileTreeNode? = null, var isLoading: Boolean = false)