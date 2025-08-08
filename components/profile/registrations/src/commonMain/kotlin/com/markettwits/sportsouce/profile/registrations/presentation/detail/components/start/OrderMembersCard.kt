package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderMember

@Composable
internal fun OrderMembersCard(
    modifier: Modifier = Modifier,
    startOrderMembers: List<StartOrderMember>
) {
    Column(
        modifier = modifier
            .clip(Shapes.large)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Участники",
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                        Shapes.small
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${startOrderMembers.size}",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        startOrderMembers.forEachIndexed { index, member ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = index * 100
                    )
                ) + expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            ) {
                RegistrationsCardMemberInfo(
                    member = member,
                    modifier = Modifier.padding(bottom = if (index < startOrderMembers.lastIndex) 12.dp else 0.dp)
                )
            }
        }
    }
}

@Composable
private fun RegistrationsCardMemberInfo(
    modifier: Modifier = Modifier,
    member: StartOrderMember,
) {
    val fullName = member.surname + " " + member.name
    var isShowResult by remember { mutableStateOf(false) }
    val hasResults = member.results.isNotEmpty()

    Box(
        modifier = modifier
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.outlineVariant)
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
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RegistrationsMemberMiniature(fullName = fullName)
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = fullName,
                        fontSize = 16.sp,
                        fontFamily = FontNunito.bold(),
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${member.ageGroupName} • ${member.distanceName}",
                        fontSize = 14.sp,
                        fontFamily = FontNunito.medium(),
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (hasResults) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Icon(
                            imageVector = if (isShowResult) Icons.Default.KeyboardArrowUp
                            else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                RegistrationsCardInfoStatusInfoText(
                    label = "Команда: ",
                    value = member.teamName
                )
                RegistrationsCardInfoStatusInfoText(
                    label = "Формат: ",
                    value = member.formatName
                )
                RegistrationsCardInfoStatusInfoText(
                    label = "Пол: ",
                    value = member.genderName
                )
            }

            AnimatedVisibility(
                visible = isShowResult && hasResults,
                enter = expandVertically(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    MemberResultsCard(
                        result = member.results.first()
                    )
                }
            }
        }

        if (hasResults) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(bottomStart = 8.dp, topEnd = 10.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Результат",
                    fontSize = 10.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }
}

@Composable
private fun RegistrationsMemberMiniature(
    modifier: Modifier = Modifier,
    fullName: String
) {
    val initials = fullName.split(" ")
        .take(2).joinToString("") { it.firstOrNull()?.toString() ?: "" }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(4.dp)
            .size(34.dp)
            .background(
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
    ) {
        Text(
            text = initials,
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 12.sp,
            fontFamily = FontNunito.semiBoldBold()
        )
    }
}

@Composable
internal fun RegistrationsCardInfoStatusInfoText(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    Text(
        modifier = modifier.padding(2.dp),
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
        fontSize = 14.sp,
        overflow = TextOverflow.Ellipsis,
        fontFamily = FontNunito.medium(),
        color = MaterialTheme.colorScheme.outline
    )
}