package com.zyron.filetree.viewmodel

import androidx.recyclerview.widget.DiffUtil

class FileTreeNodeDiffCallback(
    private val oldList: List<FileTreeNode>,
    private val newList: List<FileTreeNode>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].file.absolutePath == newList[newItemPosition].file.absolutePath
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}