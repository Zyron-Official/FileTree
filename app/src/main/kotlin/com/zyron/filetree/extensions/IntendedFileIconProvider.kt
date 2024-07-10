package com.zyron.filetree.extensions

import com.zyron.filetree.provider.FileIconProvider
import com.zyron.filetree.R
import java.io.File

class IntendedFileIconProvider : FileIconProvider {
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
            else -> getIconForExtension(file.extension)
        }
    }

    override fun getIconForExtension(extension: String): Int {
        return when (extension) {
            "xml" -> R.drawable.ic_file
            "kt" -> R.drawable.ic_file
            else -> getDefaultFileIcon()
        }
    }
}