# Keep all classes in filetree module
-keep class com.zyron.filetree.** { *; }
-keep class com.zyron.filetree.adapter.** { *; }
-keep class com.zyron.filetree.provider.** { *; }

# Exclude DataBinderMapperImpl to avoid duplication
-dontwarn com.zyron.filetree.DataBinderMapperImpl
-keep class com.zyron.filetree.DataBinderMapperImpl { *; }