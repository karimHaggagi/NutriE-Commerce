package com.example.e_commerce

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.e_commerce.ui.ECommerceAppNavigation
import com.example.navigation.routes.AuthScreen
import com.example.navigation.routes.HomeNavigation
import com.example.network.CustomerRemoteDataSource

import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val remoteDataSource = koinInject<CustomerRemoteDataSource>()
    val isUserAuthenticated = remember {  remoteDataSource.getCurrentUserId() != null}

    val startDestination = remember {
        if (isUserAuthenticated)
            HomeNavigation()
        else
            AuthScreen()

    }
    MaterialTheme {
        ECommerceAppNavigation(
            startDestination = startDestination
        )
    }

}