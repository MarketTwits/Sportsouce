package com.flipperdevices.inappnotification.impl.composable.type

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.inappnotification.api.model.InAppNotification

@Composable
internal fun ComposableInAppNotificationSelfUpdateReady(
    notification: InAppNotification.SelfUpdateReady,
    onClickAction: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SportSouceColor.SportSouceRegistryOpenGreen.copy(0.1f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Для установки доступна новая версия",
                color = SportSouceColor.SportSouceRegistryOpenGreen,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                textAlign = TextAlign.Center
            )
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                shape = Shapes.medium,
                onClick = {
                    onClickAction()
                },
                colors = ButtonDefaults.buttonColors(containerColor = SportSouceColor.SportSouceRegistryOpenGreen)
            ) {
                Text(
                    text = "Обновить приложение",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.bold()
                )
            }
        }
    }
}

