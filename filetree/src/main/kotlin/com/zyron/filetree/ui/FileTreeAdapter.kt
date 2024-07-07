package com.zyron.filetree.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zyron.filetree.R // Import your R class
import com.zyron.filetree.FileTree // Import your FileTree class
import com.zyron.filetree.model.FileTreeNode 
import com.zyron.filetree.operations.FileOperationExecutor 
import com.zyron.filetree.utils.EditTextDialog 
import android.widget.PopupMenu
import android.view.Menu
import java.io.File
import java.util.*

interface FileTreeClickListener {
    fun onFileClick(file: File)
    fun onFolderClick(folder: File)
}

class FileTreeAdapter(val context: Context, val fileTree: FileTree, val folderIcon: Drawable, val fileIcon: Drawable, val listener: FileTreeClickListener? = null
) : RecyclerView.Adapter<FileTreeAdapter.FileTreeNodeViewHolder>() {

    private val mainHandler = Handler(Looper.getMainLooper())
    private var clipboard: File? = null 

    override fun getItemCount(): Int {
    return fileTree.getNodes().size 
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileTreeNodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false) 
        return FileTreeNodeViewHolder(view)
    }


    override fun onBindViewHolder(holder: FileTreeNodeViewHolder, position: Int) {
        val node = fileTree.getNodes()[position]
        holder.fileNameTextView.text = node.file.name
        holder.fileIconImageView.setImageDrawable(if (node.file.isDirectory) folderIcon else fileIcon)
        holder.chevronImageView.visibility = if (node.file.isDirectory && node.isExpanded) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            if (node.file.isDirectory) {
                if (node.isExpanded) {
                    fileTree.collapseNode(node)
                } else {
                    fileTree.expandNode(node)
                }
                mainHandler.post {
                    notifyItemChanged(position + 1, fileTree.getNodes().size - (position + 1))
                }
            } else {
                listener?.onFileClick(node.file)
            }
        }


        // Context menu for file and folder actions
        holder.itemView.setOnLongClickListener {
            val menu = PopupMenu(context, it)
            if (node.file.isDirectory) {
                menu.menu.add(Menu.NONE, R.id.action_create_file, Menu.NONE, "Create File")
                menu.menu.add(Menu.NONE, R.id.action_create_folder, Menu.NONE, "Create Folder")
            }
            menu.menu.add(Menu.NONE, R.id.action_copy, Menu.NONE, "Copy")
            menu.menu.add(Menu.NONE, R.id.action_cut, Menu.NONE, "Cut")
            menu.menu.add(Menu.NONE, R.id.action_paste, Menu.NONE, "Paste")
            menu.menu.add(Menu.NONE, R.id.action_delete, Menu.NONE, "Delete")
            menu.menu.add(Menu.NONE, R.id.action_rename, Menu.NONE, "Rename")

            // Handle menu item clicks
            menu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId as Int) {
                    R.id.action_copy -> {
                        clipboard = node.file
                        true
                    }
                    R.id.action_cut -> {
                        clipboard = node.file
                        true
                    }
                    R.id.action_paste -> {
                        if (clipboard != null) {
                            if (node.file.isDirectory) {
                                fileTree.copyFile(clipboard!!, File(node.file, clipboard!!.name))
                            } else {
                                fileTree.copyFile(clipboard!!, node.file.parentFile!!)
                            }
                            clipboard = null // Clear the clipboard
                            // Refresh the adapter to reflect the changes
                            mainHandler.post {
                                notifyDataSetChanged()
                            }
                        }
                        true
                    }
                    R.id.action_delete -> {
                        if (node.file.deleteRecursively()) {
                            // Remove the deleted node from the list
                            fileTree.nodes.remove(node)
                            // Update the UI
                            mainHandler.post {
                                notifyItemRemoved(position)
                            }
                        }
                        true
                    }
       /*             R.id.action_rename -> {
                        val renameDialog = EditTextDialog(context, "Rename", node.file.name)
                        renameDialog.setPositiveButton("Rename") { _, newName ->
                            if (newName.isNotBlank()) {
                                fileTree.renameFile(node.file, newName)
                                // Update the UI
                                mainHandler.post {
                                    notifyItemChanged(position)
                                }
                            }
                        }
                        renameDialog.show()
                        true
                    }
                    R.id.action_create_file -> {
                        val createDialog = EditTextDialog(context, "Create File", "New File.txt")
                        createDialog.setPositiveButton("Create") { _, fileName ->
                            if (fileName.isNotBlank()) {
                                fileTree.createFile(node.file, fileName)
                                // Update the UI
                                mainHandler.post {
                                    notifyDataSetChanged()
                                }
                            }
                        }
                        createDialog.show()
                        true
                    }
                    R.id.action_create_folder -> {
                        val createDialog = EditTextDialog(context, "Create Folder", "New Folder")
                        createDialog.setPositiveButton("Create") { _, folderName ->
                            if (folderName.isNotBlank()) {
                                fileTree.createFolder(node.file, folderName)
                                // Update the UI
                                mainHandler.post {
                                    notifyDataSetChanged()
                                }
                            }
                        }
                        createDialog.show()
                        true
                    }*/
                    else -> false
                }
            }
            menu.show()
            true
        }
    }



    inner class FileTreeNodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileNameTextView: TextView = itemView.findViewById(R.id.fileNameTextView)
        val fileIconImageView: ImageView = itemView.findViewById(R.id.fileIconImageView)
        val chevronImageView: ImageView = itemView.findViewById(R.id.chevronImageView)
    }
}