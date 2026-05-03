package com.example.navigation

import androidx.navigation.NavController

fun NavController.navigateToScreen(
    screen: Screens,
    popUpScreen: Screens? = null,
    inclusive: Boolean = false
) {
    this.navigate(screen) {
        popUpScreen?.let {
            popUpTo(popUpScreen) {
                this.inclusive = inclusive
            }
        }
    }
}


