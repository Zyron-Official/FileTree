package com.zyron.filetree.provider

import androidx.annotation.DrawableRes
import java.io.File

interface FileTreeIconProvider {

    @DrawableRes
    fun getChevronExpandIcon(): Int
    
    @DrawableRes
    fun getChevronCollapseIcon(): Int
    
    @DrawableRes
    fun getFolderIcon(): Int

    @DrawableRes
    fun getDefaultFileIcon(): Int

    @DrawableRes
    fun getIconForFile(file: File): Int

    @DrawableRes
    fun getIconForExtension(extension: String): Int
    
}