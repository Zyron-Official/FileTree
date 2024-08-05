package com.zyron.filetree.callback

import androidx.recyclerview.widget.*
import com.zyron.filetree.Node

class FileTreeNodeDiffCallback(private val oldList: List<Node>, private val newList: List<Node>) : DiffUtil.Callback() {

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