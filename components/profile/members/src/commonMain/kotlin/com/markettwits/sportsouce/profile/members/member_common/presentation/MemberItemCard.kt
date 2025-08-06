package com.markettwits.sportsouce.profile.members.member_common.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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
private fun MemberAvatar(
    name: String,
    surname: String,
    modifier: Modifier = Modifier,
) {
    val initials = "${name.firstOrNull()?.uppercase() ?: ""}${surname.firstOrNull()?.uppercase() ?: ""}"

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            fontFamily = FontNunito.bold(),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ContactInfoRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontNunito.medium(),
            color = MaterialTheme.colorScheme.outline,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun InfoText(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontFamily = FontNunito.bold(), color = MaterialTheme.colorScheme.outline)) {
                append("$label: ")
            }
            withStyle(style = SpanStyle(fontFamily = FontNunito.medium(), color = MaterialTheme.colorScheme.outline)) {
                append(value)
            }
        },
        style = MaterialTheme.typography.bodySmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun MemberItemCardContentSimple(
    modifier: Modifier = Modifier,
    full: Boolean = false,
    item: ProfileMember
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar with initials
        MemberAvatar(
            name = item.name,
            surname = item.surname
        )

        // Member info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MemberItemInfo(full, item)
        }

        // Chevron icon for navigation
        if (!full) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Подробнее",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ColumnScope.MemberItemInfo(full: Boolean = false, item: ProfileMember) {
    // Name and surname
    Text(
        text = "${item.surname} ${item.name}",
        style = MaterialTheme.typography.titleMedium,
        fontFamily = FontNunito.bold(),
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(4.dp))

    if (item.type.isNotEmpty()) {
        ContactInfoRow(
            icon = Icons.Default.Person,
            text = item.type
        )
    }

    // Contact information with icons
    if (item.email.isNotEmpty()) {
        ContactInfoRow(
            icon = Icons.Default.Email,
            text = item.email
        )
    }

    if (item.phone.isNotEmpty()) {
        ContactInfoRow(
            icon = Icons.Default.Phone,
            text = item.phone
        )
    }
    
    if (full) {
        Spacer(modifier = Modifier.height(8.dp))

        if (item.gender.isNotEmpty()) {
            InfoText(label = "Пол", value = item.gender)
        }
        if (item.birthday.isNotEmpty()) {
            InfoText(label = "Дата рождения", value = item.birthday)
        }
        if (item.team.isNotEmpty()) {
            InfoText(label = "Команда", value = item.team)
        }
        InfoText(label = "Ребенок", value = if (item.child) "Да" else "Нет")
    }
}
