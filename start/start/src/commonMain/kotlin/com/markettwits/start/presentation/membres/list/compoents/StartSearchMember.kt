package com.markettwits.start.presentation.membres.list.compoents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.components.textField.BoundlessTextFieldBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.presentation.membres.list.component.StartMembersScreen

@Composable
fun StartSearchMember(modifier: Modifier = Modifier, component: StartMembersScreen) {
    val comment by component.filterValue.subscribeAsState()
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BoundlessTextFieldBase(
            modifier = Modifier.weight(0.8f),
            value = comment,
            placeholder = {
                Text(
                    text = "Поиск участника",
                    fontSize = 14.sp,
                    fontFamily = FontNunito.regular(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            },
            onValueChange = {
                component.handleTextFiled(it)
            }
        )
        IconButton(
            modifier = modifier
                .weight(0.2f),
            onClick = { component.openFilter() }
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "filter",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }

    }
}
