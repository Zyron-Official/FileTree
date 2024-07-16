package com.zyron.filetree.adapter

import android.content.Context
import android.util.*
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.zyron.filetree.R
import com.zyron.filetree.FileTree
import com.zyron.filetree.FileTreeAdapterUpdateListener
import com.zyron.filetree.viewholder.FileTreeViewHolder
import com.zyron.filetree.viewmodel.*
import com.zyron.filetree.provider.FileTreeIconProvider
import kotlinx.coroutines.*
import java.io.File
import java.nio.file.Files

interface FileTreeClickListener {
    fun onFileClick(file: File)
    fun onFolderClick(folder: File)
    fun onFileLongClick(file: File): Boolean
    fun onFolderLongClick(folder: File): Boolean
}

class FileTreeAdapter(
    private val context: Context,
    private val fileTree: FileTree,
    private val fileTreeIconProvider: FileTreeIconProvider,
    private val listener: FileTreeClickListener? = null
) : RecyclerView.Adapter<FileTreeViewHolder>(), FileTreeAdapterUpdateListener {

    private var nodes: MutableList<FileTreeNode> = fileTree.getNodes().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileTreeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return FileTreeViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileTreeViewHolder, position: Int) {
        val node = nodes[position]

        // Calculate indentation based on node level
        val indentationDp = 16 * node.level
        val indentationPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indentationDp.toFloat(), context.resources.displayMetrics).toInt()

        // Check the layout direction from the context
        val isRtl = context.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

        // Set left or right margin based on layout direction
        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (isRtl) {
            layoutParams.rightMargin = indentationPx
            layoutParams.leftMargin = 0
        } else {
            layoutParams.leftMargin = indentationPx
            layoutParams.rightMargin = 0
        }
        holder.itemView.layoutParams = layoutParams

        // Set padding for ViewHolder itself and its items
        holder.itemView.setPadding(4, 4, 4, 4)
        holder.chevronIconView.setPadding(0, 0, 0, 0)
        holder.fileIconView.setPadding(0, 0, 0, 0)
        holder.fileNameView.setPadding(6, 7, 7, 6)

        if (Files.isDirectory(node.file.toPath())) {
            holder.chevronIconView.visibility = View.VISIBLE
            holder.fileIconView.setImageDrawable(ContextCompat.getDrawable(context, fileTreeIconProvider.getFolderIcon()))
            holder.fileNameView.text = node.file.name

            val chevronIcon = if (node.isExpanded) {
                fileTreeIconProvider.getChevronCollapseIcon()
            } else {
                fileTreeIconProvider.getChevronExpandIcon()
            }
            holder.chevronIconView.setImageDrawable(ContextCompat.getDrawable(context, chevronIcon))

            holder.itemView.setOnClickListener {
                if (node.isExpanded) {
                    fileTree.collapseNode(node)
                } else {
                    fileTree.expandNode(node)
                }
                notifyItemChanged(holder.bindingAdapterPosition)
            }
        } else if (node.file.isFile) {
            holder.chevronIconView.visibility = View.GONE
            holder.fileIconView.setImageDrawable(ContextCompat.getDrawable(context, fileTreeIconProvider.getIconForFile(node.file)))
            holder.fileNameView.text = node.file.name

            holder.itemView.setOnClickListener {
                listener?.onFileClick(node.file)
            }

            holder.itemView.setOnLongClickListener {
                listener?.onFileLongClick(node.file) ?: false
            }
        }
    }

    override fun onFileTreeUpdated(startPosition: Int, itemCount: Int) {
        if (itemCount > 0) {
            notifyItemRangeInserted(startPosition, itemCount)
        } else {
            notifyItemRangeRemoved(startPosition, -itemCount)
        }
    }

    fun updateNodes(newNodes: List<FileTreeNode>) {
        val diffResult = DiffUtil.calculateDiff(FileTreeNodeDiffCallback(nodes, newNodes))
        nodes.clear()
        nodes.addAll(newNodes)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return nodes.size
    }
}
    
/*    private fun expandNodeWithAnimation(view: View, node: FileTreeNode, position: Int) {
        node.isLoading = true
        notifyItemChanged(position)

        val initialHeight = view.height
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = view.measuredHeight

        val layoutParams = view.layoutParams
        layoutParams.height = initialHeight
        view.layoutParams = layoutParams
        view.visibility = View.VISIBLE

        val valueAnimator = ValueAnimator.ofInt(initialHeight, targetHeight)
        valueAnimator.addUpdateListener { animation ->
            val animValue = animation.animatedValue as Int
            val lp = view.layoutParams
            lp.height = animValue
            view.layoutParams = lp
        }

        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val lp = view.layoutParams
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
                view.layoutParams = lp
                
                fileTree.expandNode(node)

                node.isLoading = false
                notifyItemChanged(position)
            }
        })

        valueAnimator.duration = 3
        valueAnimator.start()
    }


    private fun collapseNodeWithAnimation(view: View, node: FileTreeNode, position: Int) {
    node.isLoading = true
        notifyItemChanged(position)
        
        val initialHeight = view.height
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = view.measuredHeight
        
        val layoutParams = view.layoutParams
        layoutParams.height = initialHeight
        view.layoutParams = layoutParams
        view.visibility = View.VISIBLE

        val valueAnimator = ValueAnimator.ofInt(initialHeight, targetHeight)
        valueAnimator.addUpdateListener { animation ->
            val animValue = animation.animatedValue as Int
            val lp = view.layoutParams
            lp.height = animValue
            view.layoutParams = lp
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                
                val lp = view.layoutParams
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
                view.layoutParams = lp
                fileTree.collapseNode(node)
                node.isLoading = false
                notifyItemChanged(position)
            }
        })
        valueAnimator.duration = 3
        valueAnimator.start()
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