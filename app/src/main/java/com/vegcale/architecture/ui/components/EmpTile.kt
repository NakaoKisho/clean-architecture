package com.vegcale.architecture.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmpTile(
    detailFontSize: TextUnit,
    detailText: String,
    modifier: Modifier = Modifier,
    titleFontSize: TextUnit,
    titleText: String,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = titleText,
            modifier = Modifier.fillMaxWidth(),
            fontSize = titleFontSize
        )
        Box(
            modifier = Modifier
                .weight(1.0f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = detailText,
                fontSize = detailFontSize,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmpTilePreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            val tileCount = 2

            for (i in 1..tileCount) {
                EmpTile(
                    detailFontSize = 24.sp,
                    detailText = "test detail text",
                    modifier = Modifier
                        .weight(1.0f)
                        .background(Color.Cyan),
                    titleFontSize = 24.sp,
                    titleText = "test title text"
                )
            }
        }
    }
}