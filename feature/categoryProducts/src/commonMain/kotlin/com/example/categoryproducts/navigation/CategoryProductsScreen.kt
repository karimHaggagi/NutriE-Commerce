package com.example.categoryproducts.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.categoryproducts.CategoryProductsScreen
import com.example.navigation.routes.CartScreen
import com.example.navigation.routes.CategoryProductsScreen

fun NavController.navigateToCategoryProductsScreen(categoryName: String){
    navigate(CategoryProductsScreen(categoryName))
}
fun NavGraphBuilder.categoryProductsScreen(
    navigateBack: () -> Unit,
    navigateToDetails: (String) -> Unit
){
    composable<CategoryProductsScreen>(){
        CategoryProductsScreen(
            navigateBack = navigateBack,
            navigateToDetails = navigateToDetails
        )
    }
}