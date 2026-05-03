package com.example.navigation.routes

import com.example.navigation.Screens
import kotlinx.serialization.Serializable


const val AUTH_ROUTE = "auth_route"

@Serializable
class AuthScreen: Screens(AUTH_ROUTE)