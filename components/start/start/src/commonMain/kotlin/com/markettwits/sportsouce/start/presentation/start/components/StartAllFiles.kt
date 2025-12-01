package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.domain.StartItem

@Composable
internal fun StartAllFiles(
    modifier: Modifier = Modifier,
    results: List<StartItem.Result> = emptyList(),
    usefulLinks: List<StartItem.Result> = emptyList(),
    conditionFile: StartItem.ConditionFile? = null,
    onClickFile: (String) -> Unit,
) {
    val allFiles = mutableListOf<FileItem>()

    results.forEach { result ->
        allFiles.add(
            FileItem(
                name = result.name,
                url = result.url,
                type = FileType.RESULT
            )
        )
    }

    usefulLinks.forEach { link ->
        allFiles.add(
            FileItem(
                name = link.name,
                url = link.url,
                type = FileType.USEFUL_LINK
            )
        )
    }

    if (conditionFile is StartItem.ConditionFile.Base) {
        allFiles.add(
            FileItem(
                name = "Положение",
                url = conditionFile.url,
                type = FileType.CONDITION
            )
        )
    }

    if (allFiles.isNotEmpty()) {
        var isExpanded by rememberSaveable { mutableStateOf(false) }

        StartContentBasePanel(modifier = modifier, label = "Файлы и ссылки") {
            val displayedFiles = if (isExpanded) allFiles else allFiles.take(3)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                displayedFiles.forEach { file ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                    ) {
                        StartFileWithType(
                            modifier = Modifier.padding(vertical = 4.dp),
                            fileName = file.name,
                            fileType = file.type,
                            onClick = {
                                onClickFile(file.url)
                            }
                        )
                    }
                }

                AnimatedVisibility(
                    visible = allFiles.size > 3,
                    enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                ) {
                    TextButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier
                    ) {
                        Text(
                            text = if (isExpanded) "Свернуть" else "Ещё ${allFiles.size - 3}",
                            fontFamily = FontNunito.semiBoldBold(),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StartFileWithType(
    modifier: Modifier = Modifier,
    fileName: String,
    fileType: FileType,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 4.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon
            Surface(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Text
                Text(
                    text = fileName,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Type indicator
                Text(
                    text = fileType.displayName,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontFamily = FontNunito.regular(),
                    fontSize = 11.sp
                )
            }

            // Arrow
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private enum class FileType(val displayName: String) {
    RESULT("Результаты"),
    USEFUL_LINK("Полезная ссылка"),
    CONDITION("Положение")
}

private data class FileItem(
    val name: String,
    val url: String,
    val type: FileType,
)