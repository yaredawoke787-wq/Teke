package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.GiftViewModel
import com.example.ui.MainScreen
import com.example.ui.SplashScreen
import com.example.ui.OnboardingScreen
import com.example.ui.theme.MyApplicationTheme

enum class AppScreenState {
    Splash,
    Onboarding,
    Main
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Instantiate our elite master state manager
            val viewModel: GiftViewModel = viewModel {
                GiftViewModel(application)
            }

            // Read theme state flow reactively to enable animated switching
            val isDark by viewModel.isDarkModeFlow.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = isDark) {
                var currentScreen by remember { mutableStateOf(AppScreenState.Splash) }

                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(600)) togetherWith fadeOut(animationSpec = tween(600))
                    },
                    label = "app_routing"
                ) { screenState ->
                    when (screenState) {
                        AppScreenState.Splash -> {
                            SplashScreen(
                                onSplashFinished = { currentScreen = AppScreenState.Onboarding }
                            )
                        }
                        AppScreenState.Onboarding -> {
                            OnboardingScreen(
                                onOnboardingFinished = { currentScreen = AppScreenState.Main }
                            )
                        }
                        AppScreenState.Main -> {
                            MainScreen(
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
