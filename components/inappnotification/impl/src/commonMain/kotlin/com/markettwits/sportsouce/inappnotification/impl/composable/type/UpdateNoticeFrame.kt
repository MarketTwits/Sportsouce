package com.markettwits.sportsouce.inappnotification.impl.composable.type

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceMotion
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceStatusColors

/** The visual strip is 40 dp; its surrounding tap area remains at least 48 dp. */
@Composable
internal fun UpdateNoticeFrame(
    colors: SauceStatusColors,
    actionLabel: String? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            Modifier.widthIn(max = 600.dp).fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClickLabel = actionLabel, onClick = onClick) else Modifier)
                .heightIn(min = 48.dp)
                .padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.extraSmall),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                Modifier.fillMaxWidth().clip(SauceShapes.field)
                    .background(colors.container).border(1.dp, colors.content.copy(alpha = 0.12f), SauceShapes.field)
                    .animateContentSize(tween(SauceMotion.feedbackMillis, easing = SauceMotion.feedbackEasing))
                    .heightIn(min = 40.dp).padding(horizontal = SauceSpacing.medium, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small, Alignment.CenterHorizontally),
                content = content,
            )
        }
    }
}
