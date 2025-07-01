package com.markettwits.sportsouce.start.presentation.result.newcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

@Composable
fun ResultDetailDialog(
    result: MemberResult,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = rememberScreenSizeInfo()
    val isTablet = configuration.wDP.value.dp >= 600.dp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(if (isTablet) 0.6f else 0.95f)
                .fillMaxHeight(if (isTablet) 0.8f else 0.9f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Заголовок диалога
                DialogHeader(
                    result = result,
                    onDismiss = onDismiss
                )

                // Содержимое
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Основная информация об участнике
                    ParticipantInfoSection(result)

                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Результаты по кругам
                    LapTimesSection(result)

                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Контрольные точки
                //    CheckpointsSection(result)

//                    if (result.status != ResultStatus.COMPLETED) {
//                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
//
//                        // Статус участника
//                        StatusSection(result)
//                    }
                }

                // Кнопки действий
               // DialogActions(onDismiss = onDismiss)
            }
        }
    }
}

@Composable
private fun DialogHeader(
    result: MemberResult,
    onDismiss: () -> Unit
) {
    Surface(
        color = when (result.place) {
            1 -> Color(0xFFFFD700).copy(alpha = 0.2f)
            2 -> Color(0xFFC0C0C0).copy(alpha = 0.2f)
            3 -> Color(0xFFCD7F32).copy(alpha = 0.2f)
            else -> MaterialTheme.colorScheme.primaryContainer
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Результат участника",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = result.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Место с медалькой
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (result.place) {
                    1 -> Text("🥇", fontSize = 24.sp)
                    2 -> Text("🥈", fontSize = 24.sp)
                    3 -> Text("🥉", fontSize = 24.sp)
                    else -> {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = result.place.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Закрыть",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun ParticipantInfoSection(result: MemberResult) {
    InfoSection(
        title = "Информация об участнике",
        icon = Icons.Default.Person
    ) {
        InfoRow("Номер участника", "#${result.id}")
        InfoRow("Команда", result.team)
        InfoRow("Пол", if (result.sex == "М") "Мужской" else "Женский")
        InfoRow("Возрастная группа", result.group)
        InfoRow("Дистанция", result.distance)
        InfoRow("Место в общем зачете", "${result.place} место")
    }
}

@Composable
private fun LapTimesSection(result: MemberResult) {
    InfoSection(
        title = "Время по кругам",
        icon = Icons.Default.AccessTime
    ) {
        result.circles.forEach { (lapName, time) ->
            InfoRow(
                label = "Круг : $lapName",
                value = time.ifBlank { "--:--:--" },
                valueColor = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        InfoRow(
            label = "Финальный результат",
            value = result.result,
            valueColor = MaterialTheme.colorScheme.primary,
            isHighlighted = true
        )
    }
}

//@Composable
//private fun StatusSection(result: MemberResult) {
//    InfoSection(
//        title = "Статус участника",
//        icon = Icons.Default.Info
//    ) {
//        val (statusText, statusColor) = when (result.) {
//            ResultStatus.DNF -> "Не финишировал (DNF)" to MaterialTheme.colorScheme.error
//            ResultStatus.DSQ -> "Дисквалифицирован (DSQ)" to MaterialTheme.colorScheme.error
//            ResultStatus.DNS -> "Не стартовал (DNS)" to MaterialTheme.colorScheme.outline
//            ResultStatus.COMPLETED -> "Финишировал" to MaterialTheme.colorScheme.primary
//        }
//
//        InfoRow(
//            label = "Статус",
//            value = statusText,
//            valueColor = statusColor
//        )
//    }
//}

@Composable
private fun InfoSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        content()
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    isHighlighted: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                Color.Transparent
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isHighlighted) 2.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isHighlighted) 12.dp else 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = if (isHighlighted) 16.sp else 14.sp,
                fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = value,
                fontSize = if (isHighlighted) 18.sp else 14.sp,
                fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}