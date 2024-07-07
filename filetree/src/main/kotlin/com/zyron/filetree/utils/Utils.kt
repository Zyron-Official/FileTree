package com.zyron.filetree.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

class EditTextDialog(context: Context, title: String, initialText: String) {
/*
    private val dialog: AlertDialog
    private val editText: EditText

    init {
        val builder = AlertDialog.Builder(context)
        val view = (context as androidx.appcompat.app.AppCompatActivity).layoutInflater.inflate(android.R.layout.simple_list_item_1, null)
        editText = view.findViewById(android.R.id.text1)
        editText.setText(initialText)
        builder.setTitle(title)
            .setView(view)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onPositiveButtonClickListener?.invoke(dialog as AlertDialog, editText.text.toString())
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        dialog = builder.create()
    }

    fun setPositiveButton(text: String, listener: (dialog: AlertDialog, text: String) -> Unit) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(text)
        onPositiveButtonClickListener = listener
    }

    fun show() {
        dialog.show()
    }

    private var onPositiveButtonClickListener: ((dialog: AlertDialog, text: String) -> Unit)? = null
*/}