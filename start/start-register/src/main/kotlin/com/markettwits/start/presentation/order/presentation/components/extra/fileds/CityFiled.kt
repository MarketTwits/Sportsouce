package com.markettwits.start.presentation.order.presentation.components.extra.fileds

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.components.textField.OutlinedTextFieldBase
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.presentation.components.extra.FilterPosition

@Composable
fun CityFiled(
    modifier: Modifier = Modifier,
    statement: StartStatement,
    onValueChanged: (StartStatement) -> Unit
) {
    var cityChecked by rememberSaveable {
        mutableStateOf(false)
    }
    if (cityChecked) {
        OutlinedTextFieldBase(
            label = "Город",
            value = statement.city,
            onValueChange = {
                onValueChanged(statement.copy(city = it))
            }
        )
    } else {
        ItemsTextFiledDialog(
            label = "Город",
            value = statement.city,
            items = statement.cities.map { it.name },
            onValueChanged = {
                onValueChanged(statement.copy(city = it))
            })
    }
    FilterPosition(
        item = "В списке нет моего города",
        checked = cityChecked,
        onClick = { cityChecked = !cityChecked }
    )
}