package com.example.yandexcup_semifinals

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yandexcup_semifinals.canvas.DrawFigure
import com.example.yandexcup_semifinals.canvas.DrawType
import com.example.yandexcup_semifinals.canvas.PathData
import com.example.yandexcup_semifinals.components.BottomPanelActionsType
import com.example.yandexcup_semifinals.ui.canvasPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random


class MainViewModel: ViewModel() {

    private val _selectedColor = mutableStateOf(Color.White)
    val selectedColor: State<Color> = _selectedColor

    private val _selectedAction = mutableStateOf(BottomPanelActionsType.PENCIL)
    val selectedAction: State<BottomPanelActionsType> = _selectedAction

    private val _selectedFigure = mutableStateOf<DrawFigure?>(null)
    val selectedFigure: State<DrawFigure?> = _selectedFigure

    private val _pencilWidth = mutableFloatStateOf(1f)
    val pencilWidth: State<Float> = _pencilWidth

    private var drawType: DrawType = DrawType.PENCIL()




    private val historyCurrentCanvas = mutableListOf<PathData>()

    private val _canvasCurrentPaths = mutableStateListOf<PathData>()
    val canvasCurrentPaths: SnapshotStateList<PathData> = _canvasCurrentPaths

    private val _canvasPreviousPaths = mutableStateListOf<PathData>()
    val canvasPreviousPaths: SnapshotStateList<PathData> = _canvasPreviousPaths


    private val _canvasIsBackPathAccess = mutableStateOf(false)
    val canvasIsBackPathAccess: State<Boolean> = _canvasIsBackPathAccess

    private val _canvasIsReturnPathAccess = mutableStateOf(false)
    val canvasIsReturnPathAccess: State<Boolean> = _canvasIsReturnPathAccess


    private val _canvasIsPlayAccess = mutableStateOf(false)
    val canvasIsPlayAccess: State<Boolean> = _canvasIsPlayAccess


    private val _canvasIsPlay = mutableStateOf(false)
    val IsPlaying: State<Boolean> = _canvasIsPlay


    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading


    private val _playingSpeed = mutableLongStateOf(100L)
    val playingSpeed: State<Long> = _playingSpeed



    private val _allCanvasShots = mutableStateListOf<List<PathData>>()
    val allCanvasShots: SnapshotStateList<List<PathData>> = _allCanvasShots


    private val _selectedCanvasShotId = mutableIntStateOf(0)
    val selectedCanvasShotId: State<Int> = _selectedCanvasShotId

    private var selectedCanvasPathId = -1

    private var canvasSize: IntSize? = null



    init {
        _allCanvasShots.add(canvasCurrentPaths.toList())
    }


