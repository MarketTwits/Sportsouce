package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.foundation.layout.*
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn as LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.divider.BaseDivider
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.common.components.StartListField
import com.markettwits.sportsouce.start.presentation.common.components.StartListFacts
import com.markettwits.sportsouce.start.presentation.common.components.StartListCard
import com.markettwits.sportsouce.start.presentation.common.components.StartResultSummary
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

@Composable
fun MemberResultDetailed(memberResult: MemberResult, onBackClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            StartListCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(memberResult.name.replace("\\", "\n"), Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge, color = SauceTheme.colors.textPrimary)
                    StartResultSummary(memberResult.place.toString(), memberResult.result, memberResult.shift)
                }
            }
        }
        item {
            OnBackgroundCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    StartListField("Стартовый номер", memberResult.bodyNumber)
                    StartListField("Дистанция", memberResult.distance)
                    StartListField("Категория", memberResult.group)
                    StartListField("Команда", memberResult.team)
                    StartListField("Пол", memberResult.sex)
                }
            }
        }
        if (memberResult.circles.isNotEmpty()) {
            item {
                OnBackgroundCard {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Контрольные точки", Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium,
                            color = SauceTheme.colors.textPrimary)
                        memberResult.circles.forEach { (checkpoint, time) ->
                            StartListField("КП $checkpoint", time)
                            BaseDivider()
                        }
                    }
                }
            }
        }
        item { StartListFacts(listOf("ID старта" to memberResult.startId.toString(), "ID участника" to memberResult.id.toString())) }
        item { SauceButton("Закрыть", onBackClick, variant = SauceButtonVariant.Text) }
    }
}
