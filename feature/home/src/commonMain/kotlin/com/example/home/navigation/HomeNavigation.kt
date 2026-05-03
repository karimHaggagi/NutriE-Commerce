package com.example.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.home.HomeNavigationScreen
import com.example.navigation.routes.HomeNavigation

fun NavGraphBuilder.homeNavigation(
    navigateToAuth: () -> Unit
) {
    composable<HomeNavigation>{
        HomeNavigationScreen(
            navigateToAuth = navigateToAuth
        )
    }
}