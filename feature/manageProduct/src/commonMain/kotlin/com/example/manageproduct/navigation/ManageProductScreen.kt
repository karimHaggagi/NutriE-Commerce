package com.example.manageproduct.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.manageproduct.ManageProductScreen
import com.example.navigation.routes.ManageProductScreen
import com.example.navigation.routes.ProfileScreen

fun NavController.navigateToManageProductScreen(id: String?=null){
    navigate(ManageProductScreen(id))
}

fun NavGraphBuilder.manageProductScreen(navigateBack: () -> Unit) {
    composable<ManageProductScreen>() {
        val id = it.toRoute<ManageProductScreen>().id
        ManageProductScreen(
            navigateBack = navigateBack
        )
    }

}