package com.markettwits.core_ui.items.components.navigation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Prevent a new or migrated screen from silently bypassing the root glass viewport. */
class NavigationScrollArchitectureTest {
    @Test
    fun allProductionScrollAreasUseTheSharedViewport() {
        val root = File(checkNotNull(System.getProperty("sportsauce.repositoryRoot")))
        check(File(root, "settings.gradle.kts").isFile)
        val files = listOf("components", "instances").flatMap { directory ->
            File(root, directory).walkTopDown().onEnter { it.name != "build" }
                .filter { it.extension == "kt" && productionSource.containsMatchIn(it.invariantSeparatorsPath) }
                .toList()
        }
        assertTrue(files.isNotEmpty(), "No production Kotlin sources found")
        val violations = files.filterNot {
            it.relativeTo(root).invariantSeparatorsPath in foundationImplementations
        }.flatMap { file ->
            violations(file.readText()).map { "${file.relativeTo(root)}: $it" }
        }
        assertTrue(violations.isEmpty(),
            "Use shared navigation scroll primitives (aliases are supported):\n${violations.joinToString("\n")}")
    }

    @Test
    fun guardDetectsDirectAliasesWildcardsAndQualifiedCalls() {
        assertEquals(1, violations("import androidx.compose.foundation.verticalScroll as scrollPage").size)
        assertEquals(1, violations("import androidx.compose.foundation.lazy.LazyColumn").size)
        assertEquals(1, violations("import androidx.compose.foundation.lazy.*\nfun page() { LazyColumn {} }").size)
        assertEquals(1, violations("import androidx.compose.foundation.lazy.grid.*\nfun page() { LazyVerticalGrid\n(columns) {} }").size)
        assertEquals(1, violations("fun page() { androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid(columns) {} }").size)
        assertEquals(1, violations("import androidx.compose.foundation.*\nfun page() { modifier.verticalScroll(state) }").size)
        assertTrue(violations("""
            import androidx.compose.foundation.lazy.grid.*
            import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid as LazyVerticalGrid
            fun page() { LazyVerticalGrid(columns) {} }
        """).isEmpty())
        assertTrue(violations("""
            import androidx.compose.foundation.lazy.grid.*
            // LazyVerticalGrid(columns) {}
            /* import androidx.compose.foundation.verticalScroll */
            val note = "androidx.compose.foundation.lazy.LazyColumn()"
            fun page() { rememberLazyGridState() }
        """).isEmpty())
    }

    private fun violations(source: String): List<String> {
        val code = nonCode.replace(source) { "\n".repeat(it.value.count { c -> c == '\n' }) }
        val imports = importPattern.findAll(code).map {
            it.groupValues[1] to it.groupValues[2].ifEmpty { it.groupValues[1].substringAfterLast('.') }
        }.toList()
        val problems = imports.filter { (path, _) -> path in primitives.keys }
            .map { (path, _) -> "direct Foundation import $path" }.toMutableList()
        // Explicit imports win over Kotlin wildcard imports; preserve the existing screen aliases.
        for ((path, sharedName) in primitives) {
            val name = path.substringAfterLast('.')
            val wildcard = path.substringBeforeLast('.') + ".*"
            val shared = imports.any { (importPath, alias) -> importPath == "$sharedPackage.$sharedName" && alias == name }
            if (!shared && imports.any { it.first == wildcard } && Regex("\\b$name\\s*[({]").containsMatchIn(code)) {
                problems += "$name resolves through $wildcard"
            }
            if (Regex("\\b${Regex.escape(path)}\\s*[({]").containsMatchIn(code)) {
                problems += "qualified Foundation call $path"
            }
        }
        return problems
    }

    private companion object {
        const val sharedPackage = "com.markettwits.core_ui.items.components.navigation"
        val primitives = mapOf(
            "androidx.compose.foundation.verticalScroll" to "sauceVerticalScroll",
            "androidx.compose.foundation.lazy.LazyColumn" to "SauceLazyColumn",
            "androidx.compose.foundation.lazy.grid.LazyVerticalGrid" to "SauceLazyVerticalGrid",
            "androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid" to "SauceLazyVerticalStaggeredGrid",
        )
        val foundationImplementations = setOf(
            "components/core/ui/src/commonMain/kotlin/com/markettwits/core_ui/items/components/navigation/SauceVerticalScroll.kt",
            "components/core/ui/src/commonMain/kotlin/com/markettwits/core_ui/items/components/navigation/SauceLazyLayouts.kt",
        )
        val productionSource = Regex("/src/(?:[^/]*Main|main)/")
        val importPattern = Regex("(?m)^\\s*import\\s+([\\w.*]+)(?:[ \\t]+as[ \\t]+(\\w+))?")
        val nonCode = Regex("\"\"\"[\\s\\S]*?\"\"\"|\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'|//[^\\n]*|/\\*[\\s\\S]*?\\*/")
    }
}
