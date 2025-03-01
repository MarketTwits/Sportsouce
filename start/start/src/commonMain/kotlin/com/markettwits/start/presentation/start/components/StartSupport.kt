package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.base_extensions.noRippleClickable
import com.markettwits.core_ui.items.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start_support.presentation.component.StartSupportComponent
import com.markettwits.start_support.presentation.components.StartSupportScreen

@Composable
internal fun StartSupport(
    modifier: Modifier,
    component: StartSupportComponent,
    eventContent: (EventContent) -> Unit
) {
    var isShowSupportDialog by rememberSaveable {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable {
                isShowSupportDialog = true
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Поддержать проект",
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 18.sp
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.outline
        )
    }
    Spacer(modifier = Modifier.padding(5.dp))

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