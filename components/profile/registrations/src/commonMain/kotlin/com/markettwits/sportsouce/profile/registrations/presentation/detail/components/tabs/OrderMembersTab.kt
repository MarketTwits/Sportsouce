package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderMember
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.MemberResultsCard

@Composable
fun OrderMembersTab(members: List<StartOrderMember>) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Все участники",
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primary,
                        Shapes.small
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${members.size}",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        members.forEach { member ->
            MemberCard(member = member)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun MemberCard(member: StartOrderMember) {
    val fullName = "${member.surname} ${member.name}"
    var isShowResult by remember { mutableStateOf(false) }
    val hasResults = member.results.isNotEmpty()

    Box(
        modifier = Modifier
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .then(
                if (hasResults) {
                    Modifier.clickable(
                        onClick = { isShowResult = !isShowResult }
                    )
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val initials = fullName.split(" ")
                    .take(2).joinToString("") { it.firstOrNull()?.toString() ?: "" }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                ) {
                    Text(
                        text = initials,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 15.sp,
                        fontFamily = FontNunito.semiBoldBold()
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = fullName,
                        fontSize = 15.sp,
                        fontFamily = FontNunito.bold(),
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Группа:",
                            fontSize = 12.sp,
                            fontFamily = FontNunito.semiBoldBold(),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Text(
                            text = member.ageGroupName,
                            fontSize = 12.sp,
                            fontFamily = FontNunito.medium(),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Дистанция:",
                            fontSize = 12.sp,
                            fontFamily = FontNunito.semiBoldBold(),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Text(
                            text = member.distanceName,
                            fontSize = 12.sp,
                            fontFamily = FontNunito.medium(),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (hasResults) {
                    Icon(
                        imageVector = if (isShowResult) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(Shapes.small)
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MemberInfoRow(label = "Команда", value = member.teamName)
                MemberInfoRow(label = "Формат", value = member.formatName)
                MemberInfoRow(label = "Пол", value = member.genderName)
            }

            AnimatedVisibility(
                visible = isShowResult && hasResults,
                enter = expandVertically(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(Shapes.small)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                            .padding(12.dp)
                    ) {
                        MemberResultsCard(
                            result = member.results.first()
                        )
                    }
                }
            }
        }

        if (hasResults) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 10.dp),
                color = MaterialTheme.colorScheme.secondary,
                shape = Shapes.small
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Результат",
                        fontSize = 11.sp,
                        fontFamily = FontNunito.semiBoldBold(),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun MemberInfoRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            fontSize = 14.sp,
            fontFamily = FontNunito.semiBoldBold(),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontFamily = FontNunito.medium(),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
