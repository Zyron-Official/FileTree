package com.zyron.filetree.provider

import androidx.annotation.DrawableRes
import java.io.File

interface FileIconProvider {
    @DrawableRes
    fun getFolderIcon(): Int

    @DrawableRes
    fun getDefaultFileIcon(): Int

    @DrawableRes
    fun getIconForFile(file: File): Int

    @DrawableRes
    fun getIconForExtension(extension: String): Int
}