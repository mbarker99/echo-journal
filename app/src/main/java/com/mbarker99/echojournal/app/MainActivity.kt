package com.mbarker99.echojournal.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.mbarker99.echojournal.app.navigation.NavigationRoot
import com.mbarker99.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.mbarker99.echojournal.echos.presentation.echos.EchosRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EchoJournalTheme {
                NavigationRoot(
                    navController = rememberNavController()
                )
            }
        }
    }
}