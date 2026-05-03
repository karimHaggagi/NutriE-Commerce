package com.example.checkout.model

import com.example.model.CartItem
import com.example.model.PhoneNumber
import com.example.model.Country

data class CheckoutUiState(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val city: String? = null,
    val postalCode: Int? = null,
    val address: String? = null,
    val country: Country = Country.Serbia,
    val phoneNumber: PhoneNumber? = null,
    val cartItems: List<CartItem> = emptyList(),
)
