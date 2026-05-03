package com.example.domain.usecase

import com.example.domain.repository.OrderRepository
import com.example.model.Customer
import com.example.model.Order

class BeginPaymentTransactionUseCase(private val repository: OrderRepository) {
    operator fun invoke(order: Order,customer: Customer) = repository.beginCheckout(order,customer)
}