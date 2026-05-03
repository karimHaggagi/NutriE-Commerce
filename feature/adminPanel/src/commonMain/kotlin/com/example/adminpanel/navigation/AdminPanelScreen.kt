package com.example.adminpanel.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.adminpanel.AdminPanelScreen
import com.example.navigation.routes.AdminPanelScreen

fun NavController.navigateToAdminPanelScreen(){
    navigate(AdminPanelScreen())
}
fun NavGraphBuilder.adminPanelScreen(
    navigateBack: () -> Unit,
    navigateToManageProduct: (String?) -> Unit
){

    composable<AdminPanelScreen>(){
        AdminPanelScreen(
            navigateBack = navigateBack,
            navigateToManageProduct = navigateToManageProduct
        )
    }
}