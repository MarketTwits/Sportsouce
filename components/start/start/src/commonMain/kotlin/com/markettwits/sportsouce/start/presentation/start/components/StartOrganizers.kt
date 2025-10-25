package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.image.social_networks.IconTelegram
import com.markettwits.core_ui.items.image.social_networks.IconVk
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer

@Composable
internal fun StartOrganizers(
    modifier: Modifier = Modifier,
    organizer: List<Organizer>,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit
) {
    if (organizer.isNotEmpty()) {
        StartContentBasePanel(
            modifier = modifier,
            label = "Организаторы",
            icon = Icons.Default.Business
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                organizer.forEach { org ->
                    OrganizerCard(
                        organizer = org,
                        onClickUrl = onClickUrl,
                        onClickPhone = onClickPhone
                    )
                }
            }
        }
    }
}

@Composable
private fun OrganizerCard(
    organizer: Organizer,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = organizer.name,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontNunito.bold(),
                fontSize = 16.sp
            )

            organizer.phone?.let { phone ->
                if (phone.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(Shapes.small)
                            .clickable { onClickPhone(phone) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = phone,
                            color = MaterialTheme.colorScheme.secondary,
                            fontFamily = FontNunito.medium(),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            val availableSocialNetworks = organizer.socialNetworks.filter { it.url != null }
            if (availableSocialNetworks.isNotEmpty()) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Контакты:",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontNunito.medium(),
                        fontSize = 12.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableSocialNetworks.forEach { social ->
                            social.url?.let { url ->
                                Box(
                                    modifier = Modifier
                                        .clip(Shapes.medium)
                                        .background(MaterialTheme.colorScheme.secondary)
                                        .size(36.dp)
                                        .clickable { onClickUrl(url) }
                                ) {
                                    val icon = when (social.code) {
                                        "vk" -> IconVk
                                        "telegram" -> IconTelegram
                                        else -> IconTelegram
                                    }
                                    Icon(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .align(Alignment.Center),
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
