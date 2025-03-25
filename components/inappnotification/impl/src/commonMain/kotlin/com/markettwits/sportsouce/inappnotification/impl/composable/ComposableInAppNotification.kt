package com.markettwits.sportsouce.inappnotification.impl.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import com.markettwits.sportsouce.inappnotification.impl.composable.type.ComposableInAppNotificationSelfUpdateReady

@Composable
fun ComposableInAppNotification(
    notification: InAppNotification,
    onNotificationHidden: () -> Unit,
    onNotificationOpen: (notification: InAppNotification) -> Unit,
    modifier: Modifier = Modifier
) {
    key(notification, onNotificationHidden, modifier) {
        ComposableInAppNotificationCard(
            notification,
            onNotificationHidden,
            onNotificationOpen,
            modifier
        )
    }
}

@Composable
@Suppress("LongMethod")
private fun ComposableInAppNotificationCard(
    notification: InAppNotification,
    onNotificationHidden: () -> Unit,
    onNotificationOpen: (notification: InAppNotification) -> Unit,
    modifier: Modifier = Modifier
) {
    var visibleState by remember { mutableStateOf(false) }

    var actionClicked by remember { mutableStateOf(false) }

    LaunchedEffect(notification) {
        visibleState = true
    }


    Card(
        modifier = Modifier
            .padding(all = 14.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(size = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        when (notification) {
            is InAppNotification.SelfUpdateReady -> {
                ComposableInAppNotificationSelfUpdateReady(notification) {
                    onNotificationOpen(notification)
                }
            }

            is InAppNotification.SelfUpdateError -> {

            }

            is InAppNotification.SelfUpdateStarted -> {

            }

            is InAppNotification.HiddenApp -> {

            }

            is InAppNotification.Error -> {

            }
        }
    }
}
