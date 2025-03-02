package com.markettwits.start.presentation.membres.list.compoents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.components.textField.BoundlessTextFieldBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.presentation.membres.list.component.StartMembersScreen

@Composable
internal fun StartSearchMember(
    modifier: Modifier = Modifier,
    component: StartMembersScreen,
) {
    val query by component.filterValue.subscribeAsState()


    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            onClick = {
                component.back()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "ArrowBack",
                tint = MaterialTheme.colorScheme.outline
            )
        }
        BoundlessTextFieldBase(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .padding(top = 5.dp),
            value = query,
            onValueChange = {
                component.handleTextFiled(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "Поиск участника (Фамилия Имя)",
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.bold(),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            },
        )
        IconButton(
            onClick = {
                component.openFilter()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Notes,
                contentDescription = "Notes",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}
