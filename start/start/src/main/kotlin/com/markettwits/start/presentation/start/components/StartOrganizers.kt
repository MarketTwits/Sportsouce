package com.markettwits.start.presentation.start.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.cloud.model.start.Organizer
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.image.IconTelegram
import com.markettwits.core_ui.items.image.IconVk
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.presentation.common.StartContentBasePanel

@Composable
fun StartOrganizers(modifier: Modifier = Modifier, organizer: List<Organizer>) {
    if (organizer.isNotEmpty()) {
        StartContentBasePanel(modifier = modifier, label = "Организаторы", openByDefault = false) {
            StartOrganizersContent(modifier, organizer)
        }
    }
}

@Composable
private fun StartOrganizersContent(modifier: Modifier, organizer: List<Organizer>) {
    organizer.forEach {
        Column(modifier = modifier) {
            Row(modifier = modifier) {
                Icon(
                    imageVector = Icons.Default.HomeRepairService,
                    contentDescription = "icon",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = it.name,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.regular(),
                    fontSize = 14.sp
                )
            }
            if (it.phone.isNotEmpty()) {
                val context = LocalContext.current
                Row(modifier = modifier) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "icon",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        modifier = modifier.clickable {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:${it.phone}")
                            context.startActivity(intent)
                        },
                        text = it.phone,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = FontNunito.regular(),
                        fontSize = 14.sp
                    )
                }
            }
            Row(modifier = modifier) {
                it.social_networks?.forEach {
                    if (it.url != null) {
                        Box(
                            modifier = modifier
                                .clip(Shapes.medium)
                                .background(MaterialTheme.colorScheme.secondary)
                                .size(30.dp)
                                .clickable {
                                    //TODO open web page
                                    // openWebPage(it.url ?: "", context)
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