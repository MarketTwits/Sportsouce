package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AirplaneTicket
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.screenWidthDp
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun ResultCard(
    modifier: Modifier = Modifier,
    result: MemberResult,
    onClickMemberResult: (MemberResult) -> Unit,
) {
    val windowSizeClass = calculateWindowSizeClass()
    val windowWidth = windowSizeClass.screenWidthDp.value.dp
    if (windowWidth < 700.dp)
        CompactResultCard(
            result = result,
            onClickMemberResult = onClickMemberResult,
            modifier = modifier.fillMaxWidth()
        )
    else
        ExpandedResultCard(
            result = result,
            onClickMemberResult = onClickMemberResult,
            modifier = modifier.fillMaxWidth()
        )
}

@Composable
private fun CompactResultCard(
    result: MemberResult,
    onClickMemberResult: (MemberResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnBackgroundCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        onClick = { onClickMemberResult(result) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlaceAvatar(
                    place = result.place,
                    name = result.name,
                    isExpanded = false
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        text = result.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontFamily = FontNunito.bold(),
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.width(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(16.dp),
                            tint = MaterialTheme.colorScheme.tertiary,
                            imageVector = Icons.AutoMirrored.Filled.AirplaneTicket,
                            contentDescription = result.bodyNumber,
                        )
                        Text(
                            text = "№${result.bodyNumber}",
                            color = MaterialTheme.colorScheme.outline,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontFamily = FontNunito.medium(),
                            fontSize = 12.sp,
                        )
                        Spacer(Modifier.width(12.dp))
                        Icon(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(16.dp),
                            tint = MaterialTheme.colorScheme.tertiary,
                            imageVector = Icons.Default.Group,
                            contentDescription = result.bodyNumber,
                        )
                        Text(
                            text = result.group,
                            color = MaterialTheme.colorScheme.outline,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontFamily = FontNunito.medium(),
                            fontSize = 12.sp,
                        )
                        Spacer(Modifier.width(12.dp))
                        Icon(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(14.dp),
                            tint = MaterialTheme.colorScheme.tertiary,
                            imageVector = Icons.Default.Groups,
                            contentDescription = result.bodyNumber,
                        )
                        Text(
                            text = result.team,
                            color = MaterialTheme.colorScheme.outline,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontFamily = FontNunito.medium(),
                            fontSize = 12.sp,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompactResultItem(
                    modifier = Modifier.weight(1f),
                    label = "Место",
                    value = result.place.toString(),
                )

                CompactResultItem(
                    modifier = Modifier.weight(1f),
                    label = "Результат",
                    value = result.result,
                    isHighlighted = true
                )

                CompactResultItem(
                    modifier = Modifier.weight(1f),
                    label = "Отставание",
                    value = result.shift
                )
            }
        }
    }
}

@Composable
private fun ExpandedResultCard(
    result: MemberResult,
    onClickMemberResult: (MemberResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnBackgroundCard(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = { onClickMemberResult(result) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ExpandedResultItem(
                label = "Место",
                value = result.place.toString(),
                isHighlighted = true
            )

            Spacer(modifier = Modifier.width(12.dp))

            PlaceAvatar(
                place = result.place,
                name = result.name,
                isExpanded = true
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = result.name.formatNames(),
                    fontSize = 14.sp,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))

                CategoryChip(category = result.group)

            }

            Row(
                modifier = Modifier
                    .weight(3F)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                ExpandedResultItem(
                    modifier = Modifier.weight(1f),
                    label = "Номер",
                    value = "№ ${result.bodyNumber}",
                )

                ExpandedResultItem(
                    modifier = Modifier.weight(1f),
                    label = "Команда",
                    value = result.team,
                )

                ExpandedResultItem(
                    modifier = Modifier.weight(1f),
                    label = "Дистанция",
                    value = result.distance
                )

                ExpandedResultItem(
                    modifier = Modifier.weight(1f),
                    label = "Результат",
                    value = result.result,
                    isHighlighted = true
                )

                ExpandedResultItem(
                    modifier = Modifier.weight(1f),
                    label = "Отставание",
                    value = result.shift,
                )
            }
        }
    }
}

@Composable
private fun PlaceAvatar(
    modifier: Modifier = Modifier,
    place: Int,
    name: String,
    isExpanded: Boolean,
) {
    Box(
        modifier = modifier.size(44.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.outline.copy(0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isExpanded) name.toAbbreviation() else place.toString(),
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
private fun CategoryChip(category: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = category,
            fontSize = 12.sp,
            fontFamily = FontNunito.medium(),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun CompactResultItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isHighlighted: Boolean = false,
) {
    if (value.isNotBlank()) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontFamily = if (isHighlighted)
                    FontNunito.semiBoldBold()
                else
                    FontNunito.medium(),
                color = if (isHighlighted) MaterialTheme.colorScheme.secondary
                else
                    MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = label,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.outline,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ExpandedResultItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isHighlighted: Boolean = false,
) {
    if (value.isNotBlank()) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = label,
                fontFamily = FontNunito.regular(),
                color = MaterialTheme.colorScheme.outline,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                fontFamily = if (isHighlighted)
                    FontNunito.semiBoldBold()
                else
                    FontNunito.regular(),
                color = if (isHighlighted) MaterialTheme.colorScheme.secondary
                else
                    MaterialTheme.colorScheme.onBackground,
                fontSize = if (isHighlighted) 16.sp else 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun String.formatNames(): String {
    val names = split("\\").map { it.trim() }
    return names.joinToString("\n")
}

private fun String.toAbbreviation(maxLetters: Int = 3): String {
    val words = this
        .split("\\", " ")
        .filter { it.isNotBlank() }

    return words
        .take(maxLetters)
        .map { it.first().uppercaseChar() }
        .joinToString("")
}
