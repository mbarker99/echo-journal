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
import com.mbarker99.echojournal.echos.presentation.echos.EchosRoot
import com.mbarker99.echojournal.echos.presentation.echos.util.toCreateEchoRoute

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Route.Echos
    ) {
        composable<Route.Echos> {
            EchosRoot(
                onNavigateToCreateEcho = { recordingDetails ->
                    navController.navigate(recordingDetails.toCreateEchoRoute())
                }
            )
        }
        composable<Route.CreateEcho> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Create echos screen")
            }
        }
    }
}