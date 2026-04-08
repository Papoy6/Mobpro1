package com.adam0006.mobpro1.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object About : Screen("about")
}