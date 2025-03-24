package com.markettwits.shop.order.presentation.components.recipient

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.shop.order.domain.model.ShopRecipient
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShopOrderRecipientBottomSheet(
    modifier: Modifier = Modifier,
    state: ShopCreateOrderStore.State.ShopRecipientState,
    onClickChangeRecipient: () -> Unit,
    onChangeRecipient : (ShopRecipient) -> Unit,
    onDismiss: () -> Unit,
){

    ModalBottomSheet(
        modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.primary,
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
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.primary)
            .imePadding()
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Заказчик",
                fontFamily = FontNunito.bold(),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        AnimatedVisibility(message.isNotEmpty()){
            Text(
                modifier = Modifier
                    .padding(4.dp),
                textAlign = TextAlign.Start,
                text = message,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                color = SportSouceColor.SportSouceLightRed
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
        ButtonContentBase(
            modifier = Modifier
                .padding(10.dp),
            title = "Обновить",
            containerColor = MaterialTheme.colorScheme.secondary,
            textColor = MaterialTheme.colorScheme.onSecondary,
            onClick = onClickUpdate
        )
    }
}