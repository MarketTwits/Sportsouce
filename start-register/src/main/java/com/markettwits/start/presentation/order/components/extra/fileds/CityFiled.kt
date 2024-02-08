package com.markettwits.start.presentation.order.components.extra.fileds

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
fun CityFiled(
    modifier: Modifier = Modifier,
    statement: StartStatement,
    onValueChanged: (StartStatement) -> Unit
) {
    //  Column(modifier = modifier) {
    var cityChecked by rememberSaveable {
        mutableStateOf(false)
    }
    if (cityChecked) {
        RegistrationTextField(
            label = "Город",
            value = statement.city,
            onValueChange = {
                onValueChanged(statement.copy(city = it))
            }
        )
    } else {
        RegistrationFiled(
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
    // }
}