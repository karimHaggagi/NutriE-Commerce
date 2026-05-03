package com.example.domain.usecase

import com.example.domain.repository.CustomerRepository

class GetCustomerInfoUseCase(private val customerRepository: CustomerRepository) {
     operator fun invoke() = customerRepository.getCustomerInfo()
}