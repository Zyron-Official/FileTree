package com.zyron.filetree.ui

import android.view.View
import android.widget.ViewFlipper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

import com.zyron.filetree.R

class FileTreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val chevronIconViewHolder: ShapeableImageView = itemView.findViewById(R.id.chevronImageView)
    val fileIconViewHolder: ShapeableImageView = itemView.findViewById(R.id.fileIconImageView)
    val fileNameViewHolder: MaterialTextView = itemView.findViewById(R.id.fileNameTextView)
    val chevronLoadingSwitcher: ViewFlipper = itemView.findViewById(R.id.chevronLoadingSwitcher)
    
}
