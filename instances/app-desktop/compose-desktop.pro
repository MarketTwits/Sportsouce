-keepclasseswithmembers public class com.markettwits.sportsouce.app.desktop.MainKt {
    public static void main(java.lang.String[]);
}

# Compose Desktop already includes the baseline Kotlin/Compose/coroutines rules.
# Keep only app-specific reflection/service-loader entry points here.

# Preserve bytecode metadata used by Kotlin, ServiceLoader, and Koin DSL lambdas.
-keepattributes Signature,InnerClasses,EnclosingMethod,*Annotation*

# Koin runtime itself should remain intact.
-keep class org.koin.** { *; }

# Coil image loading is wired explicitly from the desktop image loader.

# Decompose Compose Desktop resolves main-thread checker via ServiceLoader.
-keep class com.arkivanov.decompose.extensions.compose.mainthread.SwingMainThreadChecker { *; }

# Ktor discovers serialization extensions via ServiceLoader.
-keep class * implements io.ktor.serialization.kotlinx.KotlinxSerializationExtensionProvider { *; }
-keep class io.ktor.serialization.kotlinx.json.KotlinxSerializationJsonExtensionProvider { *; }

# Coil desktop image loading still needs its Ktor fetcher/runtime in release.
-keep class coil3.network.ktor3.** { *; }
-keep class io.ktor.client.engine.okhttp.** { *; }

# OkHttp/Okio are sensitive to bytecode rewriting in release builds.
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Keep generated serializers discoverable at runtime.
-keep class **$$serializer { *; }
-dontnote **$$serializer

# Suppress known desktop/JVM-only noise from optional Android/debug integrations.
-dontwarn android.util.**
-dontwarn io.ktor.utils.io.jvm.javaio.PollersKt
-dontwarn kotlin.Deprecated$Container
-dontwarn org.graalvm.nativeimage.hosted.**
-dontwarn com.oracle.svm.core.annotate.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.openjsse.**
-dontwarn org.slf4j.**

-dontnote com.markettwits.cahce.InMemoryCache
-dontnote io.ktor.client.plugins.logging.LoggerJvmKt
-dontnote coil3.network.ktor3.**
-dontnote io.ktor.client.engine.okhttp.**
-dontnote io.ktor.http.content.BlockingBridgeKt
-dontnote io.ktor.network.sockets.UnixSocketAddress
-dontnote org.slf4j.LoggerFactory
-dontnote org.slf4j.helpers.SubstituteLogger
