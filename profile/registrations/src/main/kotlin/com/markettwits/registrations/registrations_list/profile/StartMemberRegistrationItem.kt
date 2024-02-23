package com.markettwits.registrations.registrations_list.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.registrations.registrations.domain.StartOrderInfo
import com.markettwits.registrations.start_order_profile.components.fakeOrder

@Composable
fun StartMemberRegistrationItems(startOrderInfo: StartOrderInfo) {
    val list = listOf(fakeOrder, fakeOrder, fakeOrder)
    StartOrderCardColumnList(starts = list) {

    }
}

@Preview
@Composable
private fun StartMemberRegistrationItemPreview() {
    SportSouceTheme {
        StartMemberRegistrationItems(fakeOrder)
    }
}
