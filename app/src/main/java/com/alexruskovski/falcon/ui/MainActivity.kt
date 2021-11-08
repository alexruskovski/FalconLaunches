package com.alexruskovski.falcon.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.alexruskovski.falcon.ui.navigation.FalconAppNavigation
import com.alexruskovski.falcon.ui.theme.FalconAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }

    @Composable
    fun AppContent() {
        FalconAppTheme {
            ProvideWindowInsets {
                val systemUiController = rememberSystemUiController()
                val systemBaseColour = MaterialTheme.colors.primary
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = systemBaseColour,
                        darkIcons = false
                    )
                }
            }
            FalconAppNavigation()
        }
    }
}
