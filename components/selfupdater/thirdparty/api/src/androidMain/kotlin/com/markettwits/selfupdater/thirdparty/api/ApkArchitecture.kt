package com.markettwits.selfupdater.thirdparty.api

import java.io.File
import java.util.zip.ZipFile

/** APKs without native code are architecture-independent. */
internal fun apkSupportsDeviceAbis(apk: File, supportedAbis: Set<String>): Boolean = ZipFile(apk).use { zip ->
    var hasNativeLibraries = false
    var hasCompatibleLibrary = false
    val entries = zip.entries()
    while (entries.hasMoreElements()) {
        val entry = entries.nextElement()
        if (entry.isDirectory) continue
        val parts = entry.name.split('/')
        if (parts.size != 3 || parts[0] != "lib" || !parts[2].endsWith(".so")) continue
        hasNativeLibraries = true
        if (parts[1] in supportedAbis) hasCompatibleLibrary = true
    }
    !hasNativeLibraries || hasCompatibleLibrary
}
