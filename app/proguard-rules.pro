# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep the FileTree class
-keep class com.zyron.filetree.FileTree { *; }

# Keep the FileTreeAdapterUpdateListener interface
-keep class com.zyron.filetree.FileTreeAdapterUpdateListener { *; }

# Keep the FileTreeAdapter class
-keep class com.zyron.filetree.adapter.FileTreeAdapter { *; }

# Keep the FileTreeClickListener interface
-keep class com.zyron.filetree.adapter.FileTreeClickListener { *; }

# Keep the FileTreeIconProvider class
-keep class com.zyron.filetree.provider.FileTreeIconProvider { *; }