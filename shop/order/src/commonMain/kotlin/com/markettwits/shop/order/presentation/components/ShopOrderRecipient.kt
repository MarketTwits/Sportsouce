package com.markettwits.shop.order.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.components.textField.mapSimpleRuPhoneNumberToFull
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.order.domain.model.ShopRecipient

@Composable
internal fun ShopOrderRecipient(
    modifier: Modifier = Modifier,
    shopRecipient: ShopRecipient,
    onClick : () -> Unit,
) {
    ShopBasicSectorContent(
        modifier = modifier, title = "Получатель"
    ){
        OnBackgroundCard(
            modifier = Modifier.padding(10.dp),
            onClick = onClick
        ) {
            Row(
                modifier = modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(4.dp),
                    imageVector = Icons.Default.Face,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                Column {
                    AnimatedContent(shopRecipient.name){
                        Text(
                            text = shopRecipient.name,
                            fontSize = 12.sp,
                            fontFamily = FontNunito.semiBoldBold(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    AnimatedContent(shopRecipient.phone){
                        Text(
                            text = mapSimpleRuPhoneNumberToFull(shopRecipient.phone) ,
                            fontSize = 12.sp,
                            fontFamily = FontNunito.semiBoldBold(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShopOrderRecipientBottomSheet(
    modifier: Modifier = Modifier,
    recipient: ShopRecipient,
    onChangeRecipient: (ShopRecipient) -> Unit,
    onDismiss: () -> Unit,
){
    var currentRecipient by remember {
        mutableStateOf(recipient)
    }

    ModalBottomSheet(
        modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.primary,
        onDismissRequest = onDismiss
    ) {
        ShopOrderRecipientChangeContent(
            modifier = modifier,
            recipient = currentRecipient,
            onDismiss = onDismiss,
            onChangeRecipient = {
                currentRecipient = it
            },
            onClickUpdate = {
                onChangeRecipient(currentRecipient)
                onDismiss()
            }
        )
    }

}

@Composable
private fun ShopOrderRecipientChangeContent(
    modifier: Modifier,
    recipient: ShopRecipient,
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
