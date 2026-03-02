package com.markettwits.sportsouce.start.presentation.membres.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AirplaneTicket
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.screenWidthDp
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun StartMemberCard(
    item: StartMembersUi,
    modifier: Modifier = Modifier,
) {
    val windowSizeClass = calculateWindowSizeClass()
    val windowWidth = windowSizeClass.screenWidthDp.value.dp

    if (windowWidth < 700.dp) {
        CompactMemberCard(
            item = item,
            modifier = modifier.fillMaxWidth()
        )
    } else {
        ExpandedMemberCard(
            item = item,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CompactMemberCard(
    item: StartMembersUi,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }

    OnBackgroundCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(12.dp),
        onClick = { isExpanded = !isExpanded }
    ) { cardModifier ->
        Column(
            modifier = cardModifier.padding(12.dp)
        ) {
            when (item) {
                is StartMembersUi.Single -> {
                    CompactSingleMemberContent(
                        item = item,
                        isExpanded = isExpanded
                    )
                }

                is StartMembersUi.Team -> {
                    CompactTeamMemberContent(
                        item = item,
                        isExpanded = isExpanded
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandedMemberCard(
    item: StartMembersUi,
    modifier: Modifier = Modifier,
) {
    OnBackgroundCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(16.dp),
    ) { cardModifier ->
        Column(
            modifier = cardModifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            when (item) {
                is StartMembersUi.Single -> {
                    ExpandedSingleMemberContent(
                        item = item,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is StartMembersUi.Team -> {
                    ExpandedTeamMemberContent(
                        item = item,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactSingleMemberContent(
    item: StartMembersUi.Single,
    isExpanded: Boolean,
) {
    // Participant name
    Text(
        text = "${item.name} ${item.surname}",
        style = MaterialTheme.typography.titleSmall.copy(
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onBackground
        ),
        fontSize = 14.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

    // Always show distance in compact view
    Spacer(modifier = Modifier.height(4.dp))
    if (!isExpanded) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(12.dp),
                tint = MaterialTheme.colorScheme.tertiary,
                imageVector = Icons.AutoMirrored.Filled.AirplaneTicket,
                contentDescription = null
            )
            Text(
                text = item.distance,
                fontSize = 12.sp,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }

    if (isExpanded) {
        Spacer(modifier = Modifier.height(8.dp))
        MemberMeta(distance = item.distance, team = item.team, group = item.group, city = item.city)
    }
}

@Composable
private fun CompactTeamMemberContent(
    item: StartMembersUi.Team,
    isExpanded: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.team.ifEmpty { "Команда" },
                style = MaterialTheme.typography.titleSmall.copy(
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.onBackground
                ),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            item.members.forEach { m ->
                Text(
                    modifier = Modifier.padding(vertical = 2.dp),
                    text = "${m.name} ${m.surname}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontNunito.medium(),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(12.dp),
                tint = MaterialTheme.colorScheme.tertiary,
                imageVector = Icons.AutoMirrored.Filled.AirplaneTicket,
                contentDescription = null
            )
            Text(
                text = item.distance,
                fontSize = 12.sp,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }

    AnimatedVisibility(visible = isExpanded) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            MemberMeta(
                distance = item.distance,
                team = item.team,
                group = item.group,
                city = item.city
            )
        }
    }
}

@Composable
private fun ExpandedSingleMemberContent(
    item: StartMembersUi.Single,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Name and info section
        Column(
            modifier = Modifier.weight(2f)
        ) {
            Text(
                text = "${item.name} ${item.surname}",
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(14.dp),
                    tint = MaterialTheme.colorScheme.tertiary,
                    imageVector = Icons.Default.Group,
                    contentDescription = null
                )
                Text(
                    text = item.group,
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.medium(),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Details section
        Row(
            modifier = Modifier.weight(3f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ExpandedResultItem(
                modifier = Modifier.weight(1f),
                label = "Дистанция",
                value = item.distance
            )
            ExpandedResultItem(
                modifier = Modifier.weight(1f),
                label = "Команда",
                value = item.team.ifEmpty { "-" }
            )
            ExpandedResultItem(
                modifier = Modifier.weight(1f),
                label = "Город",
                value = item.city.ifEmpty { "-" }
            )
        }
    }
}

@Composable
private fun ExpandedTeamMemberContent(
    item: StartMembersUi.Team,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Team name and info section
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Text(
                    text = item.team.ifEmpty { "Команда" },
                    fontSize = 14.sp,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(14.dp),
                        tint = MaterialTheme.colorScheme.tertiary,
                        imageVector = Icons.Default.Groups,
                        contentDescription = null
                    )
                    Text(
                        text = "${item.members.size} участн.",
                        color = MaterialTheme.colorScheme.outline,
                        fontFamily = FontNunito.medium(),
                        fontSize = 12.sp
                    )
                }
            }

            // Details section
            Row(
                modifier = Modifier.weight(3f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ExpandedResultItem(
                    modifier = Modifier.weight(1f),
                    label = "Дистанция",
                    value = item.distance
                )
                ExpandedResultItem(
                    modifier = Modifier.weight(1f),
                    label = "Группа",
                    value = item.group.ifEmpty { "-" }
                )
                ExpandedResultItem(
                    modifier = Modifier.weight(1f),
                    label = "Город",
                    value = item.city.ifEmpty { "-" }
                )
            }
        }

        // Display all team members
        if (item.members.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Участники:",
                fontSize = 12.sp,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            item.members.forEach { member ->
                Text(
                    text = "${member.name} ${member.surname}",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.medium(),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 1.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ExpandedResultItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    if (value.isNotBlank() && value != "-") {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.outline,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MemberMeta(distance: String, team: String, group: String, city: String) {
    Column(modifier = Modifier.padding(top = 6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(14.dp),
                tint = MaterialTheme.colorScheme.tertiary,
                imageVector = Icons.AutoMirrored.Filled.AirplaneTicket,
                contentDescription = null
            )
            Text(
                text = distance,
                fontSize = 12.sp,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.outline
            )
        }
        if (team.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(14.dp),
                    tint = MaterialTheme.colorScheme.tertiary,
                    imageVector = Icons.Default.Groups,
                    contentDescription = null
                )
                Text(
                    text = team,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.medium(),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        if (group.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(14.dp),
                    tint = MaterialTheme.colorScheme.tertiary,
                    imageVector = Icons.Default.Group,
                    contentDescription = null
                )
                Text(
                    text = group,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.medium(),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        if (city.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(14.dp),
                    tint = MaterialTheme.colorScheme.tertiary,
                    imageVector = Icons.Default.LocationCity,
                    contentDescription = null
                )
                Text(
                    text = city,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.medium(),
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}
