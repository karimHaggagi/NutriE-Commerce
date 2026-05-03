package com.example.domain.usecase

import com.example.domain.repository.OrderRepository

class GetIntentPaymentDataUseCae(private val orderRepository: OrderRepository) {
    operator fun invoke() = orderRepository.getPaymentData()
}