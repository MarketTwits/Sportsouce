package com.markettwits.bottom_bar.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import com.markettwits.bottom_bar.component.BottomBarStorage
import com.markettwits.bottom_bar.component.BottomBarStorageImpl

fun rememberBottomBarNestedScroll(
    bottomBarStorage: BottomBarStorage = BottomBarStorageImpl
): NestedScrollConnection =
    object : NestedScrollConnection {
        private var accumulatedScroll = 0f
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            accumulatedScroll += available.y
            if (accumulatedScroll < 0) {
                bottomBarStorage.show()
                accumulatedScroll = 0f
            } else if (accumulatedScroll > 0) {
                bottomBarStorage.hide()
                accumulatedScroll = 0f
            }
            return Offset.Zero
        }
    }