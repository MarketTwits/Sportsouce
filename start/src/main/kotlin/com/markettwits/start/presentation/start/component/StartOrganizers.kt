package com.markettwits.start.presentation.start.component

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.cloud.model.start.Organizer
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.components.openWebPage
import com.markettwits.core_ui.image.icons.IconTelegram
import com.markettwits.core_ui.image.icons.IconVk
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.common.Animation

@Composable
fun StartOrganizers(modifier: Modifier = Modifier, organizer: List<Organizer>) {
    var panelState by rememberSaveable{
        mutableStateOf(true)
    }
    if (organizer.isNotEmpty()) {
        HorizontalDivider()
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    panelState = !panelState
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Организаторы",
                color = SportSouceColor.SportSouceBlue,
                fontFamily = FontNunito.bold,
                fontSize = 16.sp
            )
            Icon(
                imageVector = if (!panelState) Icons.AutoMirrored.Filled.KeyboardArrowRight else Icons.Default.KeyboardArrowDown,
                contentDescription = "",
                tint = SportSouceColor.SportSouceBlue
            )
        }
            AnimatedVisibility(
                visible = panelState,
                enter = Animation.enterAnimation(Animation.DEFAULT_TOP_BAR_ANIMATION_DURATION),
                exit = Animation.exitAnimation(Animation.DEFAULT_TOP_BAR_ANIMATION_DURATION),
            ) {
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
                    tint = SportSouceColor.SportSouceLighBlue
                )
                Text(
                    text = it.name,
                    color = SportSouceColor.SportSouceBlue,
                    fontFamily = FontNunito.regular,
                    fontSize = 14.sp
                )
            }
            if (it.phone.isNotEmpty()){
                val context = LocalContext.current
                Row(modifier = modifier) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "icon",
                        tint = SportSouceColor.SportSouceLighBlue
                    )
                    Text(
                        modifier = modifier.clickable {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:${it.phone}")
                            context.startActivity(intent)
                        },
                        text = it.phone,
                        color = SportSouceColor.SportSouceBlue,
                        fontFamily = FontNunito.regular,
                        fontSize = 14.sp
                    )
                }
            }
            val context = LocalContext.current
            Row(modifier = modifier) {

                it.social_networks?.forEach {
                    if (it.url != null){
                        Box(
                            modifier = modifier
                                .clip(Shapes.medium)
                                .background(SportSouceColor.SportSouceLighBlue)
                                .size(30.dp)
                                .clickable {
                                    openWebPage(it.url ?: "", context)
                                }
                        ) {
                            val icon = when(it.code){
                                "vk" -> IconVk
                                "telegram" -> IconTelegram
                                else -> IconTelegram
                            }
                            Icon(
                                modifier = Modifier.size(15.dp).align(Alignment.Center),
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