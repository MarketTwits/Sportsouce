package com.markettwits.sportsouce.bottom_bar.component.listener

enum class BottomBarVisibilityStrategy {

    AlwaysVisible {
        override fun onStart(listener: BottomBarVisibilityListener) {
            listener.show()
        }

        override fun onStop(listener: BottomBarVisibilityListener) {
            listener.show()
        }
    },

    AlwaysInvisible {
        override fun onStart(listener: BottomBarVisibilityListener) {
            listener.hide()
        }

        override fun onStop(listener: BottomBarVisibilityListener) {
            listener.show()
        }
    };

    internal abstract fun onStart(
        listener: BottomBarVisibilityListener,
    )

    internal abstract fun onStop(
        listener: BottomBarVisibilityListener,
    )
}