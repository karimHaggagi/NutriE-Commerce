package com.example.cart.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.cart.CartScreen
import com.example.navigation.routes.CartScreen

fun NavController.navigateToCartScreen(navOptions: NavOptions?=null){
    navigate(CartScreen(),navOptions)
}
fun NavGraphBuilder.cartScreen(
    navigateToCheckout: (Double) -> Unit
) {
    composable<CartScreen>() {
        CartScreen(navigateToCheckout =navigateToCheckout)
    }
}