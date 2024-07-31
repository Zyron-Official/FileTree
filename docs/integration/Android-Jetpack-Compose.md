### Example (Android - Jetpack Compose):

```kotlin 
import androidx.recyclerview.widget.RecyclerView 
import androidx.recyclerview.widget.LinearLayoutManager 
import com.zyron.filetree.adapter.FileTreeAdapter 
import com.zyron.filetree.FileTree
import java.io.File

class MainActivity : AppCompatActivity(), FileTreeClickListener {

        private fun initializeFileTree(fileTree: FileTree) {
        val fileTree = FileTree(this, "storage/emulated/0")
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val fileTreeIconProvider = IntendedFileIconProvider()
        val fileTreeAdapter = FileTreeAdapter(this, fileTree, fileTreeIconProvider, this)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = fileTreeAdapter
        fileTree.loadFileTree()
        fileTree.setAdapterUpdateListener(object : FileTreeAdapterUpdateListener {
            override fun onFileTreeUpdated(startPosition: Int, itemCount: Int) {
                runOnUiThread {
                    fileTreeAdapter.updateNodes(fileTree.getNodes())
                    fileTreeAdapter.notifyItemRangeChanged(startPosition, itemCount)
                }
            }
        })
    }

    override fun onFileClick(file: File) {
        Toast.makeText(this, "File clicked: ${file.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onFolderClick(folder: File) {
        Toast.makeText(this, "Folder clicked: ${folder.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onFileLongClick(file: File): Boolean {
        Toast.makeText(this, "File long-clicked: ${file.name}", Toast.LENGTH_SHORT).show()
        return true 
    }

    override fun onFolderLongClick(folder: File): Boolean {
        Toast.makeText(this, "Folder long-clicked: ${folder.name}", Toast.LENGTH_SHORT).show()
        return true 
    }
}
```