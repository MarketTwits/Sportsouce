package com.markettwits.sportsouce.auth.flow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStage
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.validation.SignUpValidationBase
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.components.SignUpStages
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class SignUpFieldsDesignTest {
    @Test fun everyStepDisplaysSpecificAccessibleErrorsInBothThemesAndLargeText() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) for (stage in SignUpStage.entries) {
            val errors = SignUpValidationBase().errors(SignUpStatement(), setOf(stage))
            val expected = when (stage) {
                SignUpStage.FIRST -> listOf(errors.phone!!, errors.email!!)
                SignUpStage.SECOND -> listOf(errors.name!!, errors.surname!!, errors.sex!!, errors.birthday!!)
                SignUpStage.THIRD -> listOf(errors.password!!, errors.repeatPassword!!)
            }
            ImageComposeScene(width = 360, height = if (scale == 1f) 700 else 1400, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(24.dp)) {
                        SignUpStages(signUpStage = stage, statement = SignUpStatement(),
                            focusManager = LocalFocusManager.current, onValueChanged = {}, errors = errors)
                    }
                }
            }.use { scene ->
                repeat(12) { scene.render(System.nanoTime() + it * 100_000_000L).close() }
                fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                for (message in expected) {
                    assertTrue(nodes.any { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == message } }, message)
                    assertTrue(nodes.any { it.config.getOrNull(SemanticsProperties.Error) == message }, "Missing accessible field error: $message")
                }
                for (node in nodes) {
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                    layouts.forEach { layout ->
                        // Supporting text can retain a wider paragraph constraint than its measured
                        // text width. Check the actual glyph bounds rather than that constraint.
                        assertFalse(layout.didOverflowHeight, "Clipped error at scale=$scale: ${layout.layoutInput.text}")
                        for (line in 0 until layout.lineCount) {
                            assertTrue(layout.getLineRight(line) - layout.getLineLeft(line) <= layout.size.width + 1,
                                "Error exceeds the field width at scale=$scale: ${layout.layoutInput.text}")
                        }
                    }
                }
                scene.render(System.nanoTime() + 2_000_000_000L).use { image -> image.encodeToData()!!.use { data ->
                    File("build/design-system/sign-up-${stage.name.lowercase()}-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}.png")
                        .apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                } }
            }
        }
    }
}
