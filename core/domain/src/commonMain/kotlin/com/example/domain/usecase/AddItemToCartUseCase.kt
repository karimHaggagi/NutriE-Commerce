package com.example.domain.usecase

import com.example.domain.repository.CustomerRepository
import com.example.model.CartItem

class AddItemToCartUseCase(private val customerRepository: CustomerRepository) {
    operator fun invoke(cartItem: CartItem) = customerRepository.addItemToCard(cartItem)
}