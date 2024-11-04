package com.example.yandexcup_semifinals.canvas

import androidx.compose.ui.graphics.StrokeCap

sealed class DrawType(
    open var width: Float,
    open val cap: StrokeCap
){

    data class PENCIL(
        override var width: Float = 4f,
        override val cap: StrokeCap = StrokeCap.Round
    ): DrawType(
        width,
        cap
    )

    data class BRUSH(
        override var width: Float = 10f,
        override val cap: StrokeCap = StrokeCap.Round
    ): DrawType(
        width,
        cap
    )

    data class FIGURE(
        override var width: Float = 6f,
        override val cap: StrokeCap = StrokeCap.Round
    ): DrawType(
        width,
        cap
    )

    data class ERASE(
        override var width: Float = 20f,
        override val cap: StrokeCap = StrokeCap.Round
    ): DrawType(
        width,
        cap
    )

}