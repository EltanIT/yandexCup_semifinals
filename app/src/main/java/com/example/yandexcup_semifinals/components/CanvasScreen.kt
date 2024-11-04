package com.example.yandexcup_semifinals.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.yandexcup_semifinals.R
import com.example.yandexcup_semifinals.canvas.DrawFigure
import com.example.yandexcup_semifinals.canvas.DrawType
import com.example.yandexcup_semifinals.canvas.PathData
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun CanvasScreen(
    modifier: Modifier = Modifier,
    canvasPreviousPaths: List<PathData>,
    currentCanvasPaths: List<PathData>,
    allCanvasShots: List<List<PathData>>,
    selectedFigure: DrawFigure?,
    isLoading: Boolean,
    isPlaying: Boolean,
    playSpeed: Long,

    actionsType: BottomPanelActionsType,
    onChangePath: (Path, Boolean) -> Unit,
    onCanvasSizeInit: (IntSize) -> Unit
) {

    var tempPath = Path()



    var scale by remember { mutableFloatStateOf(1f) }

    Box(
        modifier
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.canvas_back),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )



        if (isPlaying){
            var currentCanvas by remember{
                mutableStateOf<List<PathData>?>(null)
            }
            LaunchedEffect(key1 = Unit) {
                while (true){
                    allCanvasShots.forEach { canvas ->
                        currentCanvas = canvas
                        delay(playSpeed)
                    }

                }
            }

            Canvas(modifier = Modifier
                .fillMaxSize()) {
                currentCanvas?.forEach { pathData ->
                    drawPath(
                        pathData.path,
                        pathData.color,
                        style = Stroke(
                            width = pathData.drawType.width,
                            cap = pathData.drawType.cap
                        )
                    )

                }
            }

        }
        else{

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
                    .onGloballyPositioned { coor ->
                        onCanvasSizeInit(coor.size)
                    }
                    .pointerInput(!isLoading, actionsType) {

                        detectDragGestures(
                            onDragStart = {
                                tempPath = Path()
                            },
                            onDragEnd = {
                                if (actionsType != BottomPanelActionsType.FIGURE) {
                                    onChangePath(
                                        tempPath,
                                        false
                                    )
                                }

                            }

                        ) { change, dragAmount ->

                            Log.i(
                                "canvasScreen",
                                "change: ${change.position.x} ${change.position.y}\n dragAmount: ${dragAmount.x} ${dragAmount.y}"
                            )

                            if (actionsType != BottomPanelActionsType.FIGURE) {
                                tempPath.moveTo(
                                    change.position.x - dragAmount.x,
                                    change.position.y - dragAmount.y,
                                )

                                tempPath.lineTo(
                                    change.position.x,
                                    change.position.y,
                                )

                                onChangePath(
                                    tempPath,
                                    true
                                )
                            }

                        }
                    }
                    .pointerInput(isLoading, selectedFigure, actionsType) {
                        detectTapGestures(
                            onTap = { change ->
                                if (actionsType == BottomPanelActionsType.FIGURE) {

                                    Log.i(
                                        "canvasScreenFigure", "onTap ${selectedFigure?.name}"
                                    )

                                    val path = Path()
                                    val sideLength = Random
                                        .nextInt(50, 250)
                                        .toFloat()

                                    when (selectedFigure) {
                                        DrawFigure.TRIANGLE -> {
                                            path.apply {

                                                moveTo(change.x, change.y)
                                                lineTo(change.x + sideLength, change.y)
                                                lineTo(
                                                    change.x + sideLength / 2,
                                                    change.y + sideLength * 0.866f
                                                )
                                                close()
                                            }
                                        }

                                        DrawFigure.SQUARE -> {
                                            path.apply {

                                                moveTo(change.x, change.y)
                                                lineTo(change.x + sideLength, change.y)
                                                lineTo(change.x + sideLength, change.y + sideLength)
                                                lineTo(change.x, change.y + sideLength)
                                                close()
                                            }
                                        }

                                        DrawFigure.ARROW -> {

                                        }

                                        null -> return@detectTapGestures
                                        DrawFigure.STAR -> {
                                            val half = sideLength / 2

                                            path.apply {
                                                moveTo(change.x + half * 0.5f, change.y + half * 0.84f);
                                                lineTo(change.x + (half * 1.5f), change.y + (half * 0.84f));
                                                lineTo(change.x + (half * 0.68f), change.y + (half * 1.45f));
                                                lineTo(change.x + (half * 1.0f),change.y + (half * 0.5f));
                                                lineTo(change.x + (half * 1.32f),change.y + (half * 1.45f));

                                                close()
                                            }



                                        }
                                    }


                                    onChangePath(path, false)
                                    onChangePath(path, false)

                                }
                            }
                        )
                    }
//                    .pointerInput(zoomEnabled){
//                        detectTransformGestures { _, _, zoom, _ ->
//                            if (zoomEnabled){
//                                scale = (scale * zoom).coerceIn(0.5f, 10f)
//                            }
//                            Log.i("canvasScale", "${zoom}")
//                        }
//                    }

            ) {

                canvasPreviousPaths.forEach { pathData ->
                    drawPath(
                        pathData.path,
                        pathData.color.copy(
                            alpha = 0.3f
                        ),
                        style = Stroke(
                            width = pathData.drawType.width,
                            cap = pathData.drawType.cap
                        )
                    )
                }

                currentCanvasPaths.forEach { pathData ->
                    drawPath(
                        pathData.path,
                        pathData.color,
                        style = Stroke(
                            width = pathData.drawType.width,
                            cap = pathData.drawType.cap
                        )
                    )
                }


            }
        }

    }
}
