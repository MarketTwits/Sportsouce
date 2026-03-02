package com.markettwits.sportsouce.start.register.presentation.registration.distance.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationDistance
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.sportsouce.start.register.presentation.registration.distance.components.member.StartRegistrationMemberCard
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.RegistrationMemberValidatorBase


@Composable
internal fun StartRegistrationDistanceContent(
    modifier: Modifier = Modifier,
    distance: StartRegistrationDistance,
    invalidStageIds: Set<Int>,
    validationAttemptTick: Int,
    onClickStartStatement: (StartStatement) -> Unit,
    onChangeStatementField: (StartRegistrationStatementAnswer) -> Unit,
    onChangeDistanceField: (StartRegistrationStatementAnswer) -> Unit,
) {
    Column(modifier = modifier) {
        distance.stages.forEach {
            StartRegistrationMemberContent(
                title = it.stage.name,
                additionalFields = it.stage.additionalFields,
                startStatement = it.statement,
                isValidationError = invalidStageIds.contains(it.stage.id),
                validationAttemptTick = validationAttemptTick,
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
    isValidationError: Boolean,
    validationAttemptTick: Int,
    onClickStartStatement: (StartStatement) -> Unit,
    onChangeAdditionalField: (StartRegistrationStatementAnswer) -> Unit,

    ) {
    val isMemberValid = RegistrationMemberValidatorBase().validateFields(startStatement).isSuccess
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis
            )
            if (!isMemberValid) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "* Данные для регистрации не заполнены",
                    color = if (isValidationError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 13.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        StartRegistrationMemberCard(
            startStatement = startStatement,
            isValidationError = isValidationError,
            validationAttemptTick = validationAttemptTick,
            onClickStartStatement = onClickStartStatement
        )
        RenderAdditionalFields(
            modifier = Modifier.padding(10.dp),
            fields = additionalFields,
            onFieldChanged = onChangeAdditionalField
        )
    }
}
