package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.buttons.SauceClickTarget
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.extensions.formatRussianPhone
import com.markettwits.core_ui.items.image.dominantImageColor
import com.markettwits.core_ui.items.image.enableImageColorSampling
import com.markettwits.core_ui.items.image.social_networks.*
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfile

@Composable
internal fun ProfileInfoTopBar(
    modifier: Modifier = Modifier,
    imageUrl: String,
    userName: String,
    userPhoneNumber: String,
    socialNetwork: UserProfile.SocialNetwork,
    onClickImage: (String) -> Unit,
    onClickEditProfile: () -> Unit,
    onSocialNetworkClick: (String) -> Unit,
    onAddSocialNetwork: () -> Unit,
    onAvatarColor: ((Color) -> Unit)? = null,
    headerTint: () -> Color = { Color.Unspecified },
) {
    val colors = SauceTheme.colors
    val hasSocialLinks = with(socialNetwork) {
        instagram.isNotBlank() || telegram.isNotBlank() || vk.isNotBlank() || whatsapp.isNotBlank()
    }
    OnBackgroundCard(modifier, colors = CardDefaults.cardColors(containerColor = Color.Transparent, contentColor = colors.textPrimary)) {
        Column(
            Modifier.drawBehind {
                val backdrop = headerTint()
                drawRect(if (backdrop == Color.Unspecified) colors.surface else lerp(colors.surface, backdrop, .45f))
            }.padding(SauceSpacing.large),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
        ) {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val stacked = maxWidth < 220.dp * LocalDensity.current.fontScale
                SauceClickTarget(modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Редактировать профиль" },
                    onClickLabel = "Редактировать профиль", onClick = onClickEditProfile) {
                    val identityModifier = Modifier.fillMaxWidth()
                    if (stacked) {
                        Column(identityModifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                ProfileAvatar(imageUrl, onClickImage, onAvatarColor)
                                Spacer(Modifier.weight(1f))
                                Icon(Icons.Default.ChevronRight, null, Modifier.size(24.dp), tint = colors.textMuted)
                            }
                            ProfileIdentity(userName, userPhoneNumber)
                        }
                    } else Row(identityModifier, verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ProfileAvatar(imageUrl, onClickImage, onAvatarColor)
                        ProfileIdentity(userName, userPhoneNumber, Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, null, Modifier.size(24.dp), tint = colors.textMuted)
                    }
                }
            }
            if (hasSocialLinks) FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (socialNetwork.instagram.isNotBlank()) ProfileSocialButton(InstagramIcon, "Instagram", SportSouceColor.InstagramIcon) { onSocialNetworkClick(socialNetwork.instagram) }
                if (socialNetwork.telegram.isNotBlank()) ProfileSocialButton(IconTelegram, "Telegram", SportSouceColor.TelegramIcon) { onSocialNetworkClick(socialNetwork.telegram) }
                if (socialNetwork.vk.isNotBlank()) ProfileSocialButton(IconVk, "ВКонтакте", SportSouceColor.VkIcon) { onSocialNetworkClick(socialNetwork.vk) }
                if (socialNetwork.whatsapp.isNotBlank()) ProfileSocialButton(WhatsappIcon, "WhatsApp", SportSouceColor.WhatsappIcon) {
                    onSocialNetworkClick("https://wa.me/${socialNetwork.whatsapp.replace("+", "").replace(" ", "")}")
                }
                ProfileSocialButton(Icons.Default.Add, "Изменить социальные сети", colors.textSecondary, onAddSocialNetwork)
            }
        }
    }
}

@Composable
private fun ProfileSocialButton(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    val tint = if (LocalDarkOrLightTheme.current) lerp(color, Color.White, 0.25f) else color
    Surface(onClick = onClick, modifier = Modifier.size(SauceSizes.touchTarget), shape = SauceShapes.field,
        color = SauceTheme.colors.surfaceSubtle) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(icon, label, Modifier.size(SauceSizes.icon), tint = tint)
        }
    }
}

@Composable
private fun ProfileIdentity(name: String, phoneNumber: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(name.ifBlank { "Мой профиль" }, style = MaterialTheme.typography.titleMedium,
            color = SauceTheme.colors.textPrimary, maxLines = 2, overflow = TextOverflow.Ellipsis)
        val phone = phoneNumber.formatRussianPhone()
        if (phone.isNotBlank()) Text(phone, style = MaterialTheme.typography.bodyMedium,
            color = SauceTheme.colors.textSecondary)
    }
}

@Composable
private fun ProfileAvatar(imageUrl: String, onClick: (String) -> Unit, onAvatarColor: ((Color) -> Unit)?) {
    val context = LocalPlatformContext.current
    val sampleColor = onAvatarColor != null
    val model = remember(context, imageUrl, sampleColor) {
        ImageRequest.Builder(context).data(imageUrl.takeIf { it.isNotBlank() }).crossfade(true)
            .apply { if (sampleColor) enableImageColorSampling() }
            .build()
    }
    SubcomposeAsyncImage(model = model, contentDescription = "Фото профиля",
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(64.dp).clip(CircleShape)
            .then(if (imageUrl.isNotBlank()) Modifier.clickable(role = Role.Button,
                onClickLabel = "Открыть фото профиля") { onClick(imageUrl) } else Modifier),
        loading = { SauceSkeleton(Modifier.fillMaxSize(), CircleShape) },
        success = { state ->
            if (onAvatarColor != null && state.result.request.data == imageUrl) {
                val density = LocalDensity.current
                val direction = LocalLayoutDirection.current
                LaunchedEffect(state.painter, imageUrl) {
                    onAvatarColor(state.painter.dominantImageColor(density, direction, sampleHeight = 32))
                }
            }
            SubcomposeAsyncImageContent()
        },
        error = { Surface(color = SauceTheme.colors.surfaceSubtle) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, Modifier.size(32.dp), tint = SauceTheme.colors.textMuted)
            }
        } })
}
