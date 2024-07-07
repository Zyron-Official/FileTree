package com.zyron.filetree.operations

import android.util.Log
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class FileOperationExecutor {

    // Define your thread pool for performing file operations
    private val executor: ThreadPoolExecutor = ThreadPoolExecutor(
        2,
        4,
        60L,
        TimeUnit.SECONDS,
        LinkedBlockingQueue()
    )

    fun copyFile(source: File, destination: File) {
        // Start the file copy operation on a background thread
        executor.execute {
            try {
                // Copy the file using Java File API
                source.copyTo(destination, overwrite = true)
                Log.d("FileOperationExecutor", "File copied successfully: ${source.absolutePath} to ${destination.absolutePath}")
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("FileOperationExecutor", "Error copying file: ${e.message}")
            }
        }
    }

    fun moveFile(source: File, destination: File) {
        executor.execute {
            try {
                // Move the file using Java File API
                source.renameTo(destination)
                Log.d("FileOperationExecutor", "File moved successfully: ${source.absolutePath} to ${destination.absolutePath}")
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("FileOperationExecutor", "Error moving file: ${e.message}")
            }
        }
    }

    fun deleteFile(file: File) {
        executor.execute {
            try {
                // Delete the file using Java File API
                if (file.isDirectory) {
                    file.deleteRecursively()
                } else {
                    file.delete()
                }
                Log.d("FileOperationExecutor", "File deleted successfully: ${file.absolutePath}")
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("FileOperationExecutor", "Error deleting file: ${e.message}")
            }
        }
    }

    fun renameFile(file: File, newName: String) {
        executor.execute {
            try {
                // Rename the file using Java File API
                file.renameTo(File(file.parentFile!!, newName))
                Log.d("FileOperationExecutor", "File renamed successfully: ${file.absolutePath} to ${newName}")
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("FileOperationExecutor", "Error renaming file: ${e.message}")
            }
        }
    }

    fun createFile(parent: File, fileName: String) {
        executor.execute {
            try {
                // Create the file using Java File API
                File(parent, fileName).createNewFile()
                Log.d("FileOperationExecutor", "File created successfully: ${File(parent, fileName).absolutePath}")
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("FileOperationExecutor", "Error creating file: ${e.message}")
            }
        }
    }

    fun createFolder(parent: File, folderName: String) {
        executor.execute {
            try {
                // Create the folder using Java File API
                File(parent, folderName).mkdirs()
                Log.d("FileOperationExecutor", "Folder created successfully: ${File(parent, folderName).absolutePath}")
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("FileOperationExecutor", "Error creating folder: ${e.message}")
            }
        }
    }
}
