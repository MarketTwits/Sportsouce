package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.support.presentation.component.StartSupportComponent
import com.markettwits.sportsouce.start.support.presentation.components.StartSupportScreen

@Composable
internal fun StartSupport(
    modifier: Modifier,
    component: StartSupportComponent,
    eventContent: (EventContent) -> Unit
) {
    var isShowSupportDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .noRippleClickable {
                isShowSupportDialog = true
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Поддержать проект",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontNunito.bold(),
                    fontSize = 18.sp
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    if (isShowSupportDialog) {
        DefaultModalBottomSheet(
            onDismissRequest = { isShowSupportDialog = false },
        ) {
            StartSupportScreen(
                modifier = modifier,
                component = component,
                event = eventContent,
                onClickDismiss = {
                    isShowSupportDialog = false
                }
            )
        }
    }
}
