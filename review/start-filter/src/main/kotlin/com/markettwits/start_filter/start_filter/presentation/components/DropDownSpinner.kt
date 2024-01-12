package com.markettwits.start_filter.start_filter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun <E> DropDownSpinner(
    modifier: Modifier = Modifier,
    defaultText: String = "Select...",
    label : String = "Label",
    selectedItem: E,
    onItemSelected: (Int, E) -> Unit,
    itemList: List<E>?
) {
    var isOpen by remember { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxWidth()
            .background(Color.White),
        contentAlignment = Alignment.CenterStart
    ) {
        if (selectedItem == null || selectedItem.toString().isEmpty()) {
            Text(
                text = defaultText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 3.dp),
                color = SportSouceColor.SportSouceBlue
            )
        }
        MenuTextField(
            label = label,
            value = selectedItem?.toString() ?: "",
            onValueChange = {}
        )

        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(.85f)
                .background(Color.White),
            expanded = isOpen,
            onDismissRequest = {
                isOpen = false
            }
        ) {
            itemList?.forEachIndexed { index, item ->
                DropdownMenuItem(
                    colors = MenuDefaults.itemColors(
                        textColor = SportSouceColor.SportSouceBlue,
                    ),
                    text = {
                        Text(item.toString())
                    },
                    onClick = {
                        isOpen = false
                        onItemSelected(index, item)
                    }
                )
            }
        }
        androidx.compose.material3.Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
                .size(24.dp),
            tint = SportSouceColor.SportSouceBlue,
            imageVector = if (!isOpen) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
            contentDescription = null
        )
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable(
                    onClick = { isOpen = true }
                )
        )
    }
}

@Preview
@Composable
private fun DropDownSpinnerPreview() {
    DropDownSpinner(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        defaultText = "Select Country...",
        selectedItem = "selectedItem2",
        onItemSelected = { index, item ->
        },
        itemList = listOf("d", "d", "d", "d")
    )
}