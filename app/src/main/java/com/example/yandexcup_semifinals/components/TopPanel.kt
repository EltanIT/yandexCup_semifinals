package com.example.yandexcup_semifinals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yandexcup_semifinals.R
import com.example.yandexcup_semifinals.ui.theme.NotActiveColor

@Composable
fun TopPanel(
    modifier: Modifier = Modifier,
    backIsActive: Boolean,
    nextIsActive: Boolean,
    playIsActive: Boolean,
    stopIsActive: Boolean,
    isLoading: Boolean,
    isPlaying: Boolean,

    selectedCanvasShotId: Int,

    canvasIds: List<Int>,

    play: () -> Unit,
    stop: () -> Unit,
    onBackCanvasPathClick: () -> Unit,
    onReturnCanvasPathClick: () -> Unit,
    deleteCurrentCanvas: () -> Unit,
    deleteAll: () -> Unit,
    createNewCanvas: () -> Unit,
    selectCanvasShot: (Int) -> Unit,
) {

    var layersExpanded by remember{
        mutableStateOf(false)
    }

    var deleteExpanded by remember{
        mutableStateOf(false)
    }

    var isPlayingState by remember{
        mutableStateOf(isPlaying)
    }

    isPlayingState = isPlaying

    Row(
        modifier
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "back",

                tint = if (backIsActive) MaterialTheme.colorScheme.onBackground else NotActiveColor,
                modifier = Modifier.clickable(backIsActive && !isLoading && !isPlaying) {
                    onBackCanvasPathClick()
                }
            )
            Icon(
                painter = painterResource(id = R.drawable.arrows_next),
                contentDescription = "next",
                tint = if (nextIsActive) MaterialTheme.colorScheme.onBackground else NotActiveColor,
                modifier = Modifier.clickable(nextIsActive && !isLoading && !isPlaying) {
                    onReturnCanvasPathClick()
                }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box{
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "delete",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    if (!isLoading && !isPlayingState){
                                        deleteExpanded = !deleteExpanded
                                    }
                                },
                                onTap = {
                                    if (!isLoading && !isPlayingState){
                                        deleteCurrentCanvas()
                                    }

                                }
                            )
                        }
                )

                DropdownMenu(
                    expanded = deleteExpanded,
                    onDismissRequest = {
                        deleteExpanded = false
                    }
                ) {
                    Text(
                        text = "Удалить все",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.background)
                            .clickable {
                                deleteExpanded = false
                                deleteAll()
                            }
                    )
                }


            }

            Icon(
                painter = painterResource(id = R.drawable.file_plus),
                contentDescription = "file_plus",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable(!isLoading && !isPlaying) {
                    createNewCanvas()
                }
            )

            Box {
                Icon(
                    painter = painterResource(id = R.drawable.layers),
                    contentDescription = "layers",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.clickable(!isLoading && !isPlaying) {
                        layersExpanded = !layersExpanded
                    }
                )
                Text(
                    text = "${selectedCanvasShotId + 1}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 5.dp, y = 5.dp)
                )

                val configuration = LocalConfiguration.current
                val screenHeight = configuration.screenHeightDp

                LayersDropMenu(
                    modifier = Modifier
                        .height((screenHeight / 1.5).dp),
                    expanded = layersExpanded,
                    canvasList = canvasIds,
                    openCanvas = { id ->
                        selectCanvasShot(id)
                        layersExpanded = !layersExpanded
                    }
                ) {
                    layersExpanded = !layersExpanded
                }
            }

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.stop),
                contentDescription = "stop",
                tint = if (stopIsActive) MaterialTheme.colorScheme.onBackground else NotActiveColor,
                modifier = Modifier.clickable(stopIsActive && !isLoading && isPlaying) {
                    stop()
                }
            )
            Icon(
                painter = painterResource(id = R.drawable.play),
                contentDescription = "play",
                tint = if (playIsActive) MaterialTheme.colorScheme.onBackground else NotActiveColor,
                modifier = Modifier.clickable(playIsActive && !isLoading && !isPlaying) {
                    play()
                }
            )

        }


    }
}