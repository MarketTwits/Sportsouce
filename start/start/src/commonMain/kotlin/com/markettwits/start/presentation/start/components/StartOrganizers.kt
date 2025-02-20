package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.image.social_networks.IconTelegram
import com.markettwits.core_ui.items.image.social_networks.IconVk
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun StartOrganizers(
    modifier: Modifier = Modifier,
    organizer: List<com.markettwits.start_cloud.model.start.fields.Organizer>,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit
) {
    if (organizer.isNotEmpty()) {
        StartContentBasePanel(modifier = modifier, label = "Организаторы", openByDefault = false) {
            StartOrganizersContent(Modifier.padding(4.dp), organizer, onClickUrl, onClickPhone)
        }
    }
}

@Composable
private fun StartOrganizersContent(
    modifier: Modifier,
    organizer: List<com.markettwits.start_cloud.model.start.fields.Organizer>,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit
) {
    Column(modifier = modifier) {
        organizer.forEach {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.HomeRepairService,
                    contentDescription = "icon",
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(Modifier.padding(horizontal = 2.dp))
                Text(
                    text = it.name,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.regular(),
                    fontSize = 14.sp
                )
            }
            it.phone?.let {
                if (it.isNotEmpty())
                    Row(
                        modifier = modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "icon",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            modifier = modifier.clickable {
                                onClickPhone(it)
                            },
                            text = it,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = FontNunito.regular(),
                            fontSize = 14.sp
                        )
                    }
            }

            Row(modifier = modifier) {
                it.social_networks.forEach {
                    if (it.url != null) {
                        Box(
                            modifier = modifier
                                .clip(Shapes.medium)
                                .background(MaterialTheme.colorScheme.tertiary)
                                .size(30.dp)
                                .clickable {
                                    it.url?.let { url ->
                                        onClickUrl(url)
                                    }
                                }
                        ) {
                            val icon = when (it.code) {
                                "vk" -> IconVk
                                "telegram" -> IconTelegram
                                else -> IconTelegram
                            }
                            Icon(
                                modifier = Modifier
                                    .size(15.dp)
                                    .align(Alignment.Center),
                                imageVector = icon,
                                contentDescription = "icon",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}