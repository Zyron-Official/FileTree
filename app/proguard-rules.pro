# Preserve all classes in the filetree module
-keep class com.zyron.filetree.** { *; }

# Preserve all classes in the filetree adapter module
-keep class com.zyron.filetree.adapter.** { *; }

# Preserve all classes in the filetree provider module
-keep class com.zyron.filetree.provider.** { *; }

# Preserve data binding classes
-keep class com.zyron.filetree.databinding.** { *; }

# Preserve classes referenced by reflection
-keep class * {
    @androidx.annotation.Keep *;
    @androidx.databinding.BindingAdapter *;
    @androidx.databinding.BindingConversion *;
}

# Keep all annotations (useful for libraries that use annotations)
-keepattributes *Annotation*

# Keep all Parcelable classes
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Preserve data binding generated classes
-keep class com.zyron.filetree.BR { *; }
-keep class * extends androidx.databinding.ViewDataBinding { *; }

# Prevent removal of specific classes used in reflection
-dontwarn java.lang.invoke.**