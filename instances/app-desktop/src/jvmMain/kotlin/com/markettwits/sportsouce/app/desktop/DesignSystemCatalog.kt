package com.markettwits.sportsouce.app.desktop

import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.use
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.catalog.SauceComponentCatalog
import java.io.File
import org.jetbrains.skia.EncodedImageFormat

/** Offline rendering of the actual Compose components, without bootstrapping the application. */
fun main(args: Array<String>) {
    val output = File(args.firstOrNull() ?: "build/design-system").apply { mkdirs() }
    for (dark in listOf(false, true)) {
        for (largeText in listOf(false, true)) {
            ImageComposeScene(
                width = if (largeText) 360 else 440,
                height = if (largeText) 3200 else 1440,
                density = Density(1f, fontScale = if (largeText) 2f else 1f),
            ) { SauceComponentCatalog(darkTheme = dark) }.use { scene ->
                // Let initial resource/layout effects complete, then render a deterministic frame.
                scene.render(0).close()
                scene.render(1_000_000_000).use { image ->
                    val data = checkNotNull(image.encodeToData(EncodedImageFormat.PNG))
                    val name = (if (dark) "dark" else "light") + (if (largeText) "-large-text" else "") + ".png"
                    data.use { File(output, name).writeBytes(it.bytes) }
                }
            }
        }
        ImageComposeScene(width = 440, height = 960) {
            SauceComponentCatalog(darkTheme = dark, initialSheetOpen = true)
        }.use { scene ->
            val time = System.nanoTime()
            repeat(12) { frame -> scene.render(time + frame * 100_000_000L).close() }
            scene.render(time + 1_500_000_000L).use { image ->
                checkNotNull(image.encodeToData(EncodedImageFormat.PNG)).use { data ->
                    File(output, if (dark) "sheet-dark.png" else "sheet-light.png").writeBytes(data.bytes)
                }
            }
        }
    }
    println("Compose component catalog rendered to ${output.absolutePath}")
}
