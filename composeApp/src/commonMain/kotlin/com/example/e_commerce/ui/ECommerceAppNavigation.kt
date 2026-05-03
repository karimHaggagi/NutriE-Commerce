package com.example.e_commerce.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.auth.navigation.authScreen
import com.example.home.navigation.homeNavigation
import com.example.navigation.Screens
import com.example.navigation.navigateToScreen
import com.example.navigation.routes.AuthScreen
import com.example.navigation.routes.HomeNavigation

@Composable
fun ECommerceAppNavigation(
    startDestination: Screens
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        authScreen(navigateToHome = {
            navController.navigateToScreen(HomeNavigation(), AuthScreen(),true)
        })

        homeNavigation(
            navigateToAuth = {
                navController.navigateToScreen(AuthScreen(), HomeNavigation(),true)
            }
        )
    }
}
