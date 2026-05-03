package com.example.checkout.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.checkout.CheckoutScreen
import com.example.navigation.routes.CheckoutScreen


fun NavController.navigateToCheckoutScreen(totalAmount: Double){
    navigate(CheckoutScreen(totalAmount))
}

fun NavGraphBuilder.checkoutScreen(
    navigateBack: () -> Unit,
    navigateToPaymentCompleted: (success: Boolean?, errorMessage: String?) -> Unit
) {
    composable<CheckoutScreen>() {
        CheckoutScreen(navigateBack =navigateBack, navigateToPaymentCompleted = navigateToPaymentCompleted)
    }
}