package com.mbarker99.echojournal.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.mbarker99.echojournal.echos.presentation.create_echo.CreateEchoRoot
import com.mbarker99.echojournal.echos.presentation.echos.EchosRoot
import com.mbarker99.echojournal.echos.presentation.echos.util.toCreateEchoRoute
import com.mbarker99.echojournal.echos.presentation.settings.SettingsRoot


const val ACTION_CREATE_ECHO = "com.mbarker99.CREATE_ECHO"

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Route.Echos(
            startRecording = false
        )
    ) {
        composable<Route.Echos>(
            deepLinks = listOf(
                navDeepLink<Route.Echos>(
                    basePath = "https://echojournal.com/echos"
                ) {
                    action = ACTION_CREATE_ECHO
                }
            )
        ) {
            EchosRoot(
                onNavigateToCreateEcho = { recordingDetails ->
                    navController.navigate(recordingDetails.toCreateEchoRoute())
                },
                onNavigateToSettings = { navController.navigate(Route.Settings) }
            )
        }
        composable<Route.CreateEcho> {
            CreateEchoRoot(
                onConfirmLeave = navController::navigateUp
            )
        }

        composable<Route.Settings> {
            SettingsRoot(
                onNavigateBack = navController::navigateUp
            )
        }
    }
}