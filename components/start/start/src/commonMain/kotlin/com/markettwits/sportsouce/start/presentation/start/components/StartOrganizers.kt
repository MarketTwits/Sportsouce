package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.image.social_networks.IconTelegram
import com.markettwits.core_ui.items.image.social_networks.IconVk
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer

@Composable
internal fun StartOrganizers(
    modifier: Modifier = Modifier,
    organizer: List<Organizer>,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit,
) {
    if (organizer.isNotEmpty()) {
        var showContactDialog by remember { mutableStateOf(false) }
        var selectedOrganizer by remember { mutableStateOf<Organizer?>(null) }
        var fullImageUrl by rememberSaveable { mutableStateOf("") }

        val sortedOrganizers = remember(organizer) {
            organizer.sortedByDescending { it.isMain }
        }

        StartContentBasePanel(
            modifier = modifier,
            label = "Контактное лицо"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Organizer cards list
                sortedOrganizers.forEachIndexed { index, org ->
                    if (index > 0) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                    }
                    OrganizerCard(
                        organizer = org,
                        onContactsClick = {
                            selectedOrganizer = org
                            showContactDialog = true
                        },
                        onImageClick = {
                            fullImageUrl = it
                        }
                    )
                }
            }
        }

        if (fullImageUrl.isNotEmpty()) {
            FullImageScreen(
                image = fullImageUrl,
                onDismiss = { fullImageUrl = "" }
            )
        }

        // Contact Dialog
        if (showContactDialog && selectedOrganizer != null) {
            OrganizerContactDialog(
                organizer = selectedOrganizer!!,
                onDismiss = { showContactDialog = false },
                onClickUrl = onClickUrl,
                onClickPhone = onClickPhone
            )
        }
    }
}

@Composable
private fun OrganizerCard(
    organizer: Organizer,
    onContactsClick: () -> Unit,
    onImageClick: (String) -> Unit,
) {
    val hasPhone = !organizer.phone.isNullOrEmpty()
    val hasSocialNetworks = organizer.socialNetworks.any { it.url != null }
    val hasContacts = hasPhone || hasSocialNetworks

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            val photoPath = organizer.photo?.fullPath
            if (!photoPath.isNullOrEmpty()) {
                SubcomposeAsyncImage(
                    model = imageRequestCrossfade(model = photoPath),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = {
                        OrganizerPlaceholder(name = organizer.name)
                    },
                    success = {
                        SubcomposeAsyncImageContent(
                            modifier = Modifier.clickable(onClick = {
                                onImageClick(photoPath)
                            }),
                            contentDescription = null
                        )
                    }
                )
            } else {
                OrganizerPlaceholder(name = organizer.name)
            }
        }

        // Info content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Name and status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = organizer.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            // Organization label
            Text(
                text = "Организатор",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontFamily = FontNunito.regular(),
                fontSize = 12.sp
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (organizer.isMain) {
                Surface(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = Shapes.small
                ) {
                    Text(
                        text = "Главный",
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontFamily = FontNunito.bold(),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
            if (hasContacts) {
                TextButton(
                    onClick = onContactsClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Контакты",
                        fontFamily = FontNunito.semiBoldBold(),
                        fontSize = 13.sp
                    )
                }
            } else {
                Text(
                    text = "Контакты не указаны",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    fontFamily = FontNunito.regular(),
                    fontSize = 12.sp
                )
            }
        }

    }
}

@Composable
private fun OrganizerPlaceholder(name: String) {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = Icons.Rounded.AccountCircle,
        contentDescription = "Организатор $name",
        tint = MaterialTheme.colorScheme.onSecondary,
    )
}

@Composable
private fun OrganizerContactDialog(
    organizer: Organizer,
    onDismiss: () -> Unit,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        OnBackgroundCard(
            shape = Shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) { modifier ->
            Column(
                modifier = modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with avatar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        val photoPath = organizer.photo?.fullPath
                        if (!photoPath.isNullOrEmpty()) {
                            SubcomposeAsyncImage(
                                model = imageRequestCrossfade(model = photoPath),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                error = {
                                    OrganizerPlaceholder(name = organizer.name)
                                },
                                success = {
                                    SubcomposeAsyncImageContent(modifier = Modifier)
                                }
                            )
                        } else {
                            OrganizerPlaceholder(name = organizer.name)
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = organizer.name,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = FontNunito.bold(),
                                fontSize = 14.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Text(
                            text = if (organizer.isMain) "Главный организатор" else "Организатор",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            fontFamily = FontNunito.regular(),
                            fontSize = 13.sp
                        )
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                // Проверяем наличие контактов
                val hasPhone = !organizer.phone.isNullOrEmpty()
                val hasSocialNetworks = organizer.socialNetworks.any { it.url != null }
                val hasContacts = hasPhone || hasSocialNetworks

                if (hasContacts) {
                    // Phone
                    if (hasPhone) {
                        organizer.phone?.let { phone ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(Shapes.medium)
                                    .clickable { onClickPhone(phone) }
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape = CircleShape
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondary,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .size(20.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Телефон",
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                        fontFamily = FontNunito.regular(),
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = phone,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontFamily = FontNunito.semiBoldBold(),
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    // Social networks
                    if (hasSocialNetworks) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Социальные сети",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                fontFamily = FontNunito.regular(),
                                fontSize = 12.sp
                            )

                            val availableSocialNetworks = organizer.socialNetworks.filter { it.url != null }

                            availableSocialNetworks.chunked(4).forEach { rowSocials ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    rowSocials.forEach { social ->
                                        social.url?.let { url ->
                                            val iconColor = when (social.code) {
                                                "vk" -> SportSouceColor.VkIcon
                                                "telegram" -> SportSouceColor.TelegramIcon
                                                else -> SportSouceColor.TelegramIcon
                                            }
                                            Surface(
                                                modifier = Modifier.size(40.dp),
                                                onClick = { onClickUrl(url) },
                                                shape = Shapes.medium,
                                                color = iconColor
                                            ) {
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    val icon = when (social.code) {
                                                        "vk" -> IconVk
                                                        "telegram" -> IconTelegram
                                                        else -> IconTelegram
                                                    }
                                                    Icon(
                                                        modifier = Modifier.size(24.dp),
                                                        imageVector = icon,
                                                        contentDescription = null,
                                                        tint = androidx.compose.ui.graphics.Color.White
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Сообщение об отсутствии контактов
                    OnBackgroundCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) { cardModifier ->
                        Column(
                            modifier = cardModifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                                shape = CircleShape,
                                modifier = Modifier.size(64.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Business,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(20.dp).size(24.dp)
                                )
                            }

                            Text(
                                text = "Контактная информация недоступна",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                fontFamily = FontNunito.medium(),
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "У организатора не указаны контактные данные",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                fontFamily = FontNunito.regular(),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = "Закрыть",
                            fontFamily = FontNunito.semiBoldBold(),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
