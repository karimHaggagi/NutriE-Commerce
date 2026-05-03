package com.example.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.adminpanel.navigation.adminPanelScreen
import com.example.cart.navigation.cartScreen
import com.example.category.navigation.categoryScreen
import com.example.categoryproducts.navigation.categoryProductsScreen
import com.example.categoryproducts.navigation.navigateToCategoryProductsScreen
import com.example.checkout.navigation.checkoutScreen
import com.example.checkout.navigation.navigateToCheckoutScreen
import com.example.details.navigation.detailsScreen
import com.example.details.navigation.navigateToDetailsScreen
import com.example.manageproduct.navigation.manageProductScreen
import com.example.manageproduct.navigation.navigateToManageProductScreen
import com.example.navigation.routes.HomeRoute
import com.example.navigation.routes.ProductOverviewScreen
import com.example.paymentstatus.navigation.navigateToPaymentStatusScreen
import com.example.paymentstatus.navigation.paymentStatusScreen
import com.example.productoverview.navigation.productOverviewScreen
import com.example.profile.navigation.profileScreen


fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
) {
    navigation<HomeRoute>(
        startDestination = ProductOverviewScreen()
    ) {
        productOverviewScreen(navigateToProductDetails = {
            navController.navigateToDetailsScreen(it)
        })
        cartScreen(navigateToCheckout = {
            navController.navigateToCheckoutScreen(it)
        })

        categoryScreen(navigateToCategoryProducts = {
            navController.navigateToCategoryProductsScreen(it.name)
        })
        categoryProductsScreen(
            navigateBack = {
                navController.popBackStack()
            },
            navigateToDetails = { id ->
                navController.navigateToDetailsScreen(id)
            }
        )
        profileScreen(navigateBack = {
            navController.popBackStack()
        })
        adminPanelScreen(
            navigateBack = {
                navController.popBackStack()
            },
            navigateToManageProduct = { id ->
                navController.navigateToManageProductScreen(id)
            }
        )

        manageProductScreen(navigateBack = {
            navController.popBackStack()
        })

        detailsScreen(onBackPress = {
            navController.popBackStack()
        })

        checkoutScreen(
            navigateBack = {
                navController.popBackStack()
            },
            navigateToPaymentCompleted = { success, errorMessage ->
                navController.navigateToPaymentStatusScreen(
                        isCompleted = success ?: false,
                        errorMessage = errorMessage
                )
            })
        paymentStatusScreen(navigateBack = {
            navController.navigate(HomeRoute) {
                launchSingleTop = true
                popUpTo(HomeRoute) {
                    inclusive = true
                }
            }
        })
        /*
        composable(
            route = PAYPAL_DEEPLINK_ROUTE,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "com.karim.ecommerce://paypalpay?success={success}"
                },
                navDeepLink {
                    uriPattern = "com.karim.ecommerce://paypalpay?cancel={cancel}"
                }
            )
        ) { backStackEntry ->

            val success = backStackEntry.savedStateHandle.get<String>("success") == "true"
            val cancel = backStackEntry.savedStateHandle.get<String>("cancel") == "true"


            val errorMessage = when {
                cancel -> "Payment was cancelled"
                !success -> "Payment failed"
                else -> null
            }


            navController.navigate(
                PaymentStatusScreen(
                    isCompleted = success,
                    errorMessage = errorMessage
                )
            ) {
                launchSingleTop = true
                popUpTo(HomeRoute) {
                    inclusive = true
                }
            }

        }*/
    }
}