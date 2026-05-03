package com.example.home.domain


import com.example.designsystem.Resources
import org.jetbrains.compose.resources.DrawableResource

enum class BottomBarDestination(
    val icon: DrawableResource,
    val title: String,
    val screen: String
) {
    ProductsOverview(
        icon = Resources.Icon.Home,
        title = "Nutri Sport",
        screen = "products_overview"
    ),
    Cart(
        icon = Resources.Icon.ShoppingCart,
        title = "Cart",
        screen = "cart"
    ),
    Categories(
        icon = Resources.Icon.Categories,
        title = "Categories",
        screen = "categories"
    )
}