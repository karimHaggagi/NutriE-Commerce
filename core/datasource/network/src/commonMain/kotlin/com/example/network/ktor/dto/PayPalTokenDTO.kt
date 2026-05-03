package com.example.network.ktor.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PayPalTokenDTO(
    @SerialName("access_token")
    val accessToken: String
)
