package com.markettwits.core_ui.items.components.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.markettwits.core_ui.items.components.navigation.LocalNavigationViewport
import com.markettwits.core_ui.items.components.navigation.LocalScrollContentBottomPadding
import com.markettwits.core_ui.items.components.topbar.LocalSauceTopBar
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme

/** A separate window must not inherit the underlying screen's glass-bar geometry. */
@Composable
fun SauceDialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        CompositionLocalProvider(LocalNavigationViewport provides null, LocalSauceTopBar provides null,
            LocalScrollContentBottomPadding provides 0.dp) {
            Box(Modifier.padding(SauceSpacing.extraLarge).widthIn(max = 480.dp).fillMaxWidth()) { content() }
        }
    }
}

/** The form scrolls in a short viewport; primary and dismiss actions stay reachable. */
@Composable
fun SauceDialogContent(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = SauceTheme.colors
    Surface(modifier.widthIn(max = 480.dp).fillMaxWidth(), shape = SauceShapes.card,
        color = colors.surfaceRaised, contentColor = colors.textPrimary) {
        Column(Modifier.padding(SauceSpacing.extraLarge), verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
            Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
                Text(title, Modifier.fillMaxWidth().semantics { heading() }, style = MaterialTheme.typography.titleLarge)
                content()
            }
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small), content = actions)
        }
    }
}
