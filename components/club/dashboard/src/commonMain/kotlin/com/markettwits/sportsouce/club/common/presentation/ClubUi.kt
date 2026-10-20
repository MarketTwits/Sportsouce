package com.markettwits.sportsouce.club.common.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ClubPhoto(url: String, description: String, modifier: Modifier = Modifier, open: (() -> Unit)? = null,
    shape: Shape = SauceShapes.card) {
    SubcomposeAsyncImage(model = imageRequestCrossfade(url), contentDescription = description,
        alignment = Alignment.TopCenter, contentScale = ContentScale.Crop, modifier = modifier.clip(shape)
            .then(if (open != null && url.isNotBlank()) Modifier.clickable(onClickLabel = "Открыть фото", onClick = open) else Modifier),
        loading = { SauceSkeleton(Modifier.fillMaxSize()) },
        success = { SubcomposeAsyncImageContent() },
        error = { Box(Modifier.fillMaxSize().background(SauceTheme.colors.surfaceSubtle), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.Image, null, Modifier.size(32.dp), tint = SauceTheme.colors.textMuted)
        } })
}

@Composable
internal fun ClubLoading(label: String = "Загрузка клуба") {
    Column(Modifier.fillMaxWidth().semantics {
        contentDescription = label; progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
    }, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(3) { OnBackgroundCard {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SauceSkeleton(Modifier.fillMaxWidth(0.55f).height(20.dp))
                SauceSkeleton(Modifier.fillMaxWidth().height(16.dp))
                SauceSkeleton(Modifier.fillMaxWidth(0.8f).height(16.dp))
            }
        } }
    }
}

@Composable
internal fun ClubNotice(title: String, description: String, retry: (() -> Unit)? = null) {
    OnBackgroundCard {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            if (retry != null) SauceButton("Повторить попытку", retry, variant = SauceButtonVariant.Outlined)
        }
    }
}
