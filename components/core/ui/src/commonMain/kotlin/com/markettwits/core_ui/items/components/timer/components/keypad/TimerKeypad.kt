package com.markettwits.core_ui.items.components.timer.components.keypad

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.timer.domain.Keypad


@Composable
internal fun TimerKeypadScreen(
    onKeyClick: (Keypad) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val modifier =  Modifier
                .width(56.dp)
                .height(56.dp)
            CircularKey(
                modifier = modifier,
                key = Keypad.Key1,
                onClick = onKeyClick
            )

            CircularKey(
                modifier = modifier,
                key = Keypad.Key2,
                onClick = onKeyClick
            )

            CircularKey(
                modifier = modifier,
                key = Keypad.Key3,
                onClick = onKeyClick
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val modifier =  Modifier
                .width(56.dp)
                .height(56.dp)
            CircularKey(
                modifier = modifier,
                key = Keypad.Key4,
                onClick = onKeyClick
            )

            CircularKey(
                modifier = modifier,
                key = Keypad.Key5,
                onClick = onKeyClick
            )

            CircularKey(
                modifier = modifier,
                key = Keypad.Key6,
                onClick = onKeyClick
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val modifier =  Modifier
                .width(56.dp)
                .height(56.dp)
            CircularKey(
                modifier = modifier,
                key = Keypad.Key7,
                onClick = onKeyClick
            )

            CircularKey(
                modifier = modifier,
                key = Keypad.Key8,
                onClick = onKeyClick
            )

            CircularKey(
                modifier = modifier,
                key = Keypad.Key9,
                onClick = onKeyClick
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val modifier =  Modifier
                .width(56.dp)
                .height(56.dp)
            CircularKey(
                modifier = modifier,
                key = Keypad.Key00,
                onClick = onKeyClick
            )

            CircularKey(
                modifier = modifier,
                key = Keypad.Key0,
                onClick = onKeyClick
            )

            CircularKey(
                modifier = modifier,
                key = Keypad.KeyDelete,
                icon = Icons.AutoMirrored.Outlined.Backspace,
                textColor = MaterialTheme.colorScheme.secondary,
                onClick = onKeyClick
            )
        }
    }
}