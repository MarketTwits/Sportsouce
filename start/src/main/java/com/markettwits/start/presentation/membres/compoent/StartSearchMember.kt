package com.markettwits.start.presentation.membres.compoent

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.membres.MockStartMembersScreen
import com.markettwits.start.presentation.membres.StartMembersScreen
import com.markettwits.start.presentation.membres.StartMembersScreenComponent
import com.markettwits.start.presentation.start.MockStartScreen

@Composable
fun StartSearchMember(modifier: Modifier = Modifier, component: StartMembersScreen) {
    var comment by rememberSaveable {
        mutableStateOf("")
    }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.medium),
            value = comment,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = SportSouceColor.SportSouceBlue,
                focusedIndicatorColor =  Color.Transparent,
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

    }
@Preview
@Composable
private fun StartSearchMemberPreview(){
    Box(modifier = Modifier.padding(10.dp)){
        StartSearchMember(component = MockStartMembersScreen())
    }
}