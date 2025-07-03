package com.markettwits.sportsouce.start.presentation.result.newcomponents

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AirplaneTicket
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RaceResultsScreen2(
    results: List<MemberResult>,
    onClickMemberResult: (MemberResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {

//            MemberResultFilterBar(
//                filterManager = manager,
//                availableDistances = results.map { it.distance }.toSet().toList(),
//                availableClubs = results.map { it.team }.toSet().toList(),
//                onFiltersChanged = {
//                    //manager.
//                }
//            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Всего ${results.size} результатов",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                )
            }

            itemsIndexed(results) { index, result ->
                ResultCard(
                    result = result,
                    onClickMemberResult = onClickMemberResult,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(tween(300))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(
                text = "Search by name or bib number...",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        },
        trailingIcon = {
            if (query.isNotBlank()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier
    )
}

//@Composable
//private fun RaceResultsHeader(
//    totalResults: Int,
//    windowSizeClass: WindowSizeClass,
//    modifier: Modifier = Modifier
//) {
//    val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
//
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.tertiaryContainer
//        ),
//        shape = RoundedCornerShape(if (isCompact) 12.dp else 16.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(if (isCompact) 12.dp else 16.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Start
//            ) {
//                Icon(
//                    imageVector = androidx.compose.material.icons.Icons.Default.Timer,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
//                    modifier = Modifier.size(if (isCompact) 20.dp else 24.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = "Показаны 1-25 из $totalResults записей",
//                    style = (if (isCompact) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleMedium).copy(
//                        fontFamily = FontNunito.medium(),
//                        color = MaterialTheme.colorScheme.onTertiaryContainer
//                    )
//                )
//            }
//
//            Spacer(modifier = Modifier.height(if (isCompact) 6.dp else 8.dp))
//
//            Text(
//                text = "Результаты участников выстроены по чистому (chip) времени",
//                style = (if (isCompact) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium).copy(
//                    fontFamily = FontNunito.regular(),
//                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
//                )
//            )
//        }
//    }
//}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun ResultCard(
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
                            fontFamily = FontNunito.semiBoldBold(),
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
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
                            fontFamily = FontNunito.regular(),
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
                            fontFamily = FontNunito.regular(),
                            fontSize = 12.sp,
                        )
                        Spacer(Modifier.width(12.dp))
                        Icon(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(16.dp),
                            tint = MaterialTheme.colorScheme.tertiary,
                            imageVector = Icons.Default.Groups,
                            contentDescription = result.bodyNumber,
                        )
                        Text(
                            text = result.team,
                            color = MaterialTheme.colorScheme.outline,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontFamily = FontNunito.regular(),
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
                    fontSize = 12.sp,
                    fontFamily = FontNunito.semiBoldBold(),
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

// АВАТАРЫ ДЛЯ РАЗНЫХ РАЗМЕРОВ
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
                    FontNunito.regular(),
                color = if (isHighlighted) MaterialTheme.colorScheme.secondary
                else
                    MaterialTheme.colorScheme.onBackground,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = label,
                fontFamily = FontNunito.regular(),
                color = MaterialTheme.colorScheme.outline,
                fontSize = 12.sp,
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