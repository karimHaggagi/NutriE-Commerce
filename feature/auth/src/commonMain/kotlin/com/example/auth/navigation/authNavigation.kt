package com.example.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.auth.AuthScreen
import com.example.navigation.routes.AuthScreen

fun NavGraphBuilder.authScreen(
    navigateToHome: () -> Unit
) {

    composable<AuthScreen>{
        AuthScreen(navigateToHome =navigateToHome)
    }
}