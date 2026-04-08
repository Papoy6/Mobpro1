package com.adam0006.mobpro1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.adam0006.mobpro1.ui.screen.MainScreen
import com.adam0006.mobpro1.navigation.AboutScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.About.route) {
            AboutScreen(navController)
        }
    }
}