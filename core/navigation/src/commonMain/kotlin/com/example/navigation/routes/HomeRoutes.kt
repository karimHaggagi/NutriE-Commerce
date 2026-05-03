package com.example.navigation.routes

import com.example.navigation.Screens
import kotlinx.serialization.Serializable

const val PRODUCT_OVERVIEW_SCREEN = "productOverview"
const val CART_SCREEN_ROUTE = "cart_screen"
const val CATEGORY_SCREEN_ROUTE = "category_screen"
const val CATEGORY_PRODUCTS_SCREEN_ROUTE = "category_products_screen_route"
const val PROFILE_ROUTE = "profile_route"
const val ADMIN_PANEL_ROUTE = "admin_panel_route"
const val MANAGE_PRODUCT_ROUTE = "manage_product_route"
const val DETAILS_SCREEN_ROUTE = "details_screen"
const val CHECKOUT_SCREEN_ROUTE = "checkout_screen"
private const val PAYMENT_STATUS_SCREEN_ROUTE = "payment_status_screen_route"
const val PAYPAL_DEEPLINK_ROUTE = "paypal_deeplink_route"
const val HOME_ROUTE = "home_route"

@Serializable
class HomeNavigation : Screens(HOME_ROUTE)

@Serializable
object HomeRoute


@Serializable
class ProductOverviewScreen : Screens(PRODUCT_OVERVIEW_SCREEN)

@Serializable
class CartScreen : Screens(CART_SCREEN_ROUTE)

@Serializable
class CategoryScreen() : Screens(CATEGORY_SCREEN_ROUTE)

@Serializable
class CategoryProductsScreen(val categoryName: String) : Screens(CATEGORY_PRODUCTS_SCREEN_ROUTE)

@Serializable
class ProfileScreen : Screens(PROFILE_ROUTE)

@Serializable
class AdminPanelScreen : Screens(ADMIN_PANEL_ROUTE)


@Serializable
class ManageProductScreen(val id: String? = null) : Screens(MANAGE_PRODUCT_ROUTE)

@Serializable
class DetailsScreen(val id: String?) : Screens(DETAILS_SCREEN_ROUTE)

@Serializable
class CheckoutScreen(val totalAmount: Double) : Screens(CHECKOUT_SCREEN_ROUTE)

@Serializable
class PaymentStatusScreen(val isCompleted: Boolean, val errorMessage: String? = null) :
    Screens(PAYMENT_STATUS_SCREEN_ROUTE)
