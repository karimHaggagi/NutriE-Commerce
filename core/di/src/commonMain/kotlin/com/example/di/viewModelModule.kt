package com.example.di

import com.example.adminpanel.AdminPanelViewModel
import com.example.auth.AuthViewModel
import com.example.details.DetailsViewModel
import com.example.home.HomeNavigationViewModel
import com.example.manageproduct.ManageProductViewModel
import com.example.productoverview.ProductOverviewViewViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.example.profile.ProfileViewModel
import com.example.cart.CartViewModel
import com.example.categoryproducts.CategoryProductsViewModel
import com.example.checkout.CheckoutViewModel

val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeNavigationViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ManageProductViewModel)
    viewModelOf(::AdminPanelViewModel)
    viewModelOf(::ProductOverviewViewViewModel)
    viewModelOf(::DetailsViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::CategoryProductsViewModel)
    viewModelOf(::CheckoutViewModel)
}