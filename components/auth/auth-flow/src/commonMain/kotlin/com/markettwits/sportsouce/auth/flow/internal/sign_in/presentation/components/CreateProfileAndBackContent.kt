package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.auth.flow.internal.common.ConsumeRowContent

@Composable
internal fun CreateProfileAndBackContent(
    modifier: Modifier = Modifier,
    onClickRegistry: () -> Unit,
    onClickConsume: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OrRowContent(modifier = Modifier.padding(10.dp))
        ButtonContentBase(
            modifier = Modifier.padding(10.dp),
            title = "Создать профиль",
            onClick = onClickRegistry,
            borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
        )
        ConsumeRowContent(
            modifier = Modifier.padding(10.dp),
            onClickConsume = onClickConsume,
            title = "Пропустить"
        )
    }
}

@Composable
private fun OrRowContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth(),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outline
        )
        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxWidth(),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outline
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp),
                text = "или",
                fontFamily = FontNunito.medium(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

