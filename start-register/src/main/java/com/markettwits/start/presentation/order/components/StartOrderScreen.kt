package com.markettwits.start.presentation.order.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.order.components.members.StartMembers
import com.markettwits.start.presentation.order.components.order.OrderComponentBox
import com.markettwits.start.presentation.order.components.payment.PaymentTypeBox
import com.markettwits.start.presentation.order.components.promo.PromoBox
import com.markettwits.start.presentation.registration.components.StartRegistrationTopBar

@Composable
fun StartOrderScreen() {
    Scaffold(
        topBar = {
            StartRegistrationTopBar(goBack = {

            })
        },
        containerColor = SportSouceColor.DirtyWhite
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(10.dp)
            ) {
                StartDistanceInfoBox(
                    modifier = Modifier.padding(5.dp),
                    format = "Эстафета",
                    distances = listOf("600м")
                )
                StartMembers()
                PaymentTypeBox()
                PromoBox()
                OrderComponentBox(modifier = Modifier.padding(10.dp))
            }
        }

    }
}

@Preview(device = Devices.DEFAULT)
@Composable
private fun StartOrderScreenPreview() {
    StartOrderScreen()
}