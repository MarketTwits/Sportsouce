package com.markettwits.sportsouce.club.info.presentation.components.features

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import androidx.compose.foundation.lazy.items
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.common.presentation.*
import com.markettwits.sportsouce.club.cloud.models.subscription.SubscriptionColor
import com.markettwits.sportsouce.club.info.domain.models.ClubFeature
import com.markettwits.core_ui.items.screens.FullImageScreen

@Composable
internal fun ClubFeaturesContent(modifier: Modifier = Modifier, features: List<ClubFeature>) {
    var image by rememberSaveable { mutableStateOf("") }
    SauceLazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (features.isEmpty()) item { ClubNotice("Возможности клуба", "Подробности появятся здесь позже.") }
        items(features, key = { it.id }) { feature ->
            OnBackgroundCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (!feature.imageUrl.isNullOrBlank()) ClubPhoto(feature.imageUrl, feature.name, Modifier.fillMaxWidth().aspectRatio(16f / 9f),
                        open = { image = feature.imageUrl })
                    if (feature.name.isNotBlank()) Text(feature.name, style = MaterialTheme.typography.titleMedium, color = SubscriptionColor.PURPLE.clubColors().content)
                    HtmlText(text = feature.description, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary, showImages = false)
                }
            }
        }
    }
    if (image.isNotBlank()) FullImageScreen(image) { image = "" }
}
