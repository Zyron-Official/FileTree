package com.zyron.filetree.ui

import android.view.View
import android.widget.ViewFlipper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.progressindicator.CircularProgressIndicator

import com.zyron.filetree.R

class FileTreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val chevronLoadingSwitcher: ViewFlipper = itemView.findViewById(R.id.chevronLoadingSwitcher)
    val chevronImageView: ShapeableImageView = itemView.findViewById(R.id.chevronImageView)
    val loadingProgressView: CircularProgressIndicator = itemView.findViewById(R.id.laodingProgressIndicator)
    val fileIconImageView: ShapeableImageView = itemView.findViewById(R.id.fileIconImageView)
    val fileNameTextView: MaterialTextView = itemView.findViewById(R.id.fileNameTextView)
}