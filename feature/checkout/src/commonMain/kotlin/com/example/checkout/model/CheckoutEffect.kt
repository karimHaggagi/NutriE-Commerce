package com.example.checkout.model

sealed interface CheckoutEffect {
    data class NavigateToPaymentStatus(val isCompleted: Boolean, val errorMessage: String? = null) : CheckoutEffect
    data class ShowError(val message: String) : CheckoutEffect
    data class OpenExternalLink(val url: String) : CheckoutEffect
}