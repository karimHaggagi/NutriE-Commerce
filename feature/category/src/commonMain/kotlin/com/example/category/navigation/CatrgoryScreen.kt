package com.example.category.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.category.CategoryScreen
import com.example.model.ProductCategory
import com.example.navigation.routes.CategoryScreen

fun NavController.navigateToCategoryScreen(navOptions: NavOptions?=null){
    navigate(CategoryScreen(),navOptions)
}
fun NavGraphBuilder.categoryScreen(
    navigateToCategoryProducts: (ProductCategory) -> Unit
){
    composable<CategoryScreen>(){
        CategoryScreen(navigateToCategorySearch = navigateToCategoryProducts)
    }
}