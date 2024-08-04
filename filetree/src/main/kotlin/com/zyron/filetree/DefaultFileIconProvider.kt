package com.zyron.filetree

import com.zyron.filetree.provider.FileTreeIconProvider
import java.io.File

class DefaultFileIconProvider : FileTreeIconProvider {

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
            "gradlew.bat" -> R.drawable.ic_file
            "gradlew" -> R.drawable.ic_file
            "settings.gradle" -> R.drawable.ic_file
            "build.gradle" -> R.drawable.ic_file
            "gradle.properties" -> R.drawable.ic_file
            else -> getIconForExtension(file.extension)
        }
    }

    override fun getIconForExtension(extension: String): Int {
        return when (extension) {
            "xml" -> R.drawable.ic_file
            "java" -> R.drawable.ic_file
            "kt" -> R.drawable.ic_file
            else -> getDefaultFileIcon()
        }
    }
}