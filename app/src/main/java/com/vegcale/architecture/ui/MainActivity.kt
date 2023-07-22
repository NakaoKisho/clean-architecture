package com.vegcale.architecture.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vegcale.architecture.ui.theme.ArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * @AndroidEntryPoint でアノテーションをすることで、そのクラスにコンテナを提供します。
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EarthquakeMapApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    ArchitectureTheme {
        EarthquakeMapApp()
    }
}