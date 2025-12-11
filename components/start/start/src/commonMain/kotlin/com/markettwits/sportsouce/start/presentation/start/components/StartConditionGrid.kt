package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.domain.StartItem.ConditionFile

@Composable
fun StartConditionGrid(
    modifier: Modifier = Modifier,
    conditionItems: List<StartItem.ConditionDetail>,
    conditionFile: ConditionFile = ConditionFile.Empty,
    onClickFile: (String) -> Unit = {},
) {
    // Filter items by type
    val regulation = conditionItems.filterIsInstance<StartItem.ConditionDetail.Regulation>().firstOrNull()
    val statement = conditionItems.filterIsInstance<StartItem.ConditionDetail.Statement>().firstOrNull()

    // State for managing which bottom sheet is open
    var selectedCondition by rememberSaveable(
        stateSaver = ConditionItemSaver
    ) { mutableStateOf<ConditionItem?>(null) }

    if (regulation == null && statement == null) {
        // Don't show anything if both are null
        return
    }

    Column(modifier = modifier.padding(top = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // First column
            if (regulation != null) {
                ConditionGridItem(
                    modifier = Modifier.weight(1f),
                    title = "Регламент",
                    description = "Правила проведения старта",
                    icon = Icons.Default.Description,
                    onClick = {
                        selectedCondition = ConditionItem(
                            title = "Регламент",
                            htmlContent = regulation.value,
                            conditionFile = null,
                            type = ConditionType.REGULATION
                        )
                    }
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Second column
            if (statement != null) {
                ConditionGridItem(
                    modifier = Modifier.weight(1f),
                    title = "Положение",
                    description = "Условия участия",
                    icon = Icons.AutoMirrored.Filled.Assignment,
                    onClick = {
                        selectedCondition = ConditionItem(
                            title = "Положение",
                            htmlContent = statement.value,
                            conditionFile = conditionFile.takeIf { it is ConditionFile.Base },
                            type = ConditionType.STATEMENT
                        )
                    }
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    // Show bottom sheet when a condition is selected
    selectedCondition?.let { condition ->
        ConditionDetailBottomSheet(
            title = condition.title,
            htmlContent = condition.htmlContent,
            conditionFile = condition.conditionFile,
            conditionType = condition.type,
            onDismiss = { selectedCondition = null },
            onClickFile = onClickFile
        )
    }
}

@Composable
private fun ConditionGridItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    OnBackgroundCard(
        modifier = modifier.height(140.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon with gradient background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                            ),
                            radius = 50f
                        ),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Text content
            Column(
                modifier = Modifier.weight(1f, fill = false),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp,
                    fontFamily = FontNunito.regular(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConditionDetailBottomSheet(
    title: String,
    htmlContent: String,
    conditionFile: ConditionFile? = null,
    conditionType: ConditionType = ConditionType.REGULATION,
    onDismiss: () -> Unit,
    onClickFile: (String) -> Unit = {},
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            ConditionDetailHeader(
                title = title,
                onDismiss = onDismiss
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (conditionType == ConditionType.STATEMENT && conditionFile is ConditionFile.Base) {
                FileHint()
                Spacer(modifier = Modifier.height(8.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp)
                ) {
                    HtmlText(
                        text = htmlContent,
                        fontSize = 16.sp,
                        fontFamily = FontNunito.medium(),
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        selectable = true
                    )

                    if (conditionType == ConditionType.STATEMENT && conditionFile is ConditionFile.Base) {
                        Spacer(modifier = Modifier.height(16.dp))
                        ConditionFileCard(
                            fileName = "Положение",
                            onClick = { onClickFile(conditionFile.url) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConditionDetailHeader(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = FontNunito.semiBoldBold(),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Закрыть",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}


private data class ConditionItem(
    val title: String,
    val htmlContent: String,
    val conditionFile: ConditionFile?,
    val type: ConditionType,
)

private val ConditionItemSaver = Saver<ConditionItem?, List<String>>(
    save = { item ->
        item?.let {
            listOf(
                it.title,
                it.htmlContent,
                (it.conditionFile as? ConditionFile.Base)?.url.orEmpty(),
                it.type.name
            )
        }
    },
    restore = { data ->
        if (data.size < 4) null else {
            ConditionItem(
                data[0],
                data[1],
                data.getOrNull(2)?.takeIf { it.isNotEmpty() }?.let { ConditionFile.Base(it) },
                ConditionType.valueOf(data[3])
            )
        }
    }
)

private enum class ConditionType { REGULATION, STATEMENT }

@Composable
private fun ConditionFileCard(
    modifier: Modifier = Modifier,
    fileName: String,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 6.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(28.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = fileName,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Файл доступен ниже — откройте или скачайте",
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.regular(),
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun FileHint() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "Файл положения ниже — прокрутите текст",
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontNunito.regular(),
                fontSize = 13.sp
            )
        }
    }
}