    fun onEvent(event: MainEvents){
        _isLoading.value = true
        when(event){
            MainEvents.Play -> {
                if (canvasIsPlayAccess.value){
                    val newList = ArrayList<PathData>()
                    newList.addAll(canvasCurrentPaths)
                    _allCanvasShots[_selectedCanvasShotId.intValue] = newList

                    _canvasIsPlay.value = true
                    _canvasIsPlayAccess.value = false
                }
            }
            MainEvents.Stop -> {
                if (IsPlaying.value){
                    _canvasIsPlay.value = false
                    _canvasIsPlayAccess.value = true
                }
            }


            is MainEvents.ChangeSelectedColor -> {
                _selectedColor.value = event.color
            }
            is MainEvents.ChangeSelectedFigure -> {
                _selectedFigure.value = event.figure
            }
            is MainEvents.ChangeSelectedAction -> {
                _selectedAction.value = event.action

                when(event.action){
                    BottomPanelActionsType.PENCIL -> {
                        drawType = DrawType.PENCIL()
                        _selectedFigure.value = null
                    }
                    BottomPanelActionsType.BRUSH -> {
                        drawType = DrawType.BRUSH()
                        _selectedFigure.value = null
                    }
                    BottomPanelActionsType.FIGURE -> {
                        drawType = DrawType.FIGURE()
                    }
                    BottomPanelActionsType.ERASE -> {
                        drawType = DrawType.ERASE()
                        _selectedFigure.value = null
                    }
                    else -> Unit
                }
            }
            is MainEvents.ChangePencilAndEraseWidth -> {
                _pencilWidth.floatValue = event.width
            }


            is MainEvents.AddPathInCurrentCanvas -> {
                when(drawType::class){
                    DrawType.PENCIL::class -> {
                        drawType = DrawType.PENCIL()
                        _selectedFigure.value = null
                    }
                    DrawType.BRUSH::class -> {
                        drawType = DrawType.BRUSH()
                        _selectedFigure.value = null
                    }
                    DrawType.FIGURE::class -> {
                        drawType = DrawType.FIGURE()
                    }
                    DrawType.ERASE::class -> {
                        drawType = DrawType.ERASE()
                        _selectedFigure.value = null
                    }
                    else -> Unit
                }

                if (
                    drawType::class == DrawType.PENCIL::class
                    || drawType::class == DrawType.ERASE()::class
                    ) drawType.width = pencilWidth.value

                val pathData = PathData(
                    path = event.path,
                    color = selectedColor.value,
                    drawType = drawType
                )


                if (event.isDelete){
                    if (canvasCurrentPaths.isNotEmpty()){
                        _canvasCurrentPaths.removeLastOrNull()
                    }

                    if (historyCurrentCanvas.isNotEmpty()){
                        historyCurrentCanvas.removeLastOrNull()
                    }
                }else{
                    selectedCanvasPathId = canvasCurrentPaths.size-1
                }


                _canvasCurrentPaths.add(
                    pathData
                )
                historyCurrentCanvas.add(
                    pathData
                )

                Log.i("canvasInfo", "history: ${historyCurrentCanvas.size}")
                Log.i("canvasInfo", "currentCanvas: ${canvasCurrentPaths.size}")

                checkTopPanelStates()
            }
            is MainEvents.ErasePathInCurrentCanvas -> {

            }



            MainEvents.DeleteLastPathInCurrentCanvas -> {
                if (canvasIsBackPathAccess.value){
                    selectedCanvasPathId--
                    _canvasCurrentPaths.removeIf{
                        item ->
                        item == canvasCurrentPaths.lastOrNull()
                    }
                    canvasCurrentPaths.lastOrNull()?.let { _canvasCurrentPaths.add(it) }
                }
                checkTopPanelStates()
            }
            MainEvents.ReturnLastPathInCurrentCanvas -> {
                if (canvasIsReturnPathAccess.value){
                    selectedCanvasPathId++
                    val addedElement = historyCurrentCanvas[selectedCanvasPathId]
                    _canvasCurrentPaths.removeLastOrNull()
                    _canvasCurrentPaths.add(addedElement)
                    _canvasCurrentPaths.add(addedElement)
                }
                checkTopPanelStates()
            }



            MainEvents.CreateNewCanvas -> {
                historyCurrentCanvas.clear()
                _canvasPreviousPaths.clear()
                selectedCanvasPathId = -1

                if (_allCanvasShots.getOrNull(_selectedCanvasShotId.intValue) != null){
                    val newList = ArrayList<PathData>()
                    newList.addAll(canvasCurrentPaths)
                    _allCanvasShots[_selectedCanvasShotId.intValue] = newList
                    _canvasPreviousPaths.addAll(newList)
                }

                _canvasCurrentPaths.clear()
                val newList = ArrayList<PathData>()
                newList.addAll(canvasCurrentPaths)
                _allCanvasShots.add(newList)

                _selectedCanvasShotId.intValue = _allCanvasShots.size-1

                checkTopPanelStates()

            }
            MainEvents.DeleteCurrentCanvas -> {
                historyCurrentCanvas.clear()
                _canvasCurrentPaths.clear()
                _canvasPreviousPaths.clear()

                if (_allCanvasShots.getOrNull(_selectedCanvasShotId.intValue) != null){
                    _allCanvasShots.removeAt(_selectedCanvasShotId.intValue)
                    if (_allCanvasShots.isNotEmpty()){
                        _canvasCurrentPaths.addAll(_allCanvasShots.last())
                        historyCurrentCanvas.addAll(allCanvasShots.last())
                        if (_allCanvasShots.size>1){
                            _canvasPreviousPaths.addAll(_allCanvasShots[_allCanvasShots.size-2])
                        }
                    }else{
                        val newList = ArrayList<PathData>()
                        newList.addAll(canvasCurrentPaths)
                        _allCanvasShots.add(newList)
                    }
                }
                _selectedCanvasShotId.intValue = _allCanvasShots.size-1
                selectedCanvasPathId = canvasCurrentPaths.size-2
                checkTopPanelStates()
            }

            is MainEvents.SelectCanvasByIndex -> {
                historyCurrentCanvas.clear()
                _canvasPreviousPaths.clear()

                if (_allCanvasShots.getOrNull(_selectedCanvasShotId.intValue) != null){
                    val newList = ArrayList<PathData>()
                    newList.addAll(canvasCurrentPaths)
                    _allCanvasShots[_selectedCanvasShotId.intValue] = newList
                }

                _canvasCurrentPaths.clear()


                if (_allCanvasShots.getOrNull(event.index) != null){
                    _selectedCanvasShotId.intValue = event.index
                    _canvasCurrentPaths.addAll(_allCanvasShots[event.index])
                    historyCurrentCanvas.addAll(_allCanvasShots[event.index])
                    if (event.index>0){
                        _canvasPreviousPaths.addAll(_allCanvasShots[event.index-1])
                    }
                }

                selectedCanvasPathId = canvasCurrentPaths.size-2
                checkTopPanelStates()
            }


            is MainEvents.GenerateCanvasShots -> {

                val list = ArrayList<PathData>()
                list.addAll(canvasCurrentPaths)
                _allCanvasShots[selectedCanvasShotId.value] = list

                val drawType = DrawType.FIGURE()
                val figures = listOf(
                    DrawFigure.TRIANGLE,
                    DrawFigure.SQUARE,
                    DrawFigure.STAR,
                )

                viewModelScope.launch(Dispatchers.IO) {
                    for(i in 1..event.count){

                        val randomCanvas = buildList { repeat(Random.nextInt(5, 15)) {

                            val drawFigure = figures.random()
                            add(
                                PathData(
                                    path = Path().apply {
                                        canvasSize?.let { size ->

                                            val sideLength = Random
                                                .nextInt(50, 250)
                                                .toFloat()

                                            val randomX = Random.nextInt(
                                                0,
                                                (size.width - sideLength).toInt()
                                            ).toFloat()

                                            val randomY = Random.nextInt(
                                                0,
                                                (size.height - sideLength).toInt()
                                            ).toFloat()



                                            when(drawFigure){
                                                DrawFigure.TRIANGLE -> {
                                                    moveTo(randomX, randomY)
                                                    lineTo(randomX + sideLength, randomY)
                                                    lineTo(
                                                        randomX + sideLength / 2,
                                                        randomY + sideLength * 0.866f
                                                    )
                                                    close()
                                                }

                                                DrawFigure.SQUARE -> {
                                                    moveTo(randomX, randomY)
                                                    lineTo(randomX + sideLength, randomY)
                                                    lineTo(randomX + sideLength, randomY + sideLength)
                                                    lineTo(randomX, randomY + sideLength)
                                                    close()
                                                }

                                                DrawFigure.STAR -> {
                                                    val half = sideLength / 2


                                                    moveTo(randomX + half * 0.5f, randomY + half * 0.84f);
                                                    lineTo(randomX + (half * 1.5f), randomY + (half * 0.84f));
                                                    lineTo(randomX + (half * 0.68f), randomY + (half * 1.45f));
                                                    lineTo(randomX + (half * 1.0f),randomY + (half * 0.5f));
                                                    lineTo(randomX + (half * 1.32f),randomY + (half * 1.45f));

                                                    close()


                                                }
                                                else -> Unit
                                            }
                                        }
                                    },
                                    color = canvasPalette.random().random(),
                                    drawType = drawType
                                )
                            )
                        } }

                        _allCanvasShots.add(
                            randomCanvas
                        )
                    }


                    _canvasCurrentPaths.clear()
                    _canvasCurrentPaths.addAll(allCanvasShots[allCanvasShots.size-1])

                    historyCurrentCanvas.clear()
                    historyCurrentCanvas.addAll(allCanvasShots[allCanvasShots.size-1])

                    _canvasPreviousPaths.clear()
                    _canvasPreviousPaths.addAll(allCanvasShots[allCanvasShots.size-2])

                    selectedCanvasPathId = canvasCurrentPaths.size - 2
                    _selectedCanvasShotId.intValue = allCanvasShots.size - 1

                    checkTopPanelStates()
                }

            }
            is MainEvents.InitCanvasSize -> {
                canvasSize = event.size
            }


            MainEvents.DeleteAll -> {
                _allCanvasShots.clear()
                _canvasCurrentPaths.clear()
                historyCurrentCanvas.clear()
                _canvasPreviousPaths.clear()

                _allCanvasShots.add(canvasCurrentPaths.toList())

                selectedCanvasPathId = -1
                _selectedCanvasShotId.intValue = 0

                checkTopPanelStates()
            }



            MainEvents.DublicateCanvas -> {
                _canvasPreviousPaths.clear()

                var newCanvas = ArrayList<PathData>()
                newCanvas.addAll(canvasCurrentPaths)
                _allCanvasShots[selectedCanvasShotId.value] = newCanvas

                newCanvas = ArrayList()
                newCanvas.addAll(canvasCurrentPaths)
                _allCanvasShots.add(newCanvas)


                _canvasCurrentPaths.clear()

                _canvasCurrentPaths.addAll(newCanvas)

                if (allCanvasShots.size>1){
                    _canvasPreviousPaths.addAll(allCanvasShots[allCanvasShots.size-2])
                }

                _selectedCanvasShotId.intValue = allCanvasShots.size-1
                selectedCanvasPathId = canvasCurrentPaths.size-2

                checkTopPanelStates()
            }

            is MainEvents.ChangePlayingSpeed -> {
                _playingSpeed.longValue = event.duration
            }
        }

        _isLoading.value = false
    }


    private fun checkTopPanelStates() {
        viewModelScope.launch(Dispatchers.IO) {
            _canvasIsBackPathAccess.value = selectedCanvasPathId >= 0
            _canvasIsReturnPathAccess.value = selectedCanvasPathId < historyCurrentCanvas.size-2
            _canvasIsPlayAccess.value = _allCanvasShots.size > 1
        }
    }
}
