package com.example.domain.usecase

import com.example.domain.repository.CustomerRepository
import com.example.model.CartItem

class DeleteCartItemUseCase(private val customerRepository: CustomerRepository) {
    operator fun invoke(cartItem: CartItem) = customerRepository.deleteCartItem(cartItem)
}