# Keep our data classes referenced by serialization / Room.
-keep class com.bistpicker.mobile.data.** { *; }

# kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.bistpicker.mobile.**$$serializer { *; }
-keepclassmembers class com.bistpicker.mobile.** {
    *** Companion;
}
-keepclasseswithmembers class com.bistpicker.mobile.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Compose
-dontwarn androidx.compose.**
