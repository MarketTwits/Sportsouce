package com.markettwits.start_filter.start_filter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun StartFilterDropDownMenu(items : List<String>,onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(items.first()) }

    val focusRequester = FocusRequester()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = true
                    focusRequester.requestFocus()
                },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    expanded = false
                }
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .clickable { expanded = !expanded }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(text = item, color = SportSouceColor.SportSouceBlue)
                    },
                    onClick = {
                        selectedItem = item
                        expanded = false
                        onItemSelected(item)
                    }
                )
            }
        }
    }
}
