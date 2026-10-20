package com.markettwits.core_ui.items.components

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class SheetHostArchitectureTest {
    @Test
    fun everyProductionModalSheetUsesTheStableHost() {
        val root = File(checkNotNull(System.getProperty("sportsauce.repositoryRoot")))
        val host = "components/core/ui/src/commonMain/kotlin/com/markettwits/core_ui/items/components/bottom_sheet/SauceModalBottomSheet.kt"
        val violations = listOf("components", "instances").flatMap { directory ->
            File(root, directory).walkTopDown().onEnter { it.name != "build" }
                .filter { it.extension == "kt" && Regex("/src/(?:[^/]*Main|main)/").containsMatchIn(it.invariantSeparatorsPath) }
                .filterNot { it.relativeTo(root).invariantSeparatorsPath == host }
                .filter { file ->
                    val code = nonCode.replace(file.readText(), "")
                    val direct = Regex("(?m)^\\s*import\\s+androidx\\.compose\\.material3\\.ModalBottomSheet\\b")
                    val qualified = Regex("androidx\\.compose\\.material3\\.ModalBottomSheet\\s*\\(")
                    val wildcard = Regex("(?m)^\\s*import\\s+androidx\\.compose\\.material3\\.\\*")
                    direct.containsMatchIn(code) || qualified.containsMatchIn(code) ||
                        (wildcard.containsMatchIn(code) && Regex("\\bModalBottomSheet\\s*\\(").containsMatchIn(code))
                }.map { it.relativeTo(root).invariantSeparatorsPath }.toList()
        }
        assertTrue(violations.isEmpty(), "Use DefaultModalBottomSheet or SauceModalBottomSheet so edge scrolling stays stable:\n${violations.joinToString("\n")}")
    }

    private val nonCode = Regex("\"\"\"[\\s\\S]*?\"\"\"|\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'|//[^\\n]*|/\\*[\\s\\S]*?\\*/")
}
