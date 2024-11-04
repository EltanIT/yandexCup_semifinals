package com.example.yandexcup_semifinals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun LayersDropMenu(
    modifier: Modifier,
    expanded: Boolean,
    canvasList: List<Int>,
    openCanvas: (Int) -> Unit,
    onClose: () -> Unit
) {

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onClose
    ) {
        Box(
            modifier = modifier
                .width(80.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn{
                items(canvasList){ id ->
                    Text(
                        text = "Кадр ${id + 1}",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                openCanvas(id)
                            }
                    )

                }
            }
        }
    }
}