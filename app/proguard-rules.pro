# Preserve the FileTree classes and their members
-keep class com.zyron.filetree.** { *; }

# Preserve the adapter classes and their members
-keep class com.zyron.filetree.adapter.** { *; }

# Preserve the provider classes and their members
-keep class com.zyron.filetree.provider.** { *; }

# Preserve the listener classes and their members
-keep class com.zyron.filetree.FileTreeAdapterUpdateListener { *; }
-keep class com.zyron.filetree.FileTreeClickListener { *; }

# Exclude DataBinderMapperImpl from multiple sources
-dontwarn com.zyron.filetree.DataBinderMapperImpl
-keep class com.zyron.filetree.DataBinderMapperImpl { *; }