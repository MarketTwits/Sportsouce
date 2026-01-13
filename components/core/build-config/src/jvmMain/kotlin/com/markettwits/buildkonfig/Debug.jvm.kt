package com.markettwits.buildkonfig

import java.io.File
import java.lang.management.ManagementFactory

private object DebugRuntimeMarker

actual val isDebugMode: Boolean
    get() = resolveDebugMode()

private fun resolveDebugMode(): Boolean {
    val explicit = System.getProperty("sportsauce.debug")
    if (explicit != null) {
        return explicit.toBoolean()
    }
    return isDebuggerAttached() || isComposeDevRun() || isRunningFromClasses()
}

private fun isDebuggerAttached(): Boolean {
    return ManagementFactory.getRuntimeMXBean()
        .inputArguments
        .any { it.contains("jdwp") }
}

private fun isComposeDevRun(): Boolean {
    return System.getProperty("compose.application.resources.dir") != null
}

private fun isRunningFromClasses(): Boolean {
    val codeSourcePath = DebugRuntimeMarker::class.java.protectionDomain.codeSource?.location?.path
    if (codeSourcePath != null && isDevPath(codeSourcePath)) {
        return true
    }
    val classPath = System.getProperty("java.class.path") ?: return false
    return classPath
        .split(File.pathSeparator)
        .any { isDevPath(it) }
}

private fun isDevPath(path: String): Boolean {
    val normalized = path.replace('\\', '/')
    return normalized.contains("/build/classes/") ||
            normalized.contains("/build/resources/") ||
            normalized.contains("/out/production/")
}
