package com.zyron.filetree.model

import androidx.recyclerview.widget.DiffUtil

import com.zyron.filetree.R 
import com.zyron.filetree.FileTree 
import com.zyron.filetree.model.FileTreeNode 

class FileTreeNodeDiffCallback(private val oldList: List<FileTreeNode>, private val newList: List<FileTreeNode>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].file == newList[newItemPosition].file
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}