package com.markettwits.start_search.search.presentation.components.publish

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito


@Composable
fun StartsSearchBarPublic(modifier: Modifier = Modifier) {
    var query by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        enabled = false,
        modifier = modifier
            .padding(10.dp)
            .height(52.dp)
            .shadow(2.dp, Shapes.extraLarge)
            .fillMaxWidth(),
        value = query,
        onValueChange = {
            query = it
        },
        colors = TextFieldDefaults.colors(
            disabledContainerColor = Color.White,
        ),
        placeholder = {
            Text(
                text = "Поиск старта",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                color = Color.Gray
            )

        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск старта",
                tint = Color.Gray
            )
        },
        shape = Shapes.extraLarge
    )
}

@Preview(showBackground = true)
@Composable
private fun StartsSearchBarPreview() {
    StartsSearchBarPublic()
}