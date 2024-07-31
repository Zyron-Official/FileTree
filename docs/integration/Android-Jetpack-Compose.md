### Example (Android - Jetpack Compose):

#### 1. Define a composable funtion 

```kotlin 
@Composable
fun FileTreeScreen(viewModel: FileTreeViewModel = viewModel()) {
    val fileTreeState by viewModel.fileTreeState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("File Tree") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(fileTreeState.nodes) { node ->
                FileTreeItem(node, viewModel)
            }
        }
    }
}

@Composable
fun FileTreeItem(fileTreeNode: FileTreeNode, viewModel: FileTreeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Handle item click */ }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron), // Update with actual icon
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = fileTreeNode.name, style = MaterialTheme.typography.body1)
    }
}
```

#### 2. Create a ViewModel

```kotlin
class FileTreeViewModel : ViewModel() {
    private val _fileTreeState = MutableStateFlow(FileTreeState())
    val fileTreeState: StateFlow<FileTreeState> = _fileTreeState.asStateFlow()

    init {
        loadFileTree()
    }

    private fun loadFileTree() {
        // Initialize FileTree and load data
        val fileTree = FileTree(/* context */, "/storage/emulated/0")
        fileTree.loadFileTree()
        fileTree.setAdapterUpdateListener(object : FileTreeAdapterUpdateListener {
            override fun onFileTreeUpdated(startPosition: Int, itemCount: Int) {
                _fileTreeState.value = FileTreeState(fileTree.getNodes())
            }
        })
    }
}

data class FileTreeState(val nodes: List<FileTreeNode> = emptyList())
```

#### 3. Setup in Activity

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FileTreeScreen()
        }
    }
}
```

### Explanation 

- `FileTreeScreen:` Main composable function displaying the file tree.
 
- `FileTreeItem:` Composable for each item in the file tree.
 
- `FileTreeViewModel:` Manages the state and updates the UI when data changes.
 
- `FileTreeState:` Data class for holding the file tree nodes.