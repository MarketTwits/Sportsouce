
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn com.fasterxml.jackson.core.type.TypeReference

 -keepattributes Exceptions, Signature, InnerClasses,*Annotation*
 -dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations
 -keepclassmembers class kotlinx.serialization.json.** {
     *** Companion;
 }
 -keepclasseswithmembers class kotlinx.serialization.json.** {
     kotlinx.serialization.KSerializer serializer(...);
 }
 -keep public class * extends android.app.Activity
  -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
 -dontwarn javax.annotation.**
