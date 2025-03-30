package com.markettwits.sportsouce.profile.members.member_common.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember

@Composable
fun MemberItemCard(
    modifier: Modifier = Modifier,
    full: Boolean = false,
    item: ProfileMember,
    onClick: (ProfileMember) -> Unit
) {
    OnBackgroundCard(
        modifier = modifier,
        onClick = {
            onClick(item)
        }
    ) {
        MemberItemCardContentSimple(
            full = full,
            item = item
        )
    }
}

@Composable
private fun MemberItemCardContentSimple(
    modifier: Modifier = Modifier,
    full: Boolean = false,
    item: ProfileMember
) {
    Row(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            MemberItemInfo(full, item)
        }
        if (!full) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun ColumnScope.MemberItemInfo(full: Boolean = false, item: ProfileMember) {
    Text(
        text = "${item.surname} ${item.name}",
        fontSize = 16.sp,
        fontFamily = FontNunito.bold(),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.tertiary
    )
    RegistrationsCardInfoStatusInfoText(
        label = "Почта: ",
        value = item.email
    )
    RegistrationsCardInfoStatusInfoText(
        label = "Телефон: ",
        value = item.phone
    )
    RegistrationsCardInfoStatusInfoText(
        label = "Тип: ",
        value = item.type
    )
    if (full) {
        RegistrationsCardInfoStatusInfoText(
            label = "Ребенок: ",
            value = if (item.child) "Да" else "Нет"
        )
        RegistrationsCardInfoStatusInfoText(
            label = "Пол: ",
            value = item.gender
        )
        RegistrationsCardInfoStatusInfoText(
            label = "Дата рождения: ",
            value = item.birthday
        )
        RegistrationsCardInfoStatusInfoText(
            label = "Команда: ",
            value = item.team
        )
    }
}

@Composable
private fun RegistrationsCardInfoStatusInfoText(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontFamily = FontNunito.bold())) {
                append(label)
            }
            withStyle(style = SpanStyle(fontFamily = FontNunito.medium())) {
                append(value)
            }
        },
        maxLines = 1,
        minLines = 1,
        fontSize = 12.sp,
        overflow = TextOverflow.Ellipsis,
        fontFamily = FontNunito.medium(),
        color = MaterialTheme.colorScheme.outline
    )
}