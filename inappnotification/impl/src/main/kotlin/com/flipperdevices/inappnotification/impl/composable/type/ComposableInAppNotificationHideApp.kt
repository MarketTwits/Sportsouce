package com.flipperdevices.inappnotification.impl.composable.type

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.markettwits.inappnotification.api.model.InAppNotification
import com.flipperdevices.inappnotification.impl.R

@Composable
fun ComposableInAppNotificationHideApp(
    notification: InAppNotification.HiddenApp,
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(top = 9.dp, bottom = 9.dp, end = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.hide_app_title),
            )
            Text(
                text = stringResource(R.string.hide_app_desc),
            )
        }
        Text(
            modifier = Modifier
                .clickable(onClick = {
                    notification.action.invoke()
                    onClickAction()
                })
                .padding(end = 12.dp),
            text = stringResource(R.string.hide_app_btn),
        )
    }
}
