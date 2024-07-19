# Preserve the FileTree classes and their members
-keep class com.zyron.filetree.** { *; }

# Preserve the adapter classes and their members
-keep class com.zyron.filetree.adapter.** { *; }

# Preserve the provider classes and their members
-keep class com.zyron.filetree.provider.** { *; }

# Preserve the listener classes and their members
-keep class com.zyron.filetree.FileTreeAdapterUpdateListener { *; }
-keep class com.zyron.filetree.FileTreeClickListener { *; }

-keep class java.lang.invoke.StringConcatFactory { *; }

# Keep standard library classes used by R8
-dontwarn java.lang.invoke.**
-keep class java.lang.invoke.** { *; }

# Exclude the RecyclerViewItemBinding from the filetree module
-dontwarn com.zyron.filetree.databinding.RecyclerViewItemBinding
-keep class com.zyron.filetree.databinding.RecyclerViewItemBinding { *; }