package com.markettwits.start.presentation.order.components.extra.fileds

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.components.extra.FilterPosition
import com.markettwits.start.presentation.order.components.extra.RegistrationTextField

@Composable
fun TeamFiled(
    modifier: Modifier = Modifier,
    statement: StartStatement,
    onValueChanged: (StartStatement) -> Unit
) {
    Column(modifier = modifier) {
        var teamChecked by rememberSaveable {
            mutableStateOf(false)
        }
        if (teamChecked) {
            RegistrationTextField(
                label = "Команда",
                value = "Лично",
                enabled = false,
                onValueChange = {
                    onValueChanged(statement.copy(team = it))
                }
            )
        } else {
            RegistrationFiled(
                label = "Команда",
                value = statement.team,
                items = statement.teams.map { it.name },
                onValueChanged = {
                    onValueChanged(statement.copy(team = it))
                })
        }
        FilterPosition(
            item = "Я участвую лично, нет команды",
            checked = teamChecked,
            onClick = {
                onValueChanged(statement.copy(team = "Лично"))
                teamChecked = !teamChecked
            }
        )
    }

}