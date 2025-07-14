package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
    Column(modifier = modifier) {
        Text(
            text = "Участники",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
        startOrderMembers.forEach { member ->
            Spacer(modifier = Modifier.height(8.dp))
            RegistrationsCardMemberInfo(
                member = member,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}

@Composable
private fun RegistrationsCardMemberInfo(
    modifier: Modifier = Modifier,
    member: StartOrderMember,
) {

    val fullName = member.surname + " " + member.name

    var isShowResult by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .shadow(3.dp, shape = Shapes.medium)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .clickable(
                enabled = member.results.isNotEmpty(),
                onClick = {
                    isShowResult = !isShowResult
                },
            )
    ) {
        if (member.results.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(bottomStart = 10.dp, topEnd = 10.dp)
                    )
                    .align(Alignment.TopEnd)
            ) {
                Text(
                    modifier = modifier.padding(6.dp),
                    text = "Есть результаты",
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontNunito.semiBoldBold(),
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
        Row {
            Spacer(modifier = Modifier.size(4.dp))
            RegistrationsMemberMiniature(fullName = fullName)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                RegistrationsCardInfoStatusInfoText(
                    label = "ФИО : ",
                    value = fullName
                )
                RegistrationsCardInfoStatusInfoText(
                    label = "Группа : ",
                    value = member.ageGroupName
                )
                RegistrationsCardInfoStatusInfoText(
                    label = "Дистанция : ",
                    value = member.distanceName
                )
                RegistrationsCardInfoStatusInfoText(
                    label = "Команда : ",
                    value = member.teamName
                )
                RegistrationsCardInfoStatusInfoText(
                    label = "Формат : ",
                    value = member.formatName
                )
                AnimatedVisibility(isShowResult && member.results.isNotEmpty()) {
                    MemberResultsCard(
                        result = member.results.first(),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(4.dp))
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