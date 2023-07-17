package com.vegcale.architecture.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.vegcale.architecture.ui.theme.ArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @AndroidEntryPoint でアノテーションをすることで、そのクラスにコンテナを提供します。
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ArchitectureTheme {
                MainScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    ArchitectureTheme {
        MainScreen()
    }
}