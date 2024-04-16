package com.markettwits.start.register.presentation.order.presentation.components.members

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.register.domain.StartStatement

@Composable
fun StartMembers(
    modifier: Modifier = Modifier,
    members: List<StartStatement>,
    onClickMember: (StartStatement, Int) -> Unit
) {
    OnBackgroundCard(
        modifier = modifier.padding(top = 10.dp)
    ) {
        Column(modifier = it.padding(10.dp)) {
            Text(
                modifier = modifier,
                text = "Участники :",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
            Column {
                members.forEachIndexed { index, member ->
                    StartMemberBox(
                        modifier = modifier.padding(top = 10.dp, bottom = 10.dp),
                        onClick = { onClickMember(member, index) },
                        stage = "Участник ${index + 1}",
                        memberName = "${member.surname} ${member.name}"
                    )
                }
            }
        }
    }

}