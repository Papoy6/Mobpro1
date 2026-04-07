package com.adam0006.mobpro1.navigation

    sealed class Screen(val route: String) {
        data object Home : Screen("mainScreen")
        data object About : Screen("aboutScreen")
    }