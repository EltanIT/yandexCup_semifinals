package com.example.yandexcup_semifinals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.yandexcup_semifinals.components.BottomPanel
import com.example.yandexcup_semifinals.components.BottomPanelActionsType
import com.example.yandexcup_semifinals.components.CanvasGeneratorDialog
import com.example.yandexcup_semifinals.components.CanvasPlayDialog
import com.example.yandexcup_semifinals.components.CanvasScreen
import com.example.yandexcup_semifinals.components.ChangeWidthSlider
import com.example.yandexcup_semifinals.components.ColorsPaletteComponent
import com.example.yandexcup_semifinals.components.FigurePaletteComponent
import com.example.yandexcup_semifinals.components.TopPanel

@Composable
fun DrawApp(viewModel: MainViewModel) {

    val selectedColor = viewModel.selectedColor.value

    val selectedFigure = viewModel.selectedFigure.value

    val pencilWidth = viewModel.pencilWidth.value


    val allCanvasShots = viewModel.allCanvasShots.toList()


    val canvasCurrentPaths = viewModel.canvasCurrentPaths.toList()
    val canvasPreviousPaths = viewModel.canvasPreviousPaths.toList()



    val canvasIsBackPathAccess = viewModel.canvasIsBackPathAccess.value

    val canvasIsReturnPathAccess = viewModel.canvasIsReturnPathAccess.value

    val canvasIsPlayAccess = viewModel.canvasIsPlayAccess.value

    val isPlaying = viewModel.IsPlaying.value

    val isLoading = viewModel.isLoading.value



    val selectedAction = viewModel.selectedAction.value

    val selectedCanvasShotId = viewModel.selectedCanvasShotId.value

    val playingSpeed = viewModel.playingSpeed.value


    var bottomPanelHeight by remember{
        mutableStateOf(0.dp)
    }



    var colorPaletteIsOpen by remember{
        mutableStateOf(false)
    }

    var figurePaletteIsOpen by remember{
        mutableStateOf(false)
    }

    var canvasGeneratorIsOpen by remember{
        mutableStateOf(false)
    }

    var canvasPlaySettingsIsOpen by remember{
        mutableStateOf(false)
    }

    var changeWidthIsOpen by remember{
        mutableStateOf(false)
    }




    val interactionSource = remember { MutableInteractionSource() }
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.background)
    ){
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 16.dp, end = 16.dp, top = 15.5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TopPanel(
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = colorPaletteIsOpen || figurePaletteIsOpen || changeWidthIsOpen,
                    ) {
                        colorPaletteIsOpen = false
                        figurePaletteIsOpen = false
                        changeWidthIsOpen = false
                    }
                    .pointerInput(
                        colorPaletteIsOpen || figurePaletteIsOpen || changeWidthIsOpen
                    ) {
                        colorPaletteIsOpen = false
                        figurePaletteIsOpen = false
                        changeWidthIsOpen = false
                    },
                backIsActive = canvasIsBackPathAccess,
                nextIsActive = canvasIsReturnPathAccess,
                stopIsActive = !canvasIsPlayAccess && isPlaying,
                playIsActive = canvasIsPlayAccess,
                isLoading = isLoading,
                isPlaying = isPlaying,

                selectedCanvasShotId = selectedCanvasShotId,

                canvasIds = List(allCanvasShots.size) { index ->
                    index
                },

                play = {
                    canvasPlaySettingsIsOpen = !canvasPlaySettingsIsOpen
                },
                stop = {
                    viewModel.onEvent(MainEvents.Stop)
                },
                onBackCanvasPathClick = {
                    viewModel.onEvent(MainEvents.DeleteLastPathInCurrentCanvas)
                },
                onReturnCanvasPathClick = {
                    viewModel.onEvent(MainEvents.ReturnLastPathInCurrentCanvas)
                },
                deleteCurrentCanvas = {
                    viewModel.onEvent(MainEvents.DeleteCurrentCanvas)
                },
                deleteAll = {
                    viewModel.onEvent(MainEvents.DeleteAll)
                },
                createNewCanvas = {
                    viewModel.onEvent(MainEvents.CreateNewCanvas)
                },
                selectCanvasShot = { id ->
                    viewModel.onEvent(MainEvents.SelectCanvasByIndex(id))
                }
            )


            Spacer(modifier = Modifier.padding(top = 32.5.dp))


            CanvasScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = colorPaletteIsOpen || figurePaletteIsOpen || changeWidthIsOpen,
                    ) {
                        colorPaletteIsOpen = false
                        figurePaletteIsOpen = false
                        changeWidthIsOpen = false
                    }
                    .pointerInput(
                        colorPaletteIsOpen || figurePaletteIsOpen || changeWidthIsOpen
                    ) {
                        colorPaletteIsOpen = false
                        figurePaletteIsOpen = false
                        changeWidthIsOpen = false
                    },
                canvasPreviousPaths = canvasPreviousPaths,
                currentCanvasPaths = canvasCurrentPaths,
                allCanvasShots = allCanvasShots,
                selectedFigure = selectedFigure,
                isLoading = isLoading,
                isPlaying = isPlaying,
                playSpeed = playingSpeed,

                actionsType = selectedAction,
                onChangePath = { path, isDelete ->
                    viewModel.onEvent(MainEvents.AddPathInCurrentCanvas(path, isDelete))
                },
                onCanvasSizeInit = {
                    viewModel.onEvent(MainEvents.InitCanvasSize(it))
                }
            )


            Spacer(modifier = Modifier.padding(top = 22.dp))


            val localDensity = LocalDensity.current
            BottomPanel(
                modifier = Modifier
                    .onGloballyPositioned { coor ->
                        bottomPanelHeight = with(localDensity) { coor.size.height.toDp() }
                    }
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = colorPaletteIsOpen || figurePaletteIsOpen || changeWidthIsOpen,
                    ) {
                        colorPaletteIsOpen = false
                        figurePaletteIsOpen = false
                        changeWidthIsOpen = false
                    }
                    .pointerInput(
                        colorPaletteIsOpen || figurePaletteIsOpen || changeWidthIsOpen
                    ) {
                        colorPaletteIsOpen = false
                        figurePaletteIsOpen = false
                        changeWidthIsOpen = false
                    },
                selectedColor = selectedColor,
                selectedAction = selectedAction,
                isLoading = isLoading,
                isPlaying = isPlaying,
                onChangeSelectedItem = { action ->

                    when (action) {
                        BottomPanelActionsType.COLOR -> {
                            colorPaletteIsOpen = !colorPaletteIsOpen
                        }
                        BottomPanelActionsType.FIGURE -> {
                            figurePaletteIsOpen = !figurePaletteIsOpen
                            viewModel.onEvent(MainEvents.ChangeSelectedAction(action))
                        }
                        BottomPanelActionsType.CHANGE_WIDTH -> {
                            changeWidthIsOpen = !changeWidthIsOpen
                        }

                        BottomPanelActionsType.CANVAS_GENERATOR -> {
                            canvasGeneratorIsOpen = !canvasGeneratorIsOpen
                        }
                        BottomPanelActionsType.DUBLICATE_CANVAS -> {
                            viewModel.onEvent(MainEvents.DublicateCanvas)
                        }

                        else -> {
                            viewModel.onEvent(MainEvents.ChangeSelectedAction(action))
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.padding(top = 22.dp))
        }

        if (colorPaletteIsOpen){
            ColorsPaletteComponent(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = bottomPanelHeight + 16.dp + 22.dp)
            ) {
                viewModel.onEvent(MainEvents.ChangeSelectedColor(it))
                colorPaletteIsOpen = false
            }
        }

        if (figurePaletteIsOpen){
            FigurePaletteComponent(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = bottomPanelHeight + 16.dp + 22.dp)
            ) {
                viewModel.onEvent(MainEvents.ChangeSelectedFigure(it))
                figurePaletteIsOpen = false
            }
        }

        if (canvasGeneratorIsOpen){
            CanvasGeneratorDialog(
                countAllCanvasShots = allCanvasShots.size,
                onClose = {
                    canvasGeneratorIsOpen = false
                }
            ) { count ->
                canvasGeneratorIsOpen = false
                viewModel.onEvent(MainEvents.GenerateCanvasShots(count))
            }
        }

        if (canvasPlaySettingsIsOpen){
            CanvasPlayDialog(
                speedInML = playingSpeed,
                onChangeSpeedPlay = {
                    viewModel.onEvent(MainEvents.ChangePlayingSpeed(it.toLong()))
                },
                onClose = {
                    canvasPlaySettingsIsOpen = false
                }
            ) {
                canvasPlaySettingsIsOpen = false
                viewModel.onEvent(MainEvents.Play)
            }
        }

        if (changeWidthIsOpen){
            ChangeWidthSlider(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = bottomPanelHeight + 16.dp + 22.dp),
                value = pencilWidth
            ) { width ->
                viewModel.onEvent(MainEvents.ChangePencilAndEraseWidth(width))
            }
        }

    }
}