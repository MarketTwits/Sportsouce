package com.markettwits.start.register.presentation.registration.presentation.components.distsance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationAdditionalField
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationDistance
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStatementAnswer
import com.markettwits.start.register.presentation.registration.presentation.components.distsance.additional_fields.RenderAdditionalFields
import com.markettwits.start.register.presentation.registration.presentation.components.distsance.member.StartRegistrationMemberCard


@Composable
internal fun StartRegistrationDistanceContent(
    modifier: Modifier = Modifier,
    distance: StartRegistrationDistance,
    onClickStartStatement: (StartStatement) -> Unit,
    onChangeStatementField: (StartRegistrationStatementAnswer) -> Unit,
    onChangeDistanceField : (StartRegistrationStatementAnswer) -> Unit
) {
    Column(modifier = modifier) {
        distance.stages.forEach {
            StartRegistrationMemberContent(
                title = it.stage.name,
                additionalFields = it.stage.additionalFields,
                startStatement = it.statement,
                onClickStartStatement = onClickStartStatement,
                onChangeAdditionalField = onChangeStatementField
            )
        }
        RenderAdditionalFields(
            modifier = Modifier.padding(10.dp),
            fields = distance.answers,
            onFieldChanged = onChangeDistanceField
        )
    }
}

@Composable
private fun StartRegistrationMemberContent(
    modifier: Modifier = Modifier,
    title: String,
    startStatement: StartStatement,
    additionalFields: List<StartRegistrationStatementAnswer>,
    onClickStartStatement: (StartStatement) -> Unit,
    onChangeAdditionalField: (StartRegistrationStatementAnswer) -> Unit

    ) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp),
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontNunito.semiBoldBold(),
            fontSize = 18.sp,
            overflow = TextOverflow.Ellipsis
        )
        StartRegistrationMemberCard(
            startStatement = startStatement,
            onClickStartStatement = onClickStartStatement
        )
        RenderAdditionalFields(
            modifier = Modifier.padding(10.dp),
            fields = additionalFields,
            onFieldChanged = onChangeAdditionalField
        )
    }
}
