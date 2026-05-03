package com.example.home.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.example.cart.navigation.navigateToCartScreen
import com.example.category.navigation.navigateToCategoryScreen
import com.example.checkout.navigation.navigateToCheckoutScreen
import com.example.home.domain.BottomBarDestination
import com.example.navigation.routes.CartScreen
import com.example.navigation.routes.CategoryScreen
import com.example.navigation.routes.ProductOverviewScreen
import com.example.productoverview.ProductOverviewScreen
import com.example.productoverview.navigation.navigateToProductOverviewScreen

fun navigateToTopLevelDestination(
    navController: NavHostController,
    topLevelDestination: BottomBarDestination
) {
//    trace("Navigation: ${topLevelDestination.name}") {
    val topLevelNavOptions = navOptions {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

    when (topLevelDestination) {
        BottomBarDestination.ProductsOverview -> navController.navigateToProductOverviewScreen(topLevelNavOptions)
        BottomBarDestination.Cart -> navController.navigateToCartScreen(topLevelNavOptions)
        BottomBarDestination.Categories -> navController.navigateToCategoryScreen(topLevelNavOptions)
    }
}

fun NavDestination.isTopLevelDestination(): Boolean {
   return hasRoute<ProductOverviewScreen>() ||  hasRoute<CartScreen>() || hasRoute<CategoryScreen>()
}