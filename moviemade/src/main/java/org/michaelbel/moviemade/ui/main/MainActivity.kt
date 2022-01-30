package org.michaelbel.moviemade.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.michaelbel.moviemade.app.playcore.InAppUpdate
import org.michaelbel.moviemade.ui.AppTheme

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @Inject lateinit var inAppUpdate: InAppUpdate

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets {
                AppTheme {
                    MainScreen(::onAppUpdateClick)
                }
            }
        }
    }

    private fun onAppUpdateClick() {
        inAppUpdate.startUpdateFlow(this)
    }
}