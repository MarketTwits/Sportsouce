package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.buttons.SauceClickTarget
import com.markettwits.core_ui.items.components.divider.BaseDivider
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.image.social_networks.IconTelegram
import com.markettwits.core_ui.items.image.social_networks.IconVk
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer

@Composable
internal fun StartOrganizers(modifier: Modifier = Modifier, organizer: List<Organizer>,
    onClickUrl: (String) -> Unit, onClickPhone: (String) -> Unit) {
    if (organizer.isEmpty()) return
    var selected by remember { mutableStateOf<Organizer?>(null) }
    StartContentBasePanel(modifier, label = "Контактное лицо") {
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            organizer.sortedByDescending { it.isMain }.forEachIndexed { index, person ->
                if (index > 0) BaseDivider()
                SauceClickTarget(onClick = { selected = person }, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                        Row(Modifier.fillMaxWidth().heightIn(min = SauceSizes.touchTarget), verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                            OrganizerAvatar(person)
                            OrganizerIdentity(person, Modifier.weight(1f))
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                                Modifier.size(SauceSizes.touchTarget).padding(SauceSpacing.medium), tint = SauceTheme.colors.textMuted)
                        }
                        if (!person.hasContacts()) Text("Контакты не указаны", Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
                    }
                }
            }
        }
    }
    selected?.let { person ->
        DefaultModalBottomSheet(title = "Контакты организатора", onDismissRequest = { selected = null }) {
            OrganizerContactContent(person, onClickUrl, onClickPhone,
                Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(SauceSpacing.large))
        }
    }
}

private fun Organizer.hasContacts() = !phone.isNullOrBlank() || socialNetworks.any { !it.url.isNullOrBlank() }

@Composable
private fun OrganizerIdentity(person: Organizer, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
        Text(person.name, Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
        Text(if (person.isMain) "Главный организатор" else "Организатор", Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
    }
}

@Composable
private fun OrganizerAvatar(person: Organizer) {
    Box(Modifier.size(SauceSizes.touchTarget).clip(CircleShape).background(SauceTheme.colors.surfaceSubtle), contentAlignment = Alignment.Center) {
        val path = person.photo?.fullPath
        val placeholder: @Composable () -> Unit = { Icon(Icons.Outlined.Business, null, Modifier.size(SauceSizes.icon), tint = SauceTheme.colors.textSecondary) }
        if (path.isNullOrBlank()) placeholder()
        else SubcomposeAsyncImage(model = imageRequestCrossfade(path), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(),
            error = { placeholder() }, success = {
                SubcomposeAsyncImageContent(modifier = Modifier.fillMaxSize(), contentDescription = null)
            })
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun OrganizerContactContent(person: Organizer, onClickUrl: (String) -> Unit, onClickPhone: (String) -> Unit,
    modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            OrganizerAvatar(person)
            OrganizerIdentity(person, Modifier.weight(1f))
        }
        BaseDivider()
        person.phone?.takeIf { it.isNotBlank() }?.let { phone ->
            SauceActionRow(title = phone, subtitle = "Телефон", icon = Icons.Outlined.Phone, onClick = { onClickPhone(phone) })
        }
        val networks = person.socialNetworks.filter { !it.url.isNullOrBlank() }
        if (networks.isNotEmpty()) {
            Text("Социальные сети", Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
                verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                networks.forEach { social ->
                    val label = when (social.code) { "vk" -> "ВКонтакте"; "telegram" -> "Telegram"; else -> social.code.ifBlank { "Ссылка" } }
                    val color = when (social.code) { "vk" -> SportSouceColor.VkIcon; "telegram" -> SportSouceColor.TelegramIcon; else -> SauceTheme.colors.surfaceSubtle }
                    val icon = when (social.code) { "vk" -> IconVk; "telegram" -> IconTelegram; else -> Icons.AutoMirrored.Filled.OpenInNew }
                    Surface(onClick = { social.url?.let(onClickUrl) }, modifier = Modifier.size(SauceSizes.touchTarget),
                        shape = SauceShapes.field, color = color) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(icon, label, Modifier.size(SauceSizes.icon),
                                tint = if (social.code == "vk" || social.code == "telegram") Color.White else SauceTheme.colors.textSecondary)
                        }
                    }
                }
            }
        }
        if (!person.hasContacts()) Text("Контактная информация недоступна", Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textMuted)
    }
}
