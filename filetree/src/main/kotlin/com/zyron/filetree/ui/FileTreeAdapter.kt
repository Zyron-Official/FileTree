// In com/zyron/filetree/ui/FileTreeAdapter.kt

package com.zyron.filetree.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zyron.filetree.FileTree
import com.zyron.filetree.FileTreeAdapterUpdateListener
import com.zyron.filetree.R
import com.zyron.filetree.model.FileTreeNode
import com.zyron.filetree.model.FileTreeNodeDiffCallback
import com.zyron.filetree.provider.FileIconProvider
import java.io.File
import java.nio.file.Files
import android.util.TypedValue

interface FileTreeClickListener {
    fun onFileClick(file: File)
    fun onFolderClick(folder: File)
}

class FileTreeAdapter(
    private val context: Context,
    private val fileTree: FileTree,
    private val fileIconProvider: FileIconProvider,
    private val iconChevronExpanded: Drawable,
    private val iconChevronCollapsed: Drawable,
    private val listener: FileTreeClickListener? = null
) : RecyclerView.Adapter<FileTreeViewHolder>(), FileTreeAdapterUpdateListener {

    private var nodes: MutableList<FileTreeNode> = fileTree.getNodes().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileTreeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return FileTreeViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileTreeViewHolder, position: Int) {
    val node = nodes[position]
    
    val indentationDp = 16 * node.level 
    val indentationPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 
        indentationDp.toFloat(), 
        context.resources.displayMetrics
    ).toInt()
    
    val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.leftMargin = indentationPx
    holder.itemView.layoutParams = layoutParams

    holder.fileNameTextView.text = node.file.name

    if (Files.isDirectory(node.file.toPath())) {
        // Directory handling 
        holder.chevronLoadingSwitcher.visibility = View.VISIBLE
        holder.fileIconImageView.setImageDrawable(
            ContextCompat.getDrawable(context, fileIconProvider.getFolderIcon())
        )

        if (node.isLoading) {
            holder.chevronLoadingSwitcher.displayedChild = 1 // Show progress indicator
        } else {
            holder.chevronLoadingSwitcher.displayedChild = 0 // Show chevron icon
            val chevronIcon = if (node.isExpanded) {
                iconChevronCollapsed
            } else {
                iconChevronExpanded
            }
            holder.chevronImageView.setImageDrawable(chevronIcon)
        }

        // Directory click handling:
        holder.itemView.setOnClickListener {
            if (node.isExpanded) {
                fileTree.collapseNode(node)
                notifyItemChanged(holder.bindingAdapterPosition)
            } else {
                node.isLoading = true
                notifyItemChanged(holder.bindingAdapterPosition)

                fileTree.expandNode(node)
                node.isLoading = false

                notifyItemChanged(holder.bindingAdapterPosition)
            }
        }

    } else { 
        // File Handling:
        holder.chevronLoadingSwitcher.visibility = View.GONE 
        holder.fileIconImageView.setImageDrawable(
            ContextCompat.getDrawable(context, fileIconProvider.getIconForFile(node.file))
        )

        // Handle file clicks:
        holder.itemView.setOnClickListener { 
            listener?.onFileClick(node.file)
        }
    } 
}

    override fun onFileTreeUpdated() {
        updateNodes(fileTree.getNodes())
        notifyDataSetChanged()
    }

    fun updateNodes(newNodes: List<FileTreeNode>) {
        val diffResult = DiffUtil.calculateDiff(FileTreeNodeDiffCallback(nodes, newNodes))
        nodes.clear()
        nodes.addAll(newNodes)
        diffResult.dispatchUpdatesTo(this)
    }
    
    override fun getItemCount(): Int {
    return nodes.size // Return the size of the ENTIRE nodes list 
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