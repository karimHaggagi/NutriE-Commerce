package com.example.cart.model

sealed interface ProductCartEffect {
    data class ShowSuccessMessage(val message: String) : ProductCartEffect
    data class ShowErrorMessage(val message: String) : ProductCartEffect
}