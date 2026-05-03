package com.example.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.details.DetailsScreen
import com.example.navigation.routes.DetailsScreen

fun NavController.navigateToDetailsScreen(id: String){
    navigate(DetailsScreen(id))
}
fun NavGraphBuilder.detailsScreen(
    onBackPress: () -> Unit
) {
    composable<DetailsScreen>() {
        DetailsScreen(onBackPress = onBackPress)
    }
}