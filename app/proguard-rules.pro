
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn com.fasterxml.jackson.core.type.TypeReference
##---------------End: proguard configuration for Pusher Java Client  ----------
 -keepattributes *Annotation*, InnerClasses
 -dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

 # kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
 -keepclassmembers class kotlinx.serialization.json.** {
     *** Companion;
 }
 -keepclasseswithmembers class kotlinx.serialization.json.** {
     kotlinx.serialization.KSerializer serializer(...);
 }
  # With R8 full mode generic signatures are stripped for classes that are not
  # kept. Suspend functions are wrapped in continuations where the type argument
  # is used.
  -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
 # Ignore JSR 305 annotations for embedding nullability information.
 -dontwarn javax.annotation.**

-keep class kotlin.Unit {
    *;
}