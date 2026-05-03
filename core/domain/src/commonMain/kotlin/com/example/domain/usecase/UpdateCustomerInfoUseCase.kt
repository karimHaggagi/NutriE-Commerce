package com.example.domain.usecase

import com.example.domain.repository.CustomerRepository
import com.example.model.Customer

class UpdateCustomerInfoUseCase(private val repository: CustomerRepository)  {
    operator fun invoke(customer: Customer) = repository.updateCustomerInfo(customer)
}