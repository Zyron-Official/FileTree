# Preserve all classes in the filetree module
-keep class com.zyron.filetree.** { *; }

# Preserve all classes in the filetree adapter module
-keep class com.zyron.filetree.adapter.** { *; }

# Preserve all classes in the filetree provider module
-keep class com.zyron.filetree.provider.** { *; }

# Preserve data binding classes
-keep class com.zyron.filetree.databinding.** { *; }

# Exclude DataBinderMapperImpl from warnings and duplication issues
-dontwarn com.zyron.filetree.DataBinderMapperImpl
-keep class com.zyron.filetree.DataBinderMapperImpl { *; }

# Keep all annotations (useful for libraries that use annotations)
-keepattributes *Annotation*

# Keep all Parcelable classes
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep all classes with @Keep annotation
-keep @androidx.annotation.Keep class * { *; }

# Keep the classes that are referenced in the code but might be stripped away
-keep class com.zyron.filetree.viewmodel.** { *; }
-keep class com.zyron.filetree.util.** { *; }

# Ensure that R8 doesn't remove used classes
-dontwarn javax.annotation.**
-dontwarn org.jetbrains.annotations.**

# Avoid conflicts with duplicate classes by keeping necessary classes
-keep class com.zyron.filetree.** { *; }
-keep class com.zyron.filetree.adapter.** { *; }
-keep class com.zyron.filetree.provider.** { *; }

# Add rules to avoid shrinking and obfuscating classes used by reflection
-keep class * {
    @androidx.annotation.Keep *;
    @androidx.databinding.BindingAdapter *;
    @androidx.databinding.BindingConversion *;
}

# Keep classes that are used with data binding
-keep class com.zyron.filetree.BR { *; }

# General rules to keep data binding generated classes
-keep class * extends androidx.databinding.ViewDataBinding { *; }