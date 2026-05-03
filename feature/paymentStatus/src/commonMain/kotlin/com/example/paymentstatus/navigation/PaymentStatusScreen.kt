package com.example.paymentstatus.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.example.navigation.routes.CheckoutScreen
import com.example.navigation.routes.HomeRoute
import com.example.navigation.routes.PaymentStatusScreen
import com.example.paymentstatus.PaymentStatusScreen
import com.example.paymentstatus.model.PaymentStatusModel
import kotlinx.serialization.Serializable

fun NavController.navigateToPaymentStatusScreen(
    isCompleted: Boolean,
    errorMessage: String? = null
) {
    navigate(PaymentStatusScreen(isCompleted, errorMessage)) {
        launchSingleTop = true
        popUpTo(HomeRoute) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.paymentStatusScreen(
    navigateBack: () -> Unit
) {
    composable<PaymentStatusScreen>() {
        val navModel = it.toRoute<PaymentStatusScreen>()

        val statusModel = remember {
            if (navModel.isCompleted) PaymentStatusModel.Success
            else PaymentStatusModel.Failure(
                navModel.errorMessage ?: "An error occurred during the payment process."
            )
        }

        PaymentStatusScreen(
            model = statusModel,
            navigateBack = navigateBack
        )
    }
}