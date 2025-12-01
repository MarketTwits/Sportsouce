package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun StartConditionGrid(
    modifier: Modifier = Modifier,
    conditionItems: List<StartItem.ConditionDetail>,
) {
    // Filter items by type
    val regulation = conditionItems.filterIsInstance<StartItem.ConditionDetail.Regulation>().firstOrNull()
    val statement = conditionItems.filterIsInstance<StartItem.ConditionDetail.Statement>().firstOrNull()

    // State for managing which bottom sheet is open
    var selectedCondition by remember { mutableStateOf<ConditionItem?>(null) }

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
                            htmlContent = regulation.value
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
                            htmlContent = statement.value
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
            onDismiss = { selectedCondition = null }
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
    onDismiss: () -> Unit,
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
            Surface(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(32.dp)
                    .height(4.dp),
                shape = RoundedCornerShape(2.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            ) {}
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            // Header
            ConditionDetailHeader(
                title = title,
                onDismiss = onDismiss
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                HtmlText(
                    text = htmlContent,
                    fontSize = 15.sp,
                    fontFamily = FontNunito.medium(),
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    selectable = true
                )
            }
        }
    }
}

@Composable
private fun ConditionDetailHeader(
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

        IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(40.dp)
        ) {
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
)
