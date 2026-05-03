package com.example.productoverview.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.navigation.routes.ProductOverviewScreen
import com.example.productoverview.ProductOverviewScreen


fun NavController.navigateToProductOverviewScreen(navOptions: NavOptions?=null){
    navigate(ProductOverviewScreen(),navOptions)
}
fun NavGraphBuilder.productOverviewScreen(
    navigateToProductDetails: (String) -> Unit
) {
    composable<ProductOverviewScreen>() {
        ProductOverviewScreen(navigateToProductDetails = navigateToProductDetails)
    }
}