package com.zyron.filetree.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zyron.filetree.R
import com.zyron.filetree.FileTree
import com.zyron.filetree.model.FileTreeNode
import com.zyron.filetree.model.FileTreeNodeDiffCallback
import com.zyron.filetree.ui.FileTreeViewHolder
import java.io.File
import java.nio.file.Files

interface FileTreeClickListener {
    fun onFileClick(file: File)
    fun onFolderClick(folder: File)
}

class FileTreeAdapter(
    private val context: Context,
    private val fileTree: FileTree,
    private val iconFolder: Drawable,
    private val iconFile: Drawable,
    private val iconChevronExpanded: Drawable,
    private val iconChevronCollapsed: Drawable,
    private val listener: FileTreeClickListener? = null
) : RecyclerView.Adapter<FileTreeViewHolder>() {

    private val mainHandler = Handler(Looper.getMainLooper())
    private var clipboard: File? = null
    private var nodes: MutableList<FileTreeNode> = mutableListOf()
    
    // Inside your FileTreeAdapter.kt file
private fun getFileIconDrawable(file: File): Drawable? {
    // Implement logic to get the appropriate file icon drawable based on file type
    // Example logic:
    // if (file.isDirectory()) return directoryIconDrawable
    // else if (file.isFile()) return fileIconDrawable
    // else return defaultIconDrawable
    return null // Replace with your actual implementation
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileTreeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return FileTreeViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileTreeViewHolder, position: Int) {
    val node = nodes[position]

    // Set file name
    holder.fileNameViewHolder.text = node.file.name

    // Handle icons
    if (Files.isDirectory(node.file.toPath())) {
        holder.chevronIconViewHolder.visibility = View.VISIBLE
        val chevronIcon = if (node.isExpanded) {
            iconChevronCollapsed
        } else {
            iconChevronExpanded
        }
        holder.chevronIconViewHolder.setImageDrawable(chevronIcon)

        holder.fileIconViewHolder.visibility = View.GONE // Hide file icon for directories
    } else {
        holder.chevronIconViewHolder.visibility = View.INVISIBLE // Hide chevron for files if not needed
        holder.fileIconViewHolder.visibility = View.VISIBLE
        // Set file icon drawable based on file type
        val fileIcon = getFileIconDrawable(node.file)
        holder.fileIconViewHolder.setImageDrawable(fileIcon ?: defaultFileIconDrawable)
    }

    // Handle click listener
    holder.itemView.setOnClickListener {
        if (Files.isDirectory(node.file.toPath())) {
            if (node.isExpanded) {
                fileTree.collapseNode(node)
            } else {
                fileTree.expandNode(node)
            }
            // Update the chevron icon after expanding/collapsing the node
            val updatedChevronIcon = if (node.isExpanded) {
                iconChevronCollapsed
            } else {
                iconChevronExpanded
            }
            holder.chevronIconViewHolder.setImageDrawable(updatedChevronIcon)
            // Notify item changed to update the view
            notifyItemChanged(holder.adapterPosition)
        } else {
            listener?.onFileClick(node.file)
        }
    }
}
    
     fun setLoading(node: FileTreeNode, loading: Boolean) {
        val position = nodes.indexOf(node)
        if (position != RecyclerView.NO_POSITION) {
            nodes[position].isLoading = loading
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return nodes.size
    }

    fun updateNodes(newNodes: List<FileTreeNode>) {
        val diffResult = DiffUtil.calculateDiff(
            FileTreeNodeDiffCallback(nodes, newNodes)
        )
        nodes.clear()
        nodes.addAll(newNodes)
        diffResult.dispatchUpdatesTo(this)
    }
}

//Expand and Collapse Animation
/*    private fun expand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight

        v.layoutParams.height = 0
        v.visibility = View.VISIBLE
        val a: Animation = object : Animation() {
            override fun willChangeBounds(): Boolean {
                return true
            }

            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }
        }

        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong() / 2
        v.startAnimation(a)
    }

    private fun collapse(v: View) {
        val initialHeight = v.measuredHeight

        val a: Animation = object : Animation() {
            override fun willChangeBounds(): Boolean {
                return true
            }

            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }
        }

        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong() / 2
        v.startAnimation(a)
    } 
}*/

        // Context menu for file and folder actions
     /*   holder.itemView.setOnLongClickListener {
            val menu = PopupMenu(context, it)
            if (Files.isDirectory(node.file.toPath())) {
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
                            if (Files.isDirectory(node.file.toPath())) {
                                fileTree.copyFile(clipboard!!, File(node.file.toPath().toString(), clipboard!!.name))
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
                    R.id.action_rename -> {
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
                    }
                    else -> false
                }
            }
            menu.show()
            true
        }
    }
}*/