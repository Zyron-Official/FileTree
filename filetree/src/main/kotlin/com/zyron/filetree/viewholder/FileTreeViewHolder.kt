package com.zyron.filetree.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ViewFlipper
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.progressindicator.CircularProgressIndicator

import com.zyron.filetree.R

class FileTreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val chevronLoadingSwitcher: ViewFlipper = itemView.findViewById(R.id.chevronLoadingSwitcher)
    val loadingProgressView: CircularProgressIndicator = itemView.findViewById(R.id.laodingProgressIndicator)    
    val chevronIconView: ShapeableImageView = itemView.findViewById(R.id.chevronIconView)
    val fileIconView: ShapeableImageView = itemView.findViewById(R.id.fileIconView)
    val fileNameView: MaterialTextView = itemView.findViewById(R.id.fileNameView)
}