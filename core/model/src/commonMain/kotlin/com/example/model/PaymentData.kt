package com.example.model

data class PaymentData(
    val isSuccess: Boolean?,
    val errorMessage: String? = null,
    val token: String? = null
)