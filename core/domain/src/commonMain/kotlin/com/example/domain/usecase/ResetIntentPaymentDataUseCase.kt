package com.example.domain.usecase

import com.example.domain.repository.OrderRepository

class ResetIntentPaymentDataUseCase(private val orderRepository: OrderRepository) {
    operator fun invoke() = orderRepository.resetPaymentData()
}