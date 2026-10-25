package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.support.presentation.component.StartSupportComponent
import com.markettwits.sportsouce.start.support.presentation.components.StartSupportScreen
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore

@Composable
internal fun StartSupport(
    modifier: Modifier,
    component: StartSupportComponent,
    sponsors: List<StartItem.Sponsor> = emptyList(),
    onClickSponsorUrl: (String) -> Unit = {},
    eventContent: (EventContent) -> Unit,
    isActive: Boolean = true,
) {
    val state by component.state.collectAsState()
    StartServiceCard(
        modifier = modifier,
        title = "Поддержка проекта",
        description = "Помогите развивать спортивные старты и делать их доступнее.",
        action = "Поддержать проект",
        icon = Icons.Outlined.FavoriteBorder,
        onClick = { component.obtainEvent(StartSupportStore.Intent.OnOpen) },
        content = if (sponsors.isEmpty()) null else {
            {
                HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = SauceTheme.colors.border)
                Text("Партнёры", Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp),
                    style = MaterialTheme.typography.labelLarge, color = SauceTheme.colors.textSecondary)
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(sponsors, key = { it.id }) { sponsor ->
                        SponsorCard(sponsor, onClick = { onClickSponsorUrl(sponsor.link) })
                    }
                }
            }
        },
    )
    if (state.isOpen && isActive) key(state.sheetSession) {
        DefaultModalBottomSheet(title = "Поддержка проекта",
            onDismissRequest = { component.obtainEvent(StartSupportStore.Intent.OnClose) }) {
            StartSupportScreen(
                component = component,
                event = eventContent,
                onClickDismiss = { component.obtainEvent(StartSupportStore.Intent.OnClose) },
            )
        }
    }
}

@Composable
private fun SponsorCard(sponsor: StartItem.Sponsor, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.width(144.dp),
        shape = SauceShapes.field,
        color = SauceTheme.colors.surfaceSubtle,
        onClick = onClick,
    ) {
        Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SubcomposeAsyncImage(
                model = imageRequestCrossfade(sponsor.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth().height(80.dp).clip(SauceShapes.badge)
                    .background(SauceTheme.colors.surface),
                error = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Business, null, Modifier.size(32.dp), tint = SauceTheme.colors.textMuted)
                    }
                },
                loading = { SauceSkeleton(Modifier.fillMaxSize()) },
                success = { SubcomposeAsyncImageContent(modifier = Modifier.padding(8.dp)) },
            )
            Text(sponsor.name, Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall,
                color = SauceTheme.colors.textPrimary, minLines = 2, maxLines = 2,
                overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
        }
    }
}
