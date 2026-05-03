package com.example.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.navigation.routes.ProfileScreen
import com.example.profile.ProfileScreen

fun NavController.navigateToProfileScreen(){
    navigate(ProfileScreen())
}
fun NavGraphBuilder.profileScreen(
    navigateBack: () -> Unit
){
    composable<ProfileScreen>(){
        ProfileScreen(navigateBack = navigateBack)
    }
}