package com.flipperdevices.inappnotification.impl.composable

import android.util.Log
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
import com.flipperdevices.inappnotification.impl.composable.type.ComposableInAppNotificationSelfUpdateReady
import com.markettwits.inappnotification.api.model.InAppNotification

const val VISIBLE_ANIMATION_MS = 1000

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

    val onClickAction = remember(onNotificationHidden) {
        {
            visibleState = false
            actionClicked = true
            onNotificationHidden()
        }
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
                Log.e("mt05", "#ComposableInAppNotificationCard SelfUpdateReady")
                ComposableInAppNotificationSelfUpdateReady(notification) {
                    onNotificationOpen(notification)
                }
            }

            is InAppNotification.SelfUpdateError -> {
                Log.e("mt05", "#ComposableInAppNotificationCard SelfUpdateError")
                // ComposableInAppNotificationSelfUpdateError()
            }

            is InAppNotification.SelfUpdateStarted -> {
                Log.e("mt05", "#ComposableInAppNotificationCard SelfUpdateError")
                //ComposableInAppNotificationSelfUpdateStarted()
            }

            is InAppNotification.HiddenApp -> {
                Log.e("mt05", "#ComposableInAppNotificationCard HiddenApp")
//                ComposableInAppNotificationHideApp(
//                    notification = notification,
//                    onClickAction = onClickAction
//                )
            }

            is InAppNotification.Error -> {
                Log.e("mt05", "#ComposableInAppNotificationCard Error")
//                ComposableInAppNotificationError(
//                    notification,
//                    onClickAction
//                )
            }
        }
        //   }
    }
//    DisposableEffect(notification) {
//        val handler = Handler(Looper.getMainLooper())
//        val fadeOutRunnable = {
//            visibleState = false
//        }
//        val hiddenRunnable = {
//            if (!actionClicked) {
//                onNotificationHidden()
//            }
//        }
//
//        handler.postDelayed(
//            fadeOutRunnable,
//            max(0L, notification.durationMs - VISIBLE_ANIMATION_MS)
//        )
//        handler.postDelayed(
//            hiddenRunnable,
//            notification.durationMs
//        )
//
//        onDispose {
//            handler.removeCallbacks(fadeOutRunnable)
//            handler.removeCallbacks(hiddenRunnable)
//        }
//    }
}
