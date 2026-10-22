package com.markettwits.sportsouce.unauthorized.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeContent

/** Remote photos are optional; the invitation remains usable on a first offline launch. */
@Composable
internal fun UnAuthorizedProfileContent(
    playIntro: Boolean = false,
    content: GuestWelcomeContent = GuestWelcomeContent.Local,
    onClickAuth: () -> Unit,
) {
    val motion = rememberGuestWelcomeMotion(playIntro, content.headingLines)
    GuestProfileStatusBar()
    val colors = SauceTheme.colors
    val scroll = rememberScrollState()
    val limit = with(LocalDensity.current) { 24.dp.toPx() }
    val (parallax, scrollConnection) = rememberGuestProfileParallax(scroll, limit)
    BoxWithConstraints(Modifier.fillMaxSize().background(colors.surface)) {
        val pageHeight = maxHeight
        val landscape = maxWidth >= 600.dp && maxWidth > maxHeight
        GuestProfileBackground(
            content = content,
            // The overscan used for parallax must stay inside the photo viewport.
            // Otherwise it leaks into the navigation bar's rounded outer corners.
            modifier = Modifier.matchParentSize().clipToBounds().graphicsLayer {
                val overscan = 1f + (limit * 2f / size.height.coerceAtLeast(1f))
                scaleX = motion.photoScale * overscan
                scaleY = motion.photoScale * overscan
                translationY = parallax.offset(scroll, limit)
            },
        )
        Box(Modifier.matchParentSize().background(Brush.verticalGradient(
            0f to Color.Black.copy(alpha = .62f),
            .25f to Color.Black.copy(alpha = .38f),
            .55f to Color.Black.copy(alpha = .08f),
            1f to Color.Black.copy(alpha = .42f),
        )))
        if (landscape) {
            Row(
                Modifier.fillMaxSize().nestedScroll(scrollConnection).sauceVerticalScroll(scroll)
                    .heightIn(min = pageHeight).statusBarsPadding().padding(SauceSpacing.extraLarge),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SauceSpacing.extraLarge),
            ) {
                GuestProfileHeading(motion, content.headingLines, Modifier.weight(1f).padding(vertical = SauceSpacing.section))
                GuestProfileSignInPanel(
                    modifier = Modifier.weight(1f).widthIn(max = 480.dp),
                    shape = SauceShapes.card,
                    onClickAuth = onClickAuth,
                    motion = motion,
                    content = content,
                )
            }
        } else {
            val heroHeight = (pageHeight * .65f).coerceIn(260.dp, 680.dp)
            Column(
                Modifier.fillMaxSize().nestedScroll(scrollConnection).sauceVerticalScroll(scroll).heightIn(min = pageHeight),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(Modifier.fillMaxWidth().heightIn(min = 260.dp)) {
                    GuestProfileHeading(
                        motion,
                        content.headingLines,
                        Modifier.align(Alignment.TopCenter).statusBarsPadding()
                            .padding(top = (heroHeight * .12f).coerceIn(32.dp, 64.dp),
                                bottom = SauceSpacing.section, start = SauceSpacing.extraLarge, end = SauceSpacing.extraLarge),
                    )
                }
                GuestProfileSignInPanel(
                    modifier = Modifier.fillMaxWidth(),
                    shape = SauceShapes.sheet,
                    onClickAuth = onClickAuth,
                    motion = motion,
                    content = content,
                )
            }
        }
    }
}

@Composable
private fun GuestProfileHeading(motion: GuestWelcomeMotion, lines: List<String>, modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier.widthIn(max = 480.dp).fillMaxWidth()
            .clearAndSetSemantics {
            // Accessibility reads the complete heading once, rather than every typed letter.
            text = AnnotatedString(lines.joinToString("\n"))
            heading()
        },
    ) {
        val density = LocalDensity.current
        val measurer = rememberTextMeasurer()
        val display = MaterialTheme.typography.displaySmall.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 40.sp,
            lineHeight = 44.sp,
            letterSpacing = (-.9).sp,
            lineBreak = LineBreak.Heading,
        )
        val width = with(density) { maxWidth.toPx() }
        // Fit the complete, static copy once. Typing never changes its size or line breaks.
        // Keep one size for all lines, and let enlarged accessibility text wrap naturally.
        val fit = remember(lines, display, width, density, measurer) {
            if (density.fontScale > 1.1f) 1f else {
                val measured = measurer.measure(lines.joinToString("\n"), display, softWrap = false)
                (width / measured.size.width.coerceAtLeast(1)).coerceIn(.8f, 1f)
            }
        }
        val style = display.copy(
            fontSize = (40f * fit).sp,
            lineHeight = (44f * fit).sp,
            letterSpacing = (-.9f * fit).sp,
            shadow = Shadow(Color.Black.copy(alpha = .18f), blurRadius = with(density) { 6.dp.toPx() }),
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            lines.forEachIndexed { lineIndex, line ->
                val text = buildAnnotatedString {
                    line.forEachIndexed { index, character ->
                        withStyle(SpanStyle(color = Color.White.copy(alpha = motion.characterAlpha(lineIndex, index)))) {
                            append(character)
                        }
                    }
                }
                Text(
                    text = text,
                    modifier = Modifier.fillMaxWidth().blur(motion.lineBlur(lineIndex).dp, BlurredEdgeTreatment.Unbounded),
                    style = style,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun GuestProfileSignInPanel(
    modifier: Modifier,
    shape: Shape,
    onClickAuth: () -> Unit,
    motion: GuestWelcomeMotion,
    content: GuestWelcomeContent,
) {
    val colors = SauceTheme.colors
    Box(
        modifier = modifier.graphicsLayer {
            translationY = motion.panelOffset.dp.toPx()
        }.clip(shape).background(colors.surface)
            .padding(horizontal = SauceSpacing.extraLarge)
            .padding(top = SauceSpacing.section, bottom = SauceSpacing.extraLarge),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            Modifier.widthIn(max = 480.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = revealedWords(content.panelTitle, colors.textPrimary, motion::titleWordAlpha),
                modifier = Modifier.widthIn(max = 360.dp).blur(motion.titleBlur.dp, BlurredEdgeTreatment.Unbounded),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 24.sp,
                    lineHeight = 29.sp,
                    letterSpacing = (-.45).sp,
                    lineBreak = LineBreak.Heading,
                ),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(SauceSpacing.medium))
            Text(
                text = revealedWords(content.description, colors.textSecondary, motion::descriptionWordAlpha),
                modifier = Modifier.widthIn(max = 320.dp).blur(motion.descriptionBlur.dp, BlurredEdgeTreatment.Unbounded),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp,
                    lineHeight = 23.sp,
                    letterSpacing = 0.sp,
                    lineBreak = LineBreak.Heading,
                ),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(SauceSpacing.extraLarge))
            SauceButton("Войти", onClickAuth)
        }
    }
}

/** Reveal words in their final positions, preserving wrapping and accessibility text. */
private fun revealedWords(text: String, color: Color, alpha: (Int, Int) -> Float) = buildAnnotatedString {
    val words = text.split(' ')
    words.forEachIndexed { index, word ->
        if (index > 0) append(' ')
        withStyle(SpanStyle(color = color.copy(alpha = color.alpha * alpha(index, words.size)))) { append(word) }
    }
}

/** White status icons belong to the photograph; the root navigation retains its own appearance. */
@Composable
internal expect fun GuestProfileStatusBar()
