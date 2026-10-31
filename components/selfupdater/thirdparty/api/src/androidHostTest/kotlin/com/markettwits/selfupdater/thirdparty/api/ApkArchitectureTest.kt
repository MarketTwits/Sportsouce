package com.markettwits.selfupdater.thirdparty.api

import org.junit.Test
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ApkArchitectureTest {
    @Test
    fun acceptsPureJavaAndCompatibleNativePackagesButRejectsIncompatibleAbis() {
        val apk = Files.createTempFile("apk-abi", ".apk").toFile()
        fun write(vararg names: String) {
            ZipOutputStream(apk.outputStream()).use { zip ->
                names.forEach { name ->
                    zip.putNextEntry(ZipEntry(name))
                    zip.write(1)
                    zip.closeEntry()
                }
            }
        }
        try {
            write("classes.dex", "assets/lib/arm64-v8a/example.so")
            assertTrue(apkSupportsDeviceAbis(apk, setOf("arm64-v8a")))
            write("lib/armeabi-v7a/libexample.so", "lib/x86_64/libexample.so")
            assertTrue(apkSupportsDeviceAbis(apk, setOf("arm64-v8a", "armeabi-v7a")))
            assertFalse(apkSupportsDeviceAbis(apk, setOf("arm64-v8a")))
            assertFalse(apkSupportsDeviceAbis(apk, emptySet()))
        } finally { apk.delete() }
    }
}
