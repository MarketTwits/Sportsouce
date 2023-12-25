package com.markettwits.start.presentation.membres.list.compoent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.membres.list.MockStartMembersScreen
import com.markettwits.start.presentation.membres.list.StartMembersScreen

@Composable
fun StartSearchMember(modifier: Modifier = Modifier, component: StartMembersScreen) {
    var comment by rememberSaveable {
        mutableStateOf("")
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TextField(
            modifier = Modifier
                // .fillMaxWidth()
                .clip(Shapes.medium),
            value = comment,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = SportSouceColor.SportSouceBlue,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            placeholder = {
                Text(
                    text = "Поиск участника",
                    fontSize = 14.sp,
                    fontFamily = FontNunito.regular,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            },
            onValueChange = {
                comment = it
                component.filter(comment)
            })
        Icon(
            modifier = modifier.clickable {
                component.openFilter()
            },
            imageVector = Icons.Default.Settings,
            contentDescription = "filter",
            tint = SportSouceColor.SportSouceBlue
        )
    }


}

@Preview
@Composable
private fun StartSearchMemberPreview() {
    Box(modifier = Modifier.padding(10.dp)) {
        StartSearchMember(component = MockStartMembersScreen())
    }
}