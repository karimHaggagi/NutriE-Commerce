package com.example.domain.usecase

import com.example.domain.repository.OrderRepository
import com.example.model.Order

class CreateOrderUseCase(private val orderRepository: OrderRepository) {
    operator fun invoke(order: Order) = orderRepository.createOrder(order)
}