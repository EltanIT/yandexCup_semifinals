package com.example.yandexcup_semifinals.components

import androidx.annotation.DrawableRes

data class BottomPanelAction(
    val type: BottomPanelActionsType,
    @DrawableRes val icon: Int?,
    val contentDes: String?
)
