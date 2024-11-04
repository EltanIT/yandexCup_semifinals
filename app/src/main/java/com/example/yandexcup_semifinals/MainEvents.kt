package com.example.yandexcup_semifinals

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntSize
import com.example.yandexcup_semifinals.canvas.DrawFigure
import com.example.yandexcup_semifinals.components.BottomPanelActionsType

sealed class MainEvents {

    data object Play: MainEvents()
    data object Stop: MainEvents()

    data class ChangeSelectedColor(val color: Color): MainEvents()
    data class ChangeSelectedFigure(val figure: DrawFigure): MainEvents()
    data class ChangeSelectedAction(val action: BottomPanelActionsType): MainEvents()


    data class AddPathInCurrentCanvas(val path: Path, val isDelete: Boolean): MainEvents()
    data class ErasePathInCurrentCanvas(val path: Path): MainEvents()
    data object DeleteLastPathInCurrentCanvas: MainEvents()
    data object ReturnLastPathInCurrentCanvas: MainEvents()


    data object CreateNewCanvas: MainEvents()
    data class SelectCanvasByIndex(val index: Int): MainEvents()
    data object DeleteCurrentCanvas: MainEvents()
    data object DeleteAll: MainEvents()


    data class InitCanvasSize(val size: IntSize): MainEvents()
    data class GenerateCanvasShots(val count: Int): MainEvents()



    data class ChangePencilAndEraseWidth(val width: Float): MainEvents()
    data object DublicateCanvas: MainEvents()


    data class ChangePlayingSpeed(val duration: Long): MainEvents()
}