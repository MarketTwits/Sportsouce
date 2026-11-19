package com.markettwits.sportsouce.shop.order.presentation.components.recipient

import com.markettwits.core_ui.items.theme.SauceTheme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import androidx.compose.foundation.layout.navigationBarsPadding
import com.markettwits.core_ui.items.components.buttons.SauceButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.sportsouce.shop.order.domain.model.ShopRecipient
import com.markettwits.sportsouce.shop.order.presentation.store.ShopCreateOrderStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShopOrderRecipientBottomSheet(
    modifier: Modifier = Modifier,
    state: ShopCreateOrderStore.State.ShopRecipientState,
    onClickChangeRecipient: () -> Unit,
    onChangeRecipient : (ShopRecipient) -> Unit,
    onDismiss: () -> Unit,
){

    DefaultModalBottomSheet(
        title = "Заказчик",
        onDismissRequest = onDismiss
    ) {
        ShopOrderRecipientChangeContent(
            modifier = modifier,
            recipient = state.mutableShopRecipient,
            message = state.message,
            onDismiss = onDismiss,
            onChangeRecipient = onChangeRecipient ,
            onClickUpdate = {
                onClickChangeRecipient()
            }
        )
    }
}

@Composable
private fun ShopOrderRecipientChangeContent(
    modifier: Modifier,
    recipient: ShopRecipient,
    message : String,
    onDismiss : () -> Unit,
    onChangeRecipient: (ShopRecipient) -> Unit,
    onClickUpdate : () -> Unit
){
    Column(
        modifier = modifier
            .sauceVerticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp).navigationBarsPadding()
    ) {
        AnimatedVisibility(message.isNotEmpty()){
            Text(
                modifier = Modifier
                    .padding(4.dp),
                textAlign = TextAlign.Start,
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = SauceTheme.colors.danger.content
            )
        }
        OutlinedTextFieldBase(
            modifier = Modifier
                .padding(4.dp),
            value = recipient.name,
            label = "Имя",
            onValueChange = {
                onChangeRecipient(recipient.copy(name = it))
            }
        )
        OutlinePhoneTextFiled(
            modifier = Modifier
                .padding(4.dp),
            value = recipient.phone,
            label = "Номер телефона",
            onValueChange = {
                onChangeRecipient(recipient.copy(phone = it))
            }
        )
        SauceButton(
            modifier = Modifier
                .padding(10.dp),
            title = "Обновить",
            onClick = onClickUpdate
        )
    }
}
